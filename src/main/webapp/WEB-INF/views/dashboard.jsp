<!-- Banking Dashboard - /WEB-INF/jsp/banking/dashboard.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Banking Dashboard - NetBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-gradient: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            --secondary-gradient: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            --success-gradient: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            --warning-gradient: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
            --card-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            --hover-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
        }
        
        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
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
        
        .dashboard-header {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: var(--card-shadow);
            border-left: 5px solid #667eea;
        }
        
        .dashboard-header h2 {
            color: #2d3748;
            font-weight: 600;
            margin: 0;
        }
        
        .account-card {
            background: white;
            border-radius: 15px;
            overflow: hidden;
            box-shadow: var(--card-shadow);
            transition: all 0.3s ease;
            border: none;
            height: 100%;
        }
        
        .account-card:hover {
            transform: translateY(-5px);
            box-shadow: var(--hover-shadow);
        }
        
        .account-card .card-header {
            background: var(--primary-gradient);
            border: none;
            padding: 1.25rem;
        }
        
        .account-card .card-header h6 {
            color: white;
            font-weight: 600;
            margin: 0;
        }
        
        .balance-amount {
            font-size: 1.5rem;
            font-weight: 700;
            background: var(--success-gradient);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }
        
        .quick-action-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            text-align: center;
            box-shadow: var(--card-shadow);
            transition: all 0.3s ease;
            border: none;
            height: 100%;
        }
        
        .quick-action-card:hover {
            transform: translateY(-5px);
            box-shadow: var(--hover-shadow);
        }
        
        .action-icon {
            width: 70px;
            height: 70px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1rem;
            font-size: 1.5rem;
            color: white;
        }
        
        .action-icon.transfer { background: var(--primary-gradient); }
        .action-icon.history { background: var(--secondary-gradient); }
        .action-icon.beneficiary { background: var(--success-gradient); }
        .action-icon.account { background: var(--warning-gradient); }
        
        .btn-custom {
            border-radius: 25px;
            padding: 0.5rem 1.5rem;
            font-weight: 500;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            transition: all 0.3s ease;
        }
        
        .btn-custom:hover {
            transform: translateY(-2px);
        }
        
        .welcome-section {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            box-shadow: var(--card-shadow);
        }
        
        .welcome-text {
            color: #4a5568;
            font-size: 1.1rem;
        }
        
        .stats-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: var(--card-shadow);
            border-left: 4px solid;
        }
        
        .status-badge {
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-weight: 500;
            font-size: 0.85rem;
        }
        
        .account-number {
            font-family: 'Courier New', monospace;
            background: #f7fafc;
            padding: 0.25rem 0.5rem;
            border-radius: 5px;
            font-weight: 600;
        }
        
        .section-title {
            color: #2d3748;
            font-weight: 600;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
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

    <div class="container mt-4">
        <!-- Welcome Section -->
        <div class="welcome-section">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h4 class="gradient-text mb-2">Good ${greeting}, ${user.firstName} ${user.lastName}!</h4>
                    <p class="welcome-text mb-0">Welcome to your personal banking dashboard. Manage your accounts, transfer funds, and track your transactions.</p>
                </div>
                <div class="col-md-4 text-end">
                    <i class="fas fa-chart-line fa-3x text-primary opacity-25"></i>
                </div>
            </div>
        </div>

        <!-- Dashboard Header -->
        <div class="dashboard-header">
            <h2><i class="fas fa-tachometer-alt me-2"></i>Banking Dashboard</h2>
        </div>

        <!-- Account Summary Cards -->
        <div class="row mb-5">
            <div class="col-12">
                <h4 class="section-title">
                    <i class="fas fa-credit-card"></i>
                    Your Accounts
                </h4>
            </div>
            <c:choose>
                <c:when test="${not empty accounts}">
                    <c:forEach var="account" items="${accounts}" varStatus="status">
                        <div class="col-lg-4 col-md-6 mb-4">
                            <div class="account-card">
                                <div class="card-header">
                                    <h6><i class="fas fa-credit-card me-2"></i>${account.accountType.displayName}</h6>
                                </div>
                                <div class="card-body">
                                    <div class="mb-3">
                                        <small class="text-muted">Account Number</small>
                                        <div class="account-number">${account.accountNumber}</div>
                                    </div>
                                    <div class="mb-3">
                                        <small class="text-muted">Current Balance</small>
                                        <div class="balance-amount">₹<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/></div>
                                    </div>
                                    <div class="mb-3">
                                        <span class="status-badge bg-success text-white">${account.status.displayName}</span>
                                    </div>
                                    <a href="/banking/accounts/${account.id}" class="btn btn-primary btn-custom w-100">
                                        <i class="fas fa-eye me-1"></i>View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="text-center py-5">
                            <i class="fas fa-credit-card fa-4x text-muted mb-3"></i>
                            <h4 class="text-muted">No Accounts Found</h4>
                            <p class="text-muted">Create your first account to get started with banking.</p>
                            <a href="create" class="btn btn-primary btn-custom">
                                <i class="fas fa-plus me-1"></i>Create Account
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Quick Actions -->
        <div class="row">
            <div class="col-12">
                <h4 class="section-title">
                    <i class="fas fa-bolt"></i>
                    Quick Actions
                </h4>
            </div>
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="quick-action-card">
                    <div class="action-icon transfer">
                        <i class="fas fa-exchange-alt"></i>
                    </div>
                    <h5 class="mb-2">Fund Transfer</h5>
                    <p class="text-muted mb-3">Transfer money between accounts or to beneficiaries</p>
                    <a href="/banking/transactions/transfer/new" class="btn btn-primary btn-custom">
                        <i class="fas fa-arrow-right me-1"></i>Transfer Now
                    </a>
                </div>
            </div>
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="quick-action-card">
                    <div class="action-icon history">
                        <i class="fas fa-history"></i>
                    </div>
                    <h5 class="mb-2">Transaction History</h5>
                    <p class="text-muted mb-3">View your recent transactions and account activity</p>
                    <a href="/banking/transactions/transfer/history" class="btn btn-info btn-custom">
                        <i class="fas fa-search me-1"></i>View History
                    </a>
                </div>
            </div>
            <div class="row">
                <c:forEach var="account" items="${accounts}">
                    <div class="col-lg-3 col-md-6 mb-4">
                        <div class="quick-action-card">
                            <div class="action-icon beneficiary">
                                <i class="fas fa-users"></i>
                            </div>
                            <h5 class="mb-2">Beneficiaries</h5>
                            <p class="text-muted mb-3">
                                Manage saved beneficiaries for <strong>${account.accountType}</strong> account <strong>${account.accountNumber}</strong>
                            </p>
                            <a href="${pageContext.request.contextPath}/beneficiaries/manage?accountId=${account.id}" class="btn btn-success btn-custom">
                                <i class="fas fa-cog me-1"></i>Manage
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="quick-action-card">
                    <div class="action-icon account">
                        <i class="fas fa-plus-circle"></i>
                    </div>
                    <h5 class="mb-2">New Account</h5>
                    <p class="text-muted mb-3">Open a new savings or current account</p>
                    <a href="create" class="btn btn-warning btn-custom">
                        <i class="fas fa-plus me-1"></i>Create Now
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Add some interactive effects
        document.addEventListener('DOMContentLoaded', function() {
            // Animate cards on scroll
            const cards = document.querySelectorAll('.account-card, .quick-action-card');
            
            const observer = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.style.opacity = '1';
                        entry.target.style.transform = 'translateY(0)';
                    }
                });
            });

            cards.forEach(card => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                card.style.transition = 'all 0.6s ease';
                observer.observe(card);
            });
        });
    </script>
</body>
</html>