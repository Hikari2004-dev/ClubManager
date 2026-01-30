<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../../common/header.jsp">
    <jsp:param name="title" value="${empty user ? 'Thêm người dùng' : 'Chỉnh sửa người dùng'}"/>
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
                <c:if test="${not empty user}">
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/admin/users/${user.id}">${user.fullName}</a>
                    </li>
                </c:if>
                <li class="breadcrumb-item active">
                    ${empty user ? 'Thêm người dùng' : 'Chỉnh sửa'}
                </li>
            </ol>
        </nav>
        
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">
                            <i class="bi bi-person-${empty user ? 'plus' : 'gear'} me-2"></i>
                            ${empty user ? 'Thêm người dùng mới' : 'Chỉnh sửa người dùng'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/admin/users${empty user ? '' : '/'.concat(user.id)}" 
                              method="post" class="needs-validation" novalidate>
                            <c:if test="${not empty user}">
                                <input type="hidden" name="_method" value="PUT">
                            </c:if>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="fullName" class="form-label">Họ và tên *</label>
                                    <input type="text" class="form-control" id="fullName" name="fullName" 
                                           value="${user.fullName}" required maxlength="100"
                                           placeholder="Nguyễn Văn A">
                                    <div class="invalid-feedback">Vui lòng nhập họ tên</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label">Email *</label>
                                    <input type="email" class="form-control" id="email" name="email" 
                                           value="${user.email}" required maxlength="100"
                                           placeholder="email@example.com"
                                           ${not empty user ? 'readonly' : ''}>
                                    <div class="invalid-feedback">Vui lòng nhập email hợp lệ</div>
                                </div>
                            </div>
                            
                            <c:if test="${empty user}">
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="password" class="form-label">Mật khẩu *</label>
                                        <input type="password" class="form-control" id="password" name="password" 
                                               required minlength="6" placeholder="Ít nhất 6 ký tự">
                                        <div class="invalid-feedback">Mật khẩu phải có ít nhất 6 ký tự</div>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="confirmPassword" class="form-label">Xác nhận mật khẩu *</label>
                                        <input type="password" class="form-control" id="confirmPassword" 
                                               name="confirmPassword" required minlength="6"
                                               placeholder="Nhập lại mật khẩu">
                                        <div class="invalid-feedback">Mật khẩu không khớp</div>
                                    </div>
                                </div>
                            </c:if>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="studentId" class="form-label">Mã sinh viên</label>
                                    <input type="text" class="form-control" id="studentId" name="studentId" 
                                           value="${user.studentId}" maxlength="20"
                                           placeholder="VD: 20210001">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label">Số điện thoại</label>
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           value="${user.phone}" maxlength="15"
                                           placeholder="0901234567">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="status" class="form-label">Trạng thái</label>
                                    <select class="form-select" id="status" name="status">
                                        <option value="active" ${user.status == 'active' || empty user.status ? 'selected' : ''}>
                                            Hoạt động
                                        </option>
                                        <option value="inactive" ${user.status == 'inactive' ? 'selected' : ''}>
                                            Không hoạt động
                                        </option>
                                        <option value="banned" ${user.status == 'banned' ? 'selected' : ''}>
                                            Đã cấm
                                        </option>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="avatarUrl" class="form-label">URL ảnh đại diện</label>
                                    <input type="url" class="form-control" id="avatarUrl" name="avatarUrl" 
                                           value="${user.avatarUrl}" maxlength="500"
                                           placeholder="https://example.com/avatar.jpg">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label class="form-label">Vai trò</label>
                                <div class="row">
                                    <c:forEach var="role" items="${roles}">
                                        <div class="col-md-4">
                                            <div class="form-check">
                                                <input class="form-check-input" type="checkbox" 
                                                       id="role_${role.id}" name="roleIds" value="${role.id}"
                                                       <c:forEach var="userRole" items="${user.roles}">
                                                           ${userRole.id == role.id ? 'checked' : ''}
                                                       </c:forEach>>
                                                <label class="form-check-label" for="role_${role.id}">
                                                    ${role.displayName}
                                                </label>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="bio" class="form-label">Tiểu sử</label>
                                <textarea class="form-control" id="bio" name="bio" rows="3"
                                          placeholder="Giới thiệu ngắn về người dùng...">${user.bio}</textarea>
                            </div>
                            
                            <hr class="my-4">
                            
                            <div class="d-flex justify-content-between">
                                <a href="${empty user ? pageContext.request.contextPath.concat('/admin/users') : 
                                          pageContext.request.contextPath.concat('/admin/users/').concat(user.id)}" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left me-1"></i>Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-check-lg me-1"></i>
                                    ${empty user ? 'Thêm người dùng' : 'Lưu thay đổi'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
// Form validation
(function() {
    'use strict';
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            // Check password match
            var password = document.getElementById('password');
            var confirmPassword = document.getElementById('confirmPassword');
            
            if (password && confirmPassword && password.value !== confirmPassword.value) {
                confirmPassword.setCustomValidity('Mật khẩu không khớp');
            } else if (confirmPassword) {
                confirmPassword.setCustomValidity('');
            }
            
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
})();
</script>

<jsp:include page="../../common/footer.jsp"/>
