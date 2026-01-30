<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../../common/header.jsp">
    <jsp:param name="title" value="Quản lý người dùng"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="../../common/alerts.jsp"/>
        
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="h3 mb-0">
                <i class="bi bi-people-fill me-2"></i>Quản lý người dùng
            </h1>
            <a href="${pageContext.request.contextPath}/admin/users/create" class="btn btn-primary">
                <i class="bi bi-person-plus me-1"></i>Thêm người dùng
            </a>
        </div>
        
        <!-- Search & Filter -->
        <div class="card mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/users" method="get" class="row g-3">
                    <div class="col-md-5">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="text" class="form-control" name="keyword" 
                                   value="${keyword}" placeholder="Tìm theo tên hoặc email...">
                        </div>
                    </div>
                    <div class="col-md-3">
                        <select class="form-select" name="status">
                            <option value="">-- Trạng thái --</option>
                            <option value="active" ${status == 'active' ? 'selected' : ''}>Hoạt động</option>
                            <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Không hoạt động</option>
                            <option value="banned" ${status == 'banned' ? 'selected' : ''}>Đã cấm</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <select class="form-select" name="roleId">
                            <option value="">-- Vai trò --</option>
                            <c:forEach var="role" items="${roles}">
                                <option value="${role.id}" ${role.id == selectedRoleId ? 'selected' : ''}>
                                    ${role.displayName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-search me-1"></i>Tìm
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <!-- Users Table -->
        <div class="card">
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>Người dùng</th>
                                <th>Email</th>
                                <th>Vai trò</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                                <th class="text-center">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>
                                        <div class="d-flex align-items-center">
                                            <div class="avatar-sm bg-primary bg-opacity-10 rounded-circle me-2 d-flex align-items-center justify-content-center"
                                                 style="width: 40px; height: 40px;">
                                                <c:choose>
                                                    <c:when test="${not empty user.avatarUrl}">
                                                        <img src="${user.avatarUrl}" alt="${user.fullName}" 
                                                             class="rounded-circle" width="40" height="40">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-primary fw-bold">
                                                            ${user.fullName.substring(0, 1).toUpperCase()}
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div>
                                                <a href="${pageContext.request.contextPath}/admin/users/${user.id}" 
                                                   class="text-decoration-none fw-medium">
                                                    ${user.fullName}
                                                </a>
                                                <c:if test="${not empty user.studentId}">
                                                    <small class="text-muted d-block">${user.studentId}</small>
                                                </c:if>
                                            </div>
                                        </div>
                                    </td>
                                    <td>${user.email}</td>
                                    <td>
                                        <c:forEach var="role" items="${user.roles}" varStatus="status">
                                            <span class="badge bg-${role.name == 'admin' ? 'danger' : 
                                                                   role.name == 'moderator' ? 'warning' : 'info'}">
                                                ${role.displayName}
                                            </span>
                                            <c:if test="${!status.last}"> </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <span class="badge bg-${user.status == 'active' ? 'success' : 
                                                              user.status == 'inactive' ? 'secondary' : 'danger'}">
                                            ${user.statusDisplayName}
                                        </span>
                                    </td>
                                    <td>
                                        <small>${user.createdAt.dayOfMonth}/${user.createdAt.monthValue}/${user.createdAt.year}</small>
                                    </td>
                                    <td class="text-center">
                                        <div class="btn-group btn-group-sm">
                                            <a href="${pageContext.request.contextPath}/admin/users/${user.id}" 
                                               class="btn btn-outline-primary" title="Xem chi tiết">
                                                <i class="bi bi-eye"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/users/${user.id}/edit" 
                                               class="btn btn-outline-secondary" title="Chỉnh sửa">
                                                <i class="bi bi-pencil"></i>
                                            </a>
                                            <c:if test="${user.status == 'active'}">
                                                <button type="button" class="btn btn-outline-danger" title="Cấm"
                                                        onclick="confirmBan(${user.id}, '${user.fullName}')">
                                                    <i class="bi bi-slash-circle"></i>
                                                </button>
                                            </c:if>
                                            <c:if test="${user.status == 'banned'}">
                                                <button type="button" class="btn btn-outline-success" title="Bỏ cấm"
                                                        onclick="confirmUnban(${user.id}, '${user.fullName}')">
                                                    <i class="bi bi-check-circle"></i>
                                                </button>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            
                            <c:if test="${empty users}">
                                <tr>
                                    <td colspan="7" class="text-center py-5">
                                        <i class="bi bi-person-x display-4 text-muted"></i>
                                        <p class="mt-3 mb-0 text-muted">Không tìm thấy người dùng nào</p>
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Ban Modal -->
<div class="modal fade" id="banModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Cấm người dùng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form id="banForm" method="post">
                <div class="modal-body">
                    <p>Bạn có chắc chắn muốn cấm người dùng <strong id="banUserName"></strong>?</p>
                    <p class="text-muted small">Người dùng bị cấm sẽ không thể đăng nhập vào hệ thống.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">
                        <i class="bi bi-slash-circle me-1"></i>Cấm
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Unban Modal -->
<div class="modal fade" id="unbanModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Bỏ cấm người dùng</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form id="unbanForm" method="post">
                <div class="modal-body">
                    <p>Bạn có chắc chắn muốn bỏ cấm người dùng <strong id="unbanUserName"></strong>?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-check-circle me-1"></i>Bỏ cấm
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function confirmBan(userId, userName) {
    document.getElementById('banUserName').textContent = userName;
    document.getElementById('banForm').action = '${pageContext.request.contextPath}/admin/users/' + userId + '/ban';
    new bootstrap.Modal(document.getElementById('banModal')).show();
}

function confirmUnban(userId, userName) {
    document.getElementById('unbanUserName').textContent = userName;
    document.getElementById('unbanForm').action = '${pageContext.request.contextPath}/admin/users/' + userId + '/unban';
    new bootstrap.Modal(document.getElementById('unbanModal')).show();
}
</script>

<jsp:include page="../../common/footer.jsp"/>
