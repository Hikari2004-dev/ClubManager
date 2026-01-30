<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${club.name}"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="../common/alerts.jsp"/>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-3">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/clubs">Câu lạc bộ</a></li>
                <li class="breadcrumb-item active">${club.name}</li>
            </ol>
        </nav>
        
        <!-- Club Header -->
        <div class="card mb-4">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-8">
                        <div class="d-flex align-items-start">
                            <div class="bg-primary text-white rounded p-3 me-3">
                                <i class="bi bi-people-fill fs-1"></i>
                            </div>
                            <div>
                                <h1 class="h2 mb-1">${club.name}</h1>
                                <div class="mb-2">
                                    <span class="badge bg-${club.status == 'active' ? 'success' : 'secondary'} me-2">
                                        ${club.status == 'active' ? 'Đang hoạt động' : club.status}
                                    </span>
                                    <c:if test="${not empty club.category}">
                                        <span class="badge bg-primary">${club.category}</span>
                                    </c:if>
                                </div>
                                <c:if test="${not empty club.supervisor}">
                                    <p class="text-muted mb-0">
                                        <i class="bi bi-person-badge me-1"></i>
                                        Giáo viên phụ trách: <strong>${club.supervisor.fullName}</strong>
                                    </p>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4 text-md-end mt-3 mt-md-0">
                        <c:if test="${canManage}">
                            <a href="${pageContext.request.contextPath}/clubs/${club.id}/edit" 
                               class="btn btn-outline-primary me-2">
                                <i class="bi bi-pencil me-1"></i>Chỉnh sửa
                            </a>
                        </c:if>
                        
                        <c:choose>
                            <c:when test="${empty userMembership}">
                                <form action="${pageContext.request.contextPath}/clubs/${club.id}/join" 
                                      method="post" class="d-inline">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-person-plus me-1"></i>Xin tham gia
                                    </button>
                                </form>
                            </c:when>
                            <c:when test="${userMembership.status == 'pending'}">
                                <span class="badge bg-warning p-2">
                                    <i class="bi bi-hourglass-split me-1"></i>Đang chờ duyệt
                                </span>
                            </c:when>
                            <c:when test="${userMembership.status == 'approved'}">
                                <span class="badge bg-success p-2 me-2">
                                    <i class="bi bi-check-circle me-1"></i>
                                    ${userMembership.clubRoleDisplayName}
                                </span>
                                <c:if test="${userMembership.clubRole != 'leader'}">
                                    <form action="${pageContext.request.contextPath}/clubs/${club.id}/leave" 
                                          method="post" class="d-inline"
                                          onsubmit="return confirm('Bạn có chắc muốn rời khỏi câu lạc bộ này?')">
                                        <button type="submit" class="btn btn-outline-danger btn-sm">
                                            <i class="bi bi-box-arrow-left me-1"></i>Rời CLB
                                        </button>
                                    </form>
                                </c:if>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="row">
            <!-- Main Content -->
            <div class="col-lg-8">
                <!-- Description -->
                <div class="card mb-4">
                    <div class="card-header">
                        <i class="bi bi-info-circle me-2"></i>Giới thiệu
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty club.description}">
                                <p class="mb-0" style="white-space: pre-line;">${club.description}</p>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted mb-0"><em>Chưa có mô tả</em></p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <!-- Events -->
                <div class="card mb-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="bi bi-calendar-event me-2"></i>Sự kiện</span>
                        <c:if test="${canManage}">
                            <a href="${pageContext.request.contextPath}/events/create?clubId=${club.id}" 
                               class="btn btn-sm btn-primary">
                                <i class="bi bi-plus me-1"></i>Tạo sự kiện
                            </a>
                        </c:if>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty events}">
                                <div class="list-group list-group-flush">
                                    <c:forEach var="event" items="${events}" end="4">
                                        <a href="${pageContext.request.contextPath}/events/${event.id}" 
                                           class="list-group-item list-group-item-action">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h6 class="mb-1">${event.title}</h6>
                                                <span class="badge bg-${event.status == 'published' ? 'success' : 
                                                                        event.status == 'cancelled' ? 'danger' : 'secondary'}">
                                                    ${event.statusDisplayName}
                                                </span>
                                            </div>
                                            <small class="text-muted">
                                                <i class="bi bi-calendar me-1"></i>
                                                ${event.startsAt.dayOfMonth}/${event.startsAt.monthValue}/${event.startsAt.year}
                                                ${event.startsAt.hour}:${event.startsAt.minute < 10 ? '0' : ''}${event.startsAt.minute}
                                                <c:if test="${not empty event.location}">
                                                    <span class="ms-2"><i class="bi bi-geo-alt me-1"></i>${event.location}</span>
                                                </c:if>
                                            </small>
                                        </a>
                                    </c:forEach>
                                </div>
                                <c:if test="${events.size() > 5}">
                                    <div class="text-center mt-3">
                                        <a href="${pageContext.request.contextPath}/events?clubId=${club.id}">
                                            Xem tất cả ${events.size()} sự kiện
                                        </a>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted text-center mb-0">
                                    <i class="bi bi-calendar-x display-6 d-block mb-2"></i>
                                    Chưa có sự kiện nào
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <!-- Sidebar -->
            <div class="col-lg-4">
                <!-- Statistics -->
                <div class="card mb-4">
                    <div class="card-header">
                        <i class="bi bi-bar-chart me-2"></i>Thống kê
                    </div>
                    <div class="card-body">
                        <div class="row text-center">
                            <div class="col-6">
                                <h3 class="mb-0">${members.size()}</h3>
                                <small class="text-muted">Thành viên</small>
                            </div>
                            <div class="col-6">
                                <h3 class="mb-0">${events.size()}</h3>
                                <small class="text-muted">Sự kiện</small>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Members -->
                <div class="card mb-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="bi bi-people me-2"></i>Thành viên</span>
                        <c:if test="${canManage && pendingCount > 0}">
                            <span class="badge bg-warning">${pendingCount} chờ duyệt</span>
                        </c:if>
                    </div>
                    <div class="card-body p-0">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="member" items="${members}" end="9">
                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                    <div>
                                        <i class="bi bi-person-circle me-2 text-muted"></i>
                                        ${member.user.fullName}
                                    </div>
                                    <span class="badge bg-${member.clubRole == 'president' ? 'primary' : 
                                                           member.clubRole == 'vice_president' ? 'info' : 'secondary'}">
                                        ${member.clubRoleDisplayName}
                                    </span>
                                </li>
                            </c:forEach>
                        </ul>
                        <c:if test="${members.size() > 10 || canManage}">
                            <div class="card-footer text-center">
                                <a href="${pageContext.request.contextPath}/clubs/${club.id}/members">
                                    <c:choose>
                                        <c:when test="${canManage}">
                                            Quản lý thành viên
                                        </c:when>
                                        <c:otherwise>
                                            Xem tất cả ${members.size()} thành viên
                                        </c:otherwise>
                                    </c:choose>
                                </a>
                            </div>
                        </c:if>
                    </div>
                </div>
                
                <!-- Info -->
                <div class="card">
                    <div class="card-header">
                        <i class="bi bi-info-circle me-2"></i>Thông tin
                    </div>
                    <div class="card-body">
                        <p class="mb-2">
                            <strong>Ngày thành lập:</strong><br>
                            <fmt:formatDate value="${club.createdAt}" pattern="dd/MM/yyyy"/>
                        </p>
                        <c:if test="${not empty club.category}">
                            <p class="mb-0">
                                <strong>Danh mục:</strong><br>
                                ${club.category}
                            </p>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="../common/footer.jsp"/>
