<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- Added fmt taglib for formatting --%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Transaction History - NetBank</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet" />
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
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

        .page-header {
            color: #2d3748;
            font-weight: 600;
            margin-bottom: 2rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .table-custom {
            background: white;
            border-radius: 15px;
            overflow: hidden; /* Ensures rounded corners on table */
            box-shadow: var(--card-shadow);
        }

        .table-custom thead {
            background: var(--primary-gradient);
            color: white;
        }

        .table-custom th {
            padding: 1rem 1.25rem;
            border-bottom: none;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.9rem;
        }

        .table-custom tbody tr {
            transition: background-color 0.2s ease;
        }

        .table-custom tbody tr:hover {
            background-color: #f0f4f8; /* Lighter hover for table rows */
        }

        .table-custom td {
            padding: 1rem 1.25rem;
            vertical-align: middle;
            border-top: 1px solid #e2e8f0;
        }

        /* Status badges */
        .status-completed {
            color: #28a745; /* Bootstrap green */
            font-weight: 500;
        }
        .status-pending {
            color: #ffc107; /* Bootstrap yellow */
            font-weight: 500;
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

        .btn-back {
            background: #cbd5e0;
            color: #2d3748;
            border: none;
        }
        .btn-back:hover {
            background: #aeb9c4;
            color: #2d3748;
        }

        .text-small {
            font-size: 0.8rem;
            color: #6c757d;
            word-break: break-all; /* Ensure long hashes wrap */
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/dashboard"> <%-- Fixed with context path --%>
                <i class="fas fa-university me-2"></i>NetBank
            </a>
            <div class="navbar-nav ms-auto">
                <span class="navbar-text me-3">
                    <i class="fas fa-user-circle me-1"></i>Welcome, <c:out value="${user.fullName}" />!
                </span>
                <a class="nav-link btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout">
                    <i class="fas fa-sign-out-alt me-1"></i>Logout
                </a>
            </div>
        </div>
    </nav>

    <div class="container main-content">
        <h2 class="page-header"><i class="fas fa-history text-primary"></i>Transaction History</h2>

        <div class="table-responsive">
            <table class="table table-bordered table-hover table-custom">
                <thead class="table-dark">
                    <tr>
                        <th>Date</th>
                        <th>Type</th>
                        <th>From Account</th>
                        <th>To Account</th>
                        <th>Amount</th>
                        <th>Note</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty transactions}">
                            <c:forEach var="tx" items="${transactions}">
                                <tr>
                                    <td><fmt:formatDate value="${tx.completedAt}" pattern="dd-MM-yyyy HH:mm:ss" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty tx.transactionType.displayName}">
                                                <c:out value="${tx.transactionType.displayName}" />
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${tx.transactionType}" />
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty tx.fromAccount}">
                                                <c:out value="${tx.fromAccount.accountNumber}" />
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty tx.toAccount}">
                                                <c:out value="${tx.toAccount.accountNumber}" />
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>₹<fmt:formatNumber value="${tx.amount}" pattern="#,##0.00" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty tx.referenceNote}">
                                                <c:out value="${tx.referenceNote}" />
                                            </c:when>
                                            <c:otherwise>-</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:out value="${tx.transactionStatus.displayName}" />
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="8" class="text-center py-4">No transactions found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <div class="d-flex justify-content-end mt-4">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-back btn-custom">
                <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
