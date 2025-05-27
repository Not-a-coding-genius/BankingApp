**PROCEDURES** 
1. *MarkInactiveAccountsBasedOnTransactions*
```sql
BEGIN
    UPDATE accounts a
    SET a.status = 'INACTIVE'
    WHERE a.status NOT IN ('INACTIVE', 'BLOCKED')  -- Skip INACTIVE and BLOCKED accounts
      AND NOT EXISTS (
          SELECT 1
          FROM transactions t
          WHERE (t.from_account_id = a.id OR t.to_account_id = a.id)
            AND t.completed_at >= DATE_SUB(CURDATE(), INTERVAL 5 YEAR)
            AND t.transaction_status = 'COMPLETED'
      );
END
```
2. *add_beneficiary*
```sql
   BEGIN
    DECLARE nominee_count INT;
    DECLARE account_exists INT;

    -- Check if account exists in 'accounts' table
    SELECT COUNT(*) INTO account_exists
    FROM accounts
    WHERE id = p_account_id;

    IF account_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Account does not exist.';
    END IF;

    -- Validate age constraints
    IF p_age < 20 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Beneficiary must be at least 20 years old.';
    END IF;

    IF p_age > 85 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Beneficiary cannot be older than 85 years.';
    END IF;

    -- Validate relationship
    IF p_relationship NOT IN ('Spouse', 'Child', 'Parent', 'Sibling', 'Friend') THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid relationship. Allowed values: Spouse, Child, Parent, Sibling, Friend.';
    END IF;

    -- Check existing beneficiaries for this account
    SELECT COUNT(*) INTO nominee_count
    FROM beneficiary
    WHERE account_id = p_account_id;

    IF nominee_count >= 2 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Maximum of 2 beneficiaries allowed per account.';
    END IF;

    -- Insert new beneficiary
    INSERT INTO beneficiary (account_id, name, relationship, age)
    VALUES (p_account_id, p_name, p_relationship, p_age);

END
```
3. *block_accounts_on_high_activity*
 ```sql
BEGIN
    DECLARE threshold INT DEFAULT 10;
    DECLARE interval_hours INT DEFAULT 1;

    UPDATE accounts a
    SET a.status = 'BLOCKED'
    WHERE a.status != 'BLOCKED' -- only block accounts not already blocked
      AND (
          SELECT COUNT(*)
          FROM transactions t
          WHERE (t.from_account_id = a.id OR t.to_account_id = a.id)
            AND t.completed_at >= NOW() - INTERVAL interval_hours HOUR
      ) > threshold;
END
```
**EVENTS** 

1. *block_high_activity_accounts_event* (RECURRING EXECUTE ONCE EVERY HOUR)
```sql
CALL block_accounts_on_high_activity()
```
3. *daily_inactive_accounts_update* (RECURRING EXECUTE ONCE EVERY DAY)
```sql
CALL MarkInactiveAccountsBasedOnTransactions()
```
