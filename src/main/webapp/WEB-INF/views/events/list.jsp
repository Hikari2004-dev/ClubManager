<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Danh sách sự kiện"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="../common/alerts.jsp"/>
        
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="h3 mb-0">
                <i class="bi bi-calendar-event me-2"></i>Sự kiện
            </h1>
            <c:if test="${canCreateEvent}">
                <a href="${pageContext.request.contextPath}/events/create" class="btn btn-primary">
                    <i class="bi bi-plus-circle me-1"></i>Tạo sự kiện
                </a>
            </c:if>
        </div>
        
        <!-- Search & Filter -->
        <div class="card mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/events" method="get" class="row g-3">
                    <div class="col-md-5">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="text" class="form-control" name="keyword" 
                                   value="${keyword}" placeholder="Tìm kiếm sự kiện...">
                        </div>
                    </div>
                    <div class="col-md-5">
                        <select class="form-select" name="clubId">
                            <option value="">-- Tất cả câu lạc bộ --</option>
                            <c:forEach var="club" items="${clubs}">
                                <option value="${club.id}" ${club.id == selectedClubId ? 'selected' : ''}>
                                    ${club.name}
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
        
        <!-- Events List -->
        <div class="row g-4">
            <c:forEach var="event" items="${events}">
                <div class="col-md-6">
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex">
                                <div class="text-center me-3" style="min-width: 70px;">
                                    <div class="bg-primary text-white rounded p-2">
                                        <div class="fw-bold fs-3">${event.startsAt.dayOfMonth}</div>
                                        <small>Th${event.startsAt.monthValue}/${event.startsAt.year}</small>
                                    </div>
                                </div>
                                <div class="flex-grow-1">
                                    <div class="d-flex justify-content-between align-items-start">
                                        <h5 class="card-title mb-1">
                                            <a href="${pageContext.request.contextPath}/events/${event.id}" 
                                               class="text-decoration-none">
                                                ${event.title}
                                            </a>
                                        </h5>
                                        <span class="badge bg-${event.status == 'published' ? 'success' : 
                                                              event.status == 'cancelled' ? 'danger' : 
                                                              event.status == 'completed' ? 'info' : 'secondary'}">
                                            ${event.statusDisplayName}
                                        </span>
                                    </div>
                                    <p class="text-muted mb-2">
                                        <a href="${pageContext.request.contextPath}/clubs/${event.clubId}" 
                                           class="text-decoration-none">
                                            <i class="bi bi-people me-1"></i>${event.club.name}
                                        </a>
                                    </p>
                                    <p class="card-text text-muted mb-2">
                                        <c:if test="${not empty event.description}">
                                            ${event.description.length() > 100 ? 
                                              event.description.substring(0, 100).concat('...') : 
                                              event.description}
                                        </c:if>
                                    </p>
                                    <div class="d-flex flex-wrap gap-2">
                                        <small class="text-muted">
                                            <i class="bi bi-clock me-1"></i>
                                            ${event.startsAt.hour}:${event.startsAt.minute < 10 ? '0' : ''}${event.startsAt.minute}
                                        </small>
                                        <c:if test="${not empty event.location}">
                                            <small class="text-muted">
                                                <i class="bi bi-geo-alt me-1"></i>${event.location}
                                            </small>
                                        </c:if>
                                        <c:if test="${not empty event.capacity}">
                                            <small class="text-muted">
                                                <i class="bi bi-person me-1"></i>Tối đa ${event.capacity} người
                                            </small>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty events}">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body text-center py-5">
                            <i class="bi bi-calendar-x display-1 text-muted"></i>
                            <h5 class="mt-3">Không tìm thấy sự kiện nào</h5>
                            <p class="text-muted">
                                <c:choose>
                                    <c:when test="${not empty keyword}">
                                        Không có kết quả cho từ khóa "${keyword}"
                                    </c:when>
                                    <c:otherwise>
                                        Chưa có sự kiện nào được tạo
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</main>

<jsp:include page="../common/footer.jsp"/>
