<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account Details - NetBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --secondary-gradient: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            --success-gradient: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            --warning-gradient: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            --danger-gradient: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%); /* New gradient for danger */
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

        .details-card {
            background: white;
            border-radius: 15px;
            padding: 2.5rem;
            box-shadow: var(--card-shadow);
            border-left: 5px solid #667eea;
        }

        .details-card h2 {
            color: #2d3748;
            font-weight: 600;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .details-item {
            display: flex;
            justify-content: space-between;
            padding: 0.75rem 0;
            border-bottom: 1px dashed #e2e8f0;
        }

        .details-item:last-child {
            border-bottom: none;
        }

        .details-label {
            font-weight: 500;
            color: #4a5568;
            flex-basis: 40%; /* Adjust as needed */
        }

        .details-value {
            color: #2d3748;
            font-weight: 600;
            text-align: right;
            flex-basis: 60%; /* Adjust as needed */
        }

        .account-number-display {
            font-family: 'Courier New', monospace;
            background: #f7fafc;
            padding: 0.25rem 0.5rem;
            border-radius: 5px;
            font-weight: 600;
        }

        .balance-amount-display {
            font-size: 1.25rem;
            font-weight: 700;
            background: var(--success-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .section-title {
            color: #2d3748;
            font-weight: 600;
            margin-top: 2rem;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
            border-bottom: 2px solid #e2e8f0;
            padding-bottom: 0.75rem;
        }

        .form-label {
            font-weight: 500;
            color: #4a5568;
            margin-bottom: 0.5rem;
        }

        .form-control, .form-select {
            border-radius: 10px;
            padding: 0.75rem 1rem;
            border: 1px solid #e2e8f0;
            box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }

        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.25rem rgba(102, 126, 234, 0.25);
            outline: none;
        }

        .btn-custom {
            border-radius: 25px;
            padding: 0.75rem 2rem; /* Adjusted padding for larger buttons */
            font-weight: 600; /* Increased font weight */
            font-size: 1.05rem; /* Slightly larger font */
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
            background: var(--primary-gradient); /* Ensure gradient stays on hover */
            opacity: 0.9;
            color: white;
        }

        .btn-danger-gradient {
            background: var(--danger-gradient);
            color: white;
            border: none;
        }
        .btn-danger-gradient:hover {
            background: var(--danger-gradient); /* Ensure gradient stays on hover */
            opacity: 0.9;
            color: white;
        }

        .btn-back {
            background: #cbd5e0; /* Light gray for back button */
            color: #2d3748;
            border: none;
        }
        .btn-back:hover {
            background: #aeb9c4; /* Slightly darker gray on hover */
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
                <div class="details-card">
                    <h2><i class="fas fa-edit text-primary"></i>Manage Account Details</h2>
                    <p class="text-muted mb-4">View and update the details for account **${account.accountNumber}**.</p>

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

                    <h4 class="section-title"><i class="fas fa-info-circle"></i>Account Information</h4>
                    <div class="mb-4">
                        <div class="details-item">
                            <span class="details-label">Account Number:</span>
                            <span class="details-value account-number-display">${account.accountNumber}</span>
                        </div>
                        <div class="details-item">
                            <span class="details-label">Current Balance:</span>
                            <span class="details-value balance-amount-display">₹<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/></span>
                        </div>
                        <div class="details-item">
                            <span class="details-label">Account Owner:</span>
                            <span class="details-value">${account.user.fullName}</span>
                        </div>
                        <div class="details-item">
                            <span class="details-label">User ID:</span>
                            <span class="details-value">${account.user.id}</span>
                        </div>
                    </div>

                    <h4 class="section-title"><i class="fas fa-cogs"></i>Update Account Settings</h4>
                    <form method="post" action="/banking/accounts/${account.id}/update">
                        <div class="mb-3">
                            <label for="accountType" class="form-label">Account Type</label>
                            <select class="form-select" id="accountType" name="accountType" required>
                                <option value="" disabled>Select account type</option>
                                <c:forEach var="type" items="${types}">
                                    <option value="${type}" ${type == account.accountType ? 'selected' : ''}>${type.displayName}</option>
                                </c:forEach>
                            </select>
                            <div class="form-text text-muted">Select the desired account type.</div>
                        </div>
                        <div class="mb-4">
                        <label for="status" class="form-label">Account Status</label>
                        <select class="form-select" id="status" name="status" disabled>
                            <option value="" disabled>Select account status</option>
                            <c:forEach var="statusOption" items="${statuses}">
                                <option value="${statusOption}" ${statusOption == account.status ? 'selected' : ''}>${statusOption.displayName}</option>
                            </c:forEach>
                        </select>
                        <input type="hidden" name="status" value="${account.status}" />
                        <div class="form-text text-muted">Account status cannot be changed by the user.</div>
                    </div>
                        <div class="d-grid d-md-flex justify-content-md-end">
                            <button type="submit" class="btn btn-primary-gradient btn-custom">
                                <i class="fas fa-sync-alt me-2"></i>Update Account
                            </button>
                        </div>
                    </form>

                    <h4 class="section-title mt-5"><i class="fas fa-trash-alt"></i>Account Actions</h4>
                    <p class="text-muted mb-4">Use this button to permanently delete this account. This action cannot be undone.</p>
                    <form method="post" action="/banking/accounts/${account.id}/delete" onsubmit="return confirm('Are you sure you want to permanently delete this account? This action cannot be undone.');">
                        <div class="d-grid">
                            <button type="submit" class="btn btn-danger-gradient btn-custom">
                                <i class="fas fa-exclamation-triangle me-2"></i>Delete Account
                            </button>
                        </div>
                    </form>

                    <div class="d-grid d-md-flex justify-content-md-start mt-4">
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-back btn-custom">
                            <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>