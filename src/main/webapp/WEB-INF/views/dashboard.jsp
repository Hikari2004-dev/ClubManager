<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Dashboard"/>
</jsp:include>

<main class="main-content">
    <div class="container-fluid py-4">
        <jsp:include page="common/alerts.jsp"/>
        
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="h3 mb-0">
                <i class="bi bi-speedometer2 me-2"></i>Dashboard
            </h1>
            <span class="text-muted">
                Xin chào, <strong>${sessionScope.currentUser.fullName}</strong>!
            </span>
        </div>
        
        <!-- Admin/Teacher Statistics -->
        <c:if test="${isAdmin || isTeacher}">
            <div class="row g-4 mb-4">
                <div class="col-xl-3 col-md-6">
                    <div class="card stat-card primary h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-xs text-uppercase text-muted mb-1">Tổng câu lạc bộ</div>
                                    <div class="h4 mb-0 fw-bold">${totalClubs}</div>
                                </div>
                                <i class="bi bi-people stat-icon text-primary"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6">
                    <div class="card stat-card success h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-xs text-uppercase text-muted mb-1">Tổng người dùng</div>
                                    <div class="h4 mb-0 fw-bold">${totalUsers}</div>
                                </div>
                                <i class="bi bi-person-check stat-icon text-success"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6">
                    <div class="card stat-card info h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-xs text-uppercase text-muted mb-1">Tổng sự kiện</div>
                                    <div class="h4 mb-0 fw-bold">${totalEvents}</div>
                                </div>
                                <i class="bi bi-calendar-event stat-icon text-info"></i>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xl-3 col-md-6">
                    <div class="card stat-card warning h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <div class="text-xs text-uppercase text-muted mb-1">CLB của tôi</div>
                                    <div class="h4 mb-0 fw-bold">${myClubs.size()}</div>
                                </div>
                                <i class="bi bi-star stat-icon text-warning"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Admin Quick Links -->
            <c:if test="${isAdmin}">
                <div class="card mb-4">
                    <div class="card-header">
                        <i class="bi bi-gear me-2"></i>Quản trị nhanh
                    </div>
                    <div class="card-body">
                        <div class="row g-3">
                            <div class="col-auto">
                                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-primary">
                                    <i class="bi bi-people me-1"></i>Quản lý người dùng
                                </a>
                            </div>
                            <div class="col-auto">
                                <a href="${pageContext.request.contextPath}/clubs/create" class="btn btn-outline-success">
                                    <i class="bi bi-plus-circle me-1"></i>Tạo câu lạc bộ mới
                                </a>
                            </div>
                            <div class="col-auto">
                                <a href="${pageContext.request.contextPath}/admin/users/create" class="btn btn-outline-info">
                                    <i class="bi bi-person-plus me-1"></i>Thêm người dùng
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:if>
        
        <div class="row">
            <!-- My Clubs -->
            <div class="col-lg-6 mb-4">
                <div class="card h-100">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="bi bi-people me-2"></i>Câu lạc bộ của tôi</span>
                        <a href="${pageContext.request.contextPath}/clubs" class="btn btn-sm btn-outline-primary">
                            Xem tất cả
                        </a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty myClubs}">
                                <div class="list-group list-group-flush">
                                    <c:forEach var="club" items="${myClubs}">
                                        <a href="${pageContext.request.contextPath}/clubs/${club.id}" 
                                           class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
                                            <div>
                                                <h6 class="mb-1">${club.name}</h6>
                                                <small class="text-muted">
                                                    <c:if test="${not empty club.category}">
                                                        ${club.category}
                                                    </c:if>
                                                </small>
                                            </div>
                                            <span class="badge bg-${club.status == 'active' ? 'success' : 'secondary'}">
                                                ${club.status == 'active' ? 'Hoạt động' : club.status}
                                            </span>
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4">
                                    <i class="bi bi-inbox display-4 text-muted"></i>
                                    <p class="text-muted mt-2">Bạn chưa tham gia câu lạc bộ nào</p>
                                    <a href="${pageContext.request.contextPath}/clubs" class="btn btn-primary">
                                        Khám phá câu lạc bộ
                                    </a>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Upcoming Events -->
            <div class="col-lg-6 mb-4">
                <div class="card h-100">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="bi bi-calendar-event me-2"></i>Sự kiện sắp tới</span>
                        <a href="${pageContext.request.contextPath}/events" class="btn btn-sm btn-outline-primary">
                            Xem tất cả
                        </a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty upcomingEvents}">
                                <div class="list-group list-group-flush">
                                    <c:forEach var="event" items="${upcomingEvents}" end="4">
                                        <a href="${pageContext.request.contextPath}/events/${event.id}" 
                                           class="list-group-item list-group-item-action">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h6 class="mb-1">${event.title}</h6>
                                                <small class="text-muted">
                                                    ${event.startsAt.dayOfMonth}/${event.startsAt.monthValue}
                                                </small>
                                            </div>
                                            <small class="text-muted">
                                                <i class="bi bi-people me-1"></i>${event.club.name}
                                                <c:if test="${not empty event.location}">
                                                    <span class="ms-2">
                                                        <i class="bi bi-geo-alt me-1"></i>${event.location}
                                                    </span>
                                                </c:if>
                                            </small>
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4">
                                    <i class="bi bi-calendar-x display-4 text-muted"></i>
                                    <p class="text-muted mt-2">Không có sự kiện nào sắp tới</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- My Membership Requests -->
        <c:if test="${not empty myMemberships}">
            <div class="card mb-4">
                <div class="card-header">
                    <i class="bi bi-hourglass-split me-2"></i>Yêu cầu tham gia của tôi
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Câu lạc bộ</th>
                                    <th>Ngày gửi</th>
                                    <th>Trạng thái</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="membership" items="${myMemberships}">
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/clubs/${membership.clubId}">
                                                ${membership.club.name}
                                            </a>
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${membership.requestedAt}" 
                                                          pattern="dd/MM/yyyy HH:mm"/>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${membership.status == 'pending'}">
                                                    <span class="badge bg-warning">Chờ duyệt</span>
                                                </c:when>
                                                <c:when test="${membership.status == 'approved'}">
                                                    <span class="badge bg-success">Đã duyệt</span>
                                                </c:when>
                                                <c:when test="${membership.status == 'rejected'}">
                                                    <span class="badge bg-danger">Đã từ chối</span>
                                                    <c:if test="${not empty membership.rejectedReason}">
                                                        <br><small class="text-muted">${membership.rejectedReason}</small>
                                                    </c:if>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${membership.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </c:if>
    </div>
</main>

<jsp:include page="common/footer.jsp"/>
