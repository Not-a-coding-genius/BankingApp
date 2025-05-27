<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Beneficiaries - NetBank</title>
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

        .page-header {
            color: #2d3748;
            font-weight: 600;
            margin-bottom: 2rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
        }

        .form-card {
            background: white;
            border-radius: 15px;
            padding: 2.5rem;
            box-shadow: var(--card-shadow);
            border-left: 5px solid #667eea;
        }

        .form-card h3 { /* Changed from h2 for sub-header */
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
            width: 100%;
            box-sizing: border-box;
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

        .btn-info-outline {
            color: #2d3748;
            border: 1px solid #aeb9c4;
            background-color: transparent;
        }
        .btn-info-outline:hover {
            background-color: #e2e8f0;
            color: #2d3748;
        }

        .btn-danger-outline {
            color: #dc3545;
            border: 1px solid #dc3545;
            background-color: transparent;
        }
        .btn-danger-outline:hover {
            background-color: #dc3545;
            color: white;
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
                    <i class="fas fa-user-circle me-1"></i>Welcome, ${user.firstName} ${user.lastName}!
                </span>
                <a class="nav-link btn btn-outline-light btn-sm" href="/logout">
                    <i class="fas fa-sign-out-alt me-1"></i>Logout
                </a>
            </div>
        </div>
    </nav>

    <div class="container main-content">
        <h2 class="page-header"><i class="fas fa-users-cog text-primary"></i>Manage Beneficiaries</h2>
        <p class="text-muted mb-4">View, edit, or delete your registered beneficiaries.</p>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
                <i class="fas fa-check-circle me-2"></i> ${successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMessage}"> <%-- Assuming 'error' from controller is mapped to 'errorMessage' or change here to 'error' --%>
            <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i> ${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="table-responsive mb-5">
            <table class="table table-bordered table-hover table-custom">
                <thead class="table-dark">
                    <tr>
                        <th>Name</th>
                        <th>Relationship</th>
                        <th>Age</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty beneficiaries}">
                            <c:forEach var="b" items="${beneficiaries}">
                                <tr>
                                    <td><c:out value="${b.name}" /></td>
                                    <td><c:out value="${b.relationship != null && b.relationship != '' ? b.relationship : '-'}" /></td>
                                    <td><c:out value="${b.age != null ? b.age : '-'}" /></td>
                                    <td class="text-nowrap"> <%-- Prevents actions from wrapping --%>
                                        <a href="${pageContext.request.contextPath}/beneficiaries/edit?id=${b.id}" class="btn btn-sm btn-info-outline me-2">
                                            <i class="fas fa-edit me-1"></i>Edit
                                        </a>
                                        <a href="${pageContext.request.contextPath}/beneficiaries/delete?id=${b.id}&accountId=${b.accountId}"
                                           class="btn btn-sm btn-danger-outline"
                                           onclick="return confirm('Are you sure you want to delete ${b.name} from your beneficiaries?');">
                                            <i class="fas fa-trash-alt me-1"></i>Delete
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-center py-4">No beneficiaries added yet.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <div class="row justify-content-center mt-4">
            <div class="col-lg-8 col-md-10">
                <div class="form-card">
                    <h3><i class="fas fa-user-plus text-primary"></i>Add New Beneficiary</h3>
                    <p class="text-muted mb-4">Quickly add another beneficiary to your list.</p>
                    <form action="${pageContext.request.contextPath}/beneficiaries/add" method="post">
                        <input type="hidden" name="accountId" value="${accountId}" />

                        <div class="mb-3">
                            <label for="name" class="form-label">Beneficiary Name:</label>
                            <input type="text" id="name" name="name" class="form-control" placeholder="e.g., Jane Smith" required />
                        </div>
                        <div class="mb-3">
                            <label for="relationship" class="form-label">Relationship:</label>
                            <select id="relationship" name="relationship" class="form-select" required>
                                <option value="" disabled selected>Select relationship</option>
                                <option value="Spouse">Spouse</option>
                                <option value="Child">Child</option>
                                <option value="Parent">Parent</option>
                                <option value="Sibling">Sibling</option>
                                <option value="Friend">Friend</option>
                                <!-- Add any other allowed relationships -->
                            </select>
                        </div>
                        <div class="mb-4">
                            <label for="age" class="form-label">Age:</label>
                            <input type="number" id="age" name="age" class="form-control" required min="20" max="85" placeholder="e.g., 45" />
                        </div>

                        <div class="d-grid mt-4">
                            <button type="submit" class="btn btn-primary-gradient btn-custom">
                                <i class="fas fa-plus-circle me-2"></i>Add Beneficiary
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="d-flex justify-content-end mt-5">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-back btn-custom">
                <i class="fas fa-arrow-left me-2"></i>Back to Dashboard
            </a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>