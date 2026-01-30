<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="common/header.jsp">
    <jsp:param name="title" value="Trang chủ"/>
</jsp:include>

<main class="main-content">
    <!-- Hero Section -->
    <section class="bg-primary text-white py-5">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-lg-6">
                    <h1 class="display-4 fw-bold mb-3">Quản lý Câu lạc bộ</h1>
                    <p class="lead mb-4">
                        Nền tảng quản lý câu lạc bộ hiện đại, giúp bạn dễ dàng tổ chức, 
                        điều hành và phát triển các hoạt động câu lạc bộ.
                    </p>
                    <c:if test="${empty sessionScope.currentUser}">
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-light btn-lg me-2">
                            <i class="bi bi-person-plus me-2"></i>Đăng ký ngay
                        </a>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-lg">
                            Đăng nhập
                        </a>
                    </c:if>
                    <c:if test="${not empty sessionScope.currentUser}">
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-light btn-lg">
                            <i class="bi bi-speedometer2 me-2"></i>Vào Dashboard
                        </a>
                    </c:if>
                </div>
                <div class="col-lg-6 text-center d-none d-lg-block">
                    <i class="bi bi-people-fill" style="font-size: 15rem; opacity: 0.3;"></i>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Statistics -->
    <section class="py-5 bg-light">
        <div class="container">
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card stat-card primary h-100">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted text-uppercase mb-2">Câu lạc bộ</h6>
                                <h2 class="mb-0">${totalClubs}</h2>
                            </div>
                            <i class="bi bi-people stat-icon text-primary"></i>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card success h-100">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted text-uppercase mb-2">Thành viên</h6>
                                <h2 class="mb-0">${totalUsers}</h2>
                            </div>
                            <i class="bi bi-person-check stat-icon text-success"></i>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card info h-100">
                        <div class="card-body d-flex justify-content-between align-items-center">
                            <div>
                                <h6 class="text-muted text-uppercase mb-2">Sự kiện</h6>
                                <h2 class="mb-0">${totalEvents}</h2>
                            </div>
                            <i class="bi bi-calendar-event stat-icon text-info"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Clubs Section -->
    <section class="py-5">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-people me-2"></i>Các câu lạc bộ</h2>
                <a href="${pageContext.request.contextPath}/clubs" class="btn btn-outline-primary">
                    Xem tất cả <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            
            <div class="row g-4">
                <c:forEach var="club" items="${clubs}" end="5">
                    <div class="col-md-6 col-lg-4">
                        <div class="card h-100">
                            <div class="card-body">
                                <h5 class="card-title">
                                    <a href="${pageContext.request.contextPath}/clubs/${club.id}" 
                                       class="text-decoration-none">
                                        ${club.name}
                                    </a>
                                </h5>
                                <c:if test="${not empty club.category}">
                                    <span class="badge bg-secondary mb-2">${club.category}</span>
                                </c:if>
                                <p class="card-text text-muted">
                                    <c:choose>
                                        <c:when test="${not empty club.description}">
                                            ${club.description.length() > 100 ? 
                                              club.description.substring(0, 100).concat('...') : 
                                              club.description}
                                        </c:when>
                                        <c:otherwise>
                                            Chưa có mô tả
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                            </div>
                            <div class="card-footer bg-transparent">
                                <small class="text-muted">
                                    <i class="bi bi-people me-1"></i>${club.memberCount} thành viên
                                </small>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                
                <c:if test="${empty clubs}">
                    <div class="col-12">
                        <div class="text-center py-5">
                            <i class="bi bi-inbox display-1 text-muted"></i>
                            <p class="text-muted mt-3">Chưa có câu lạc bộ nào</p>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </section>
    
    <!-- Upcoming Events -->
    <section class="py-5 bg-light">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-calendar-event me-2"></i>Sự kiện sắp tới</h2>
                <a href="${pageContext.request.contextPath}/events" class="btn btn-outline-primary">
                    Xem tất cả <i class="bi bi-arrow-right"></i>
                </a>
            </div>
            
            <div class="row g-4">
                <c:forEach var="event" items="${upcomingEvents}">
                    <div class="col-md-6">
                        <div class="card h-100">
                            <div class="card-body">
                                <div class="d-flex">
                                    <div class="text-center me-3" style="min-width: 60px;">
                                        <div class="bg-primary text-white rounded p-2">
                                            <div class="fw-bold fs-4">
                                                ${event.startsAt.dayOfMonth}
                                            </div>
                                            <small>Th${event.startsAt.monthValue}</small>
                                        </div>
                                    </div>
                                    <div>
                                        <h5 class="card-title mb-1">
                                            <a href="${pageContext.request.contextPath}/events/${event.id}"
                                               class="text-decoration-none">
                                                ${event.title}
                                            </a>
                                        </h5>
                                        <p class="text-muted mb-1">
                                            <i class="bi bi-people me-1"></i>${event.club.name}
                                        </p>
                                        <p class="text-muted mb-0">
                                            <i class="bi bi-clock me-1"></i>
                                            ${event.startsAt.hour}:${event.startsAt.minute < 10 ? '0' : ''}${event.startsAt.minute}
                                            <c:if test="${not empty event.location}">
                                                <span class="ms-2">
                                                    <i class="bi bi-geo-alt me-1"></i>${event.location}
                                                </span>
                                            </c:if>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
                
                <c:if test="${empty upcomingEvents}">
                    <div class="col-12">
                        <div class="text-center py-5">
                            <i class="bi bi-calendar-x display-1 text-muted"></i>
                            <p class="text-muted mt-3">Chưa có sự kiện nào sắp tới</p>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </section>
</main>

<jsp:include page="common/footer.jsp"/>
