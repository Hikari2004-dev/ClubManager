<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Quản lý thành viên - ${club.name}"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="../common/alerts.jsp"/>
        
        <nav aria-label="breadcrumb" class="mb-3">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/clubs">Câu lạc bộ</a></li>
                <li class="breadcrumb-item">
                    <a href="${pageContext.request.contextPath}/clubs/${club.id}">${club.name}</a>
                </li>
                <li class="breadcrumb-item active">Thành viên</li>
            </ol>
        </nav>
        
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="h3 mb-0">
                <i class="bi bi-people me-2"></i>Thành viên - ${club.name}
            </h1>
            <a href="${pageContext.request.contextPath}/clubs/${club.id}" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i>Quay lại CLB
            </a>
        </div>
        
        <!-- Filter Tabs -->
        <ul class="nav nav-tabs mb-4">
            <li class="nav-item">
                <a class="nav-link ${empty selectedStatus ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/clubs/${club.id}/members">
                    Tất cả
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${selectedStatus == 'approved' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/clubs/${club.id}/members?status=approved">
                    Đã duyệt
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${selectedStatus == 'pending' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/clubs/${club.id}/members?status=pending">
                    Chờ duyệt
                    <c:set var="pendingCount" value="0"/>
                    <c:forEach var="m" items="${members}">
                        <c:if test="${m.status == 'pending'}">
                            <c:set var="pendingCount" value="${pendingCount + 1}"/>
                        </c:if>
                    </c:forEach>
                    <c:if test="${pendingCount > 0}">
                        <span class="badge bg-warning text-dark">${pendingCount}</span>
                    </c:if>
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${selectedStatus == 'rejected' ? 'active' : ''}" 
                   href="${pageContext.request.contextPath}/clubs/${club.id}/members?status=rejected">
                    Đã từ chối
                </a>
            </li>
        </ul>
        
        <!-- Members Table -->
        <div class="card">
            <div class="card-body">
                <c:choose>
                    <c:when test="${not empty members}">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Thành viên</th>
                                        <th>Mã SV</th>
                                        <th>Lớp</th>
                                        <th>Vai trò</th>
                                        <th>Trạng thái</th>
                                        <th>Ngày tham gia</th>
                                        <c:if test="${canManage}">
                                            <th class="text-end">Thao tác</th>
                                        </c:if>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="member" items="${members}">
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <i class="bi bi-person-circle fs-4 me-2 text-muted"></i>
                                                    <div>
                                                        <strong>${member.user.fullName}</strong>
                                                        <br><small class="text-muted">${member.user.email}</small>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>${member.user.studentCode}</td>
                                            <td>${member.user.className}</td>
                                            <td>
                                                <c:if test="${canManage && member.status == 'approved'}">
                                                    <form action="${pageContext.request.contextPath}/clubs/${club.id}/members/${member.id}/role" 
                                                          method="post" class="d-inline">
                                                        <select name="role" class="form-select form-select-sm" 
                                                                style="width: auto;" onchange="this.form.submit()">
                                                            <option value="member" ${member.clubRole == 'member' ? 'selected' : ''}>
                                                                Thành viên
                                                            </option>
                                                            <option value="secretary" ${member.clubRole == 'secretary' ? 'selected' : ''}>
                                                                Thư ký
                                                            </option>
                                                            <option value="vice_president" ${member.clubRole == 'vice_president' ? 'selected' : ''}>
                                                                Phó chủ nhiệm
                                                            </option>
                                                            <option value="president" ${member.clubRole == 'president' ? 'selected' : ''}>
                                                                Chủ nhiệm
                                                            </option>
                                                        </select>
                                                    </form>
                                                </c:if>
                                                <c:if test="${!canManage || member.status != 'approved'}">
                                                    <span class="badge bg-${member.clubRole == 'president' ? 'primary' : 
                                                                           member.clubRole == 'vice_president' ? 'info' : 'secondary'}">
                                                        ${member.clubRoleDisplayName}
                                                    </span>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${member.status == 'pending'}">
                                                        <span class="badge bg-warning">Chờ duyệt</span>
                                                    </c:when>
                                                    <c:when test="${member.status == 'approved'}">
                                                        <span class="badge bg-success">Đã duyệt</span>
                                                    </c:when>
                                                    <c:when test="${member.status == 'rejected'}">
                                                        <span class="badge bg-danger">Đã từ chối</span>
                                                    </c:when>
                                                    <c:when test="${member.status == 'left'}">
                                                        <span class="badge bg-secondary">Đã rời</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <fmt:formatDate value="${member.requestedAt}" pattern="dd/MM/yyyy"/>
                                            </td>
                                            <c:if test="${canManage}">
                                                <td class="text-end">
                                                    <c:if test="${member.status == 'pending'}">
                                                        <form action="${pageContext.request.contextPath}/clubs/${club.id}/members/${member.id}/approve" 
                                                              method="post" class="d-inline">
                                                            <button type="submit" class="btn btn-sm btn-success" 
                                                                    title="Duyệt">
                                                                <i class="bi bi-check-lg"></i>
                                                            </button>
                                                        </form>
                                                        <button type="button" class="btn btn-sm btn-danger" 
                                                                title="Từ chối" data-bs-toggle="modal" 
                                                                data-bs-target="#rejectModal${member.id}">
                                                            <i class="bi bi-x-lg"></i>
                                                        </button>
                                                        
                                                        <!-- Reject Modal -->
                                                        <div class="modal fade" id="rejectModal${member.id}" tabindex="-1">
                                                            <div class="modal-dialog">
                                                                <div class="modal-content">
                                                                    <form action="${pageContext.request.contextPath}/clubs/${club.id}/members/${member.id}/reject" 
                                                                          method="post">
                                                                        <div class="modal-header">
                                                                            <h5 class="modal-title">Từ chối yêu cầu</h5>
                                                                            <button type="button" class="btn-close" 
                                                                                    data-bs-dismiss="modal"></button>
                                                                        </div>
                                                                        <div class="modal-body">
                                                                            <p>Từ chối yêu cầu của <strong>${member.user.fullName}</strong>?</p>
                                                                            <div class="mb-3">
                                                                                <label class="form-label">Lý do từ chối</label>
                                                                                <textarea class="form-control" name="reason" 
                                                                                          rows="3" placeholder="Nhập lý do..."></textarea>
                                                                            </div>
                                                                        </div>
                                                                        <div class="modal-footer">
                                                                            <button type="button" class="btn btn-secondary" 
                                                                                    data-bs-dismiss="modal">Hủy</button>
                                                                            <button type="submit" class="btn btn-danger">
                                                                                Từ chối
                                                                            </button>
                                                                        </div>
                                                                    </form>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                    
                                                    <c:if test="${member.status == 'approved' && member.clubRole != 'leader'}">
                                                        <form action="${pageContext.request.contextPath}/clubs/${club.id}/members/${member.id}/remove" 
                                                              method="post" class="d-inline"
                                                              onsubmit="return confirm('Bạn có chắc muốn xóa thành viên này?')">
                                                            <button type="submit" class="btn btn-sm btn-outline-danger" 
                                                                    title="Xóa">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </form>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <i class="bi bi-people display-1 text-muted"></i>
                            <p class="text-muted mt-3">Không có thành viên nào</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</main>

<jsp:include page="../common/footer.jsp"/>
