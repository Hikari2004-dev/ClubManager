<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Hồ sơ của tôi"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="common/alerts.jsp"/>
        
        <h1 class="h3 mb-4">
            <i class="bi bi-person-circle me-2"></i>Hồ sơ của tôi
        </h1>
        
        <div class="row">
            <!-- Profile Card -->
            <div class="col-lg-4">
                <div class="card mb-4">
                    <div class="card-body text-center">
                        <div class="avatar-lg bg-primary bg-opacity-10 rounded-circle mx-auto mb-3 d-flex align-items-center justify-content-center"
                             style="width: 120px; height: 120px;">
                            <span class="text-primary display-4 fw-bold">
                                ${sessionScope.currentUser.fullName.substring(0, 1).toUpperCase()}
                            </span>
                        </div>
                        
                        <h4 class="mb-1">${sessionScope.currentUser.fullName}</h4>
                        <p class="text-muted mb-3">${sessionScope.currentUser.email}</p>
                        
                        <c:if test="${not empty sessionScope.currentUser.className}">
                            <span class="badge bg-info">${sessionScope.currentUser.className}</span>
                        </c:if>
                    </div>
                </div>
                
                <!-- Quick Stats -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-bar-chart me-2"></i>Thống kê</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Câu lạc bộ tham gia:</span>
                            <strong>${memberships.size()}</strong>
                        </div>
                        <div class="d-flex justify-content-between">
                            <span class="text-muted">Ngày tham gia:</span>
                            <strong>
                                <fmt:formatDate value="${sessionScope.currentUser.createdAt}" pattern="dd/MM/yyyy"/>
                            </strong>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Main Content -->
            <div class="col-lg-8">
                <!-- Edit Profile -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-pencil me-2"></i>Cập nhật thông tin</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/profile" method="post">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="fullName" class="form-label">Họ và tên *</label>
                                    <input type="text" class="form-control" id="fullName" name="fullName" 
                                           value="${sessionScope.currentUser.fullName}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="email" class="form-label">Email</label>
                                    <input type="email" class="form-control" id="email" 
                                           value="${sessionScope.currentUser.email}" readonly>
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="studentCode" class="form-label">Mã sinh viên</label>
                                    <input type="text" class="form-control" id="studentCode" name="studentCode" 
                                           value="${sessionScope.currentUser.studentCode}">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label">Số điện thoại</label>
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           value="${sessionScope.currentUser.phone}">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="className" class="form-label">Lớp</label>
                                <input type="text" class="form-control" id="className" name="className" 
                                       value="${sessionScope.currentUser.className}"
                                       placeholder="VD: CNTT-K21">
                            </div>
                            
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-check-lg me-1"></i>Cập nhật
                            </button>
                        </form>
                    </div>
                </div>
                
                <!-- Change Password -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-key me-2"></i>Đổi mật khẩu</h5>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/profile/password" method="post">
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="currentPassword" class="form-label">Mật khẩu hiện tại *</label>
                                    <input type="password" class="form-control" id="currentPassword" 
                                           name="currentPassword" required>
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="newPassword" class="form-label">Mật khẩu mới *</label>
                                    <input type="password" class="form-control" id="newPassword" 
                                           name="newPassword" required minlength="6">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="confirmNewPassword" class="form-label">Xác nhận mật khẩu mới *</label>
                                    <input type="password" class="form-control" id="confirmNewPassword" 
                                           name="confirmNewPassword" required minlength="6">
                                </div>
                            </div>
                            <button type="submit" class="btn btn-warning">
                                <i class="bi bi-key me-1"></i>Đổi mật khẩu
                            </button>
                        </form>
                    </div>
                </div>
                
                <!-- My Clubs -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="bi bi-people me-2"></i>Câu lạc bộ của tôi
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
                                                <th></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="membership" items="${memberships}">
                                                <tr>
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/clubs/${membership.clubId}" 
                                                           class="text-decoration-none fw-medium">
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
                                                    <td>
                                                        <a href="${pageContext.request.contextPath}/clubs/${membership.clubId}" 
                                                           class="btn btn-sm btn-outline-primary">
                                                            <i class="bi bi-eye"></i>
                                                        </a>
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
                                    <p class="mt-2 text-muted">Bạn chưa tham gia câu lạc bộ nào</p>
                                    <a href="${pageContext.request.contextPath}/clubs" class="btn btn-primary">
                                        <i class="bi bi-search me-1"></i>Khám phá câu lạc bộ
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>
