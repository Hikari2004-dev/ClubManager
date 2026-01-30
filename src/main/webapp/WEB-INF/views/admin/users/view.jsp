<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../../common/header.jsp">
    <jsp:param name="title" value="${user.fullName}"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="../../common/alerts.jsp"/>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="${pageContext.request.contextPath}/admin/users">Quản lý người dùng</a>
                </li>
                <li class="breadcrumb-item active">${user.fullName}</li>
            </ol>
        </nav>
        
        <div class="row">
            <!-- User Info Card -->
            <div class="col-lg-4">
                <div class="card mb-4">
                    <div class="card-body text-center">
                        <c:choose>
                            <c:when test="${not empty user.avatarUrl}">
                                <img src="${user.avatarUrl}" alt="${user.fullName}" 
                                     class="rounded-circle mb-3" width="120" height="120">
                            </c:when>
                            <c:otherwise>
                                <div class="avatar-lg bg-primary bg-opacity-10 rounded-circle mx-auto mb-3 d-flex align-items-center justify-content-center"
                                     style="width: 120px; height: 120px;">
                                    <span class="text-primary display-4 fw-bold">
                                        ${user.fullName.substring(0, 1).toUpperCase()}
                                    </span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                        
                        <h4 class="mb-1">${user.fullName}</h4>
                        <p class="text-muted mb-3">${user.email}</p>
                        
                        <div class="mb-3">
                            <c:forEach var="role" items="${user.roles}">
                                <span class="badge bg-${role.name == 'admin' ? 'danger' : 
                                                       role.name == 'moderator' ? 'warning' : 'info'} me-1">
                                    ${role.displayName}
                                </span>
                            </c:forEach>
                        </div>
                        
                        <span class="badge bg-${user.status == 'active' ? 'success' : 
                                              user.status == 'inactive' ? 'secondary' : 'danger'} fs-6">
                            ${user.statusDisplayName}
                        </span>
                    </div>
                </div>
                
                <!-- Actions -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-gear me-2"></i>Thao tác</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/admin/users/${user.id}/edit" 
                               class="btn btn-outline-primary">
                                <i class="bi bi-pencil me-1"></i>Chỉnh sửa
                            </a>
                            <c:if test="${user.status == 'active'}">
                                <form action="${pageContext.request.contextPath}/admin/users/${user.id}/ban" method="post">
                                    <button type="submit" class="btn btn-outline-danger w-100"
                                            onclick="return confirm('Bạn có chắc muốn cấm người dùng này?')">
                                        <i class="bi bi-slash-circle me-1"></i>Cấm người dùng
                                    </button>
                                </form>
                            </c:if>
                            <c:if test="${user.status == 'banned'}">
                                <form action="${pageContext.request.contextPath}/admin/users/${user.id}/unban" method="post">
                                    <button type="submit" class="btn btn-outline-success w-100">
                                        <i class="bi bi-check-circle me-1"></i>Bỏ cấm
                                    </button>
                                </form>
                            </c:if>
                            <button type="button" class="btn btn-outline-secondary"
                                    data-bs-toggle="modal" data-bs-target="#resetPasswordModal">
                                <i class="bi bi-key me-1"></i>Đặt lại mật khẩu
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Main Content -->
            <div class="col-lg-8">
                <!-- User Details -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-person me-2"></i>Thông tin chi tiết</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted">ID người dùng</label>
                                <p class="mb-0 fw-medium">${user.id}</p>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted">Mã sinh viên</label>
                                <p class="mb-0 fw-medium">${empty user.studentId ? 'Chưa cập nhật' : user.studentId}</p>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted">Họ và tên</label>
                                <p class="mb-0 fw-medium">${user.fullName}</p>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted">Email</label>
                                <p class="mb-0 fw-medium">${user.email}</p>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted">Số điện thoại</label>
                                <p class="mb-0 fw-medium">${empty user.phone ? 'Chưa cập nhật' : user.phone}</p>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label text-muted">Ngày tạo</label>
                                <p class="mb-0 fw-medium">
                                    ${user.createdAt.dayOfMonth}/${user.createdAt.monthValue}/${user.createdAt.year}
                                    ${user.createdAt.hour}:${user.createdAt.minute < 10 ? '0' : ''}${user.createdAt.minute}
                                </p>
                            </div>
                            <div class="col-12">
                                <label class="form-label text-muted">Tiểu sử</label>
                                <p class="mb-0">${empty user.bio ? 'Chưa cập nhật' : user.bio}</p>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Club Memberships -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="bi bi-people me-2"></i>Câu lạc bộ tham gia
                            <span class="badge bg-primary">${memberships.size()}</span>
                        </h5>
                    </div>
                    <div class="card-body p-0">
                        <c:choose>
                            <c:when test="${not empty memberships}">
                                <div class="table-responsive">
                                    <table class="table table-hover mb-0">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Câu lạc bộ</th>
                                                <th>Vai trò</th>
                                                <th>Trạng thái</th>
                                                <th>Ngày tham gia</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="membership" items="${memberships}">
                                                <tr>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/clubs/${membership.clubId}" 
                                                           class="text-decoration-none">
                                                            ${membership.club.name}
                                                        </a>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-${membership.role == 'president' ? 'danger' : 
                                                                              membership.role == 'vice_president' ? 'warning' : 
                                                                              membership.role == 'secretary' ? 'info' : 'secondary'}">
                                                            ${membership.roleDisplayName}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-${membership.status == 'approved' ? 'success' : 
                                                                              membership.status == 'pending' ? 'warning' : 'danger'}">
                                                            ${membership.statusDisplayName}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <small>${membership.joinedAt.dayOfMonth}/${membership.joinedAt.monthValue}/${membership.joinedAt.year}</small>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4">
                                    <i class="bi bi-people display-4 text-muted"></i>
                                    <p class="mt-2 text-muted">Chưa tham gia câu lạc bộ nào</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Reset Password Modal -->
<div class="modal fade" id="resetPasswordModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Đặt lại mật khẩu</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/users/${user.id}/reset-password" method="post">
                <div class="modal-body">
                    <p>Đặt mật khẩu mới cho người dùng <strong>${user.fullName}</strong></p>
                    <div class="mb-3">
                        <label for="newPassword" class="form-label">Mật khẩu mới *</label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" 
                               required minlength="6">
                    </div>
                    <div class="mb-3">
                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu *</label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                               required minlength="6">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-key me-1"></i>Đặt lại mật khẩu
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../../common/footer.jsp"/>
