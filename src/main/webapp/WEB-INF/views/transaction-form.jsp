<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create New Transaction - NetBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --secondary-gradient: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            --success-gradient: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            --warning-gradient: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            --danger-gradient: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%);
            --card-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            --hover-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
        }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        .navbar {
            background: var(--primary-gradient) !important;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            backdrop-filter: blur(10px);
        }

        .navbar-brand {
            font-weight: 700;
            font-size: 1.5rem;
        }

        .main-content {
            flex-grow: 1;
            padding-top: 2rem;
            padding-bottom: 2rem;
        }

        .form-card {
            background: white;
            border-radius: 15px;
            padding: 2.5rem;
            box-shadow: var(--card-shadow);
            border-left: 5px solid #667eea;
        }

        .form-card h2 {
            color: #2d3748;
            font-weight: 600;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .form-label {
            font-weight: 500;
            color: #4a5568;
            margin-bottom: 0.5rem;
        }

        /* Styling for Spring Form tags */
        .form-control,
        .form-select,
        .form-control[type="number"],
        .form-control[type="text"],
        textarea.form-control {
            border-radius: 10px;
            padding: 0.75rem 1rem;
            border: 1px solid #e2e8f0;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
            width: 100%; /* Ensure full width */
            box-sizing: border-box; /* Include padding/border in width */
        }

        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.25rem rgba(102, 126, 234, 0.25);
            outline: none;
        }

        .btn-custom {
            border-radius: 25px;
            padding: 0.75rem 2rem;
            font-weight: 600;
            font-size: 1.05rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            transition: all 0.3s ease;
            box-shadow: var(--card-shadow);
        }

        .btn-custom:hover {
            transform: translateY(-2px);
            box-shadow: var(--hover-shadow);
        }

        .btn-primary-gradient {
            background: var(--primary-gradient);
            color: white;
            border: none;
        }
        .btn-primary-gradient:hover {
            background: var(--primary-gradient);
            opacity: 0.9;
            color: white;
        }

        .btn-back {
            background: #cbd5e0;
            color: #2d3748;
            border: none;
        }
        .btn-back:hover {
            background: #aeb9c4;
            color: #2d3748;
        }

        .alert-container {
            margin-top: 1.5rem;
        }

        .alert-success {
            background-color: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
            border-radius: 10px;
        }

        .alert-danger {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
            border-radius: 10px;
        }

        .gradient-text {
            background: var(--primary-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="dashboard">
                <i class="fas fa-university me-2"></i>NetBank
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">
                    <i class="fas fa-user-circle me-1"></i>Welcome, ${user.fullName}!
                </span>
                <a class="nav-link btn btn-outline-light btn-sm" href="/logout">
                    <i class="fas fa-sign-out-alt me-1"></i>Logout
                </a>
            </div>
        </div>
    </nav>

    <div class="container main-content">
        <div class="row justify-content-center">
            <div class="col-lg-8 col-md-10">
                <div class="form-card">
                    <h2><i class="fas fa-exchange-alt text-primary"></i>Create New Transaction</h2>
                    <p class="text-muted mb-4">Initiate a new money transfer between your accounts.</p>

                    <c:if test="${not empty successMessage}">
                        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                            <i class="fas fa-check-circle me-2"></i> ${successMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                            <i class="fas fa-exclamation-triangle me-2"></i> ${errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>
                    <form:form modelAttribute="transaction" action="${pageContext.request.contextPath}/banking/transactions/transfer/submit" method="post">
    <div class="mb-4">
        <label for="transactionType" class="form-label">Transaction Type:</label>
        <form:select path="transactionType" id="transactionType" class="form-select" required="required">
            <form:option value="" label="-- Select Type --" />
            <c:forEach items="${transactionTypes}" var="type">
                <form:option value="${type}">
                    <c:choose>
                        <c:when test="${type.displayName != null}">
                            <c:out value="${type.displayName}" />
                        </c:when>
                        <c:otherwise>
                            <c:out value="${type}" />
                        </c:otherwise>
                    </c:choose>
                </form:option>
            </c:forEach>
        </form:select>
        <div class="form-text text-muted">Choose the type of transaction (e.g., Transfer, Deposit, Withdrawal).</div>
    </div>

    <div class="mb-4">
        <label for="fromAccount" class="form-label">From Account:</label>
        <form:select path="fromAccount.id" id="fromAccount" class="form-select" required="required">
            <form:option value="" label="-- Select Account --" />
            <c:forEach items="${accounts}" var="account">
                <form:option value="${account.id}">
                    ${account.accountNumber} (
                    <c:choose>
                        <c:when test="${account.accountType.displayName != null}">
                            <c:out value="${account.accountType.displayName}" />
                        </c:when>
                        <c:otherwise>
                            <c:out value="${account.accountType}" />
                        </c:otherwise>
                    </c:choose>
                    ) - Balance: ₹<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/>
                </form:option>
            </c:forEach>
        </form:select>
        <div class="form-text text-muted">Select the account from which funds will be debited.</div>
    </div>

    <div class="mb-4">
        <label for="toAccount" class="form-label">To Account:</label>
        <form:select path="toAccount.id" id="toAccount" class="form-select" required="required">
            <form:option value="" label="-- Select Account --" />
            <c:forEach items="${accounts}" var="account">
                <form:option value="${account.id}">
                    ${account.accountNumber} (
                    <c:choose>
                        <c:when test="${account.accountType.displayName != null}">
                            <c:out value="${account.accountType.displayName}" />
                        </c:when>
                        <c:otherwise>
                            <c:out value="${account.accountType}" />
                        </c:otherwise>
                    </c:choose>
                    ) - Balance: ₹<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/>
                </form:option>
            </c:forEach>
        </form:select>
        <div class="form-text text-muted">Select the account where funds will be credited.</div>
    </div>

    <div class="mb-4">
        <label for="amount" class="form-label">Amount:</label>
        <form:input path="amount" id="amount" type="number" step="0.01" min="0.01" class="form-control" placeholder="e.g., 100.00" required="required"/>
        <div class="form-text text-muted">Enter the amount for the transaction. Minimum is ₹0.01.</div>
    </div>

    <div class="mb-4">
        <label for="referenceNote" class="form-label">Reference Note:</label>
        <form:textarea path="referenceNote" id="referenceNote" rows="3" class="form-control" placeholder="Optional: Add a note for this transaction."></form:textarea>
        <div class="form-text text-muted">Provide a brief description or reference for your records.</div>
    </div>

    <div class="d-grid gap-3 d-md-flex justify-content-md-end">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-back btn-custom">
            <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
        </a>
        <button type="submit" class="btn btn-primary-gradient btn-custom">
            <i class="fas fa-paper-plane me-2"></i>Submit Transaction
        </button>
    </div>
</form:form>

                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>