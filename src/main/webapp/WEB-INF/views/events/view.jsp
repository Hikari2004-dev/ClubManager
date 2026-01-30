<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${event.title}"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="../common/alerts.jsp"/>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="${pageContext.request.contextPath}/events">Sự kiện</a>
                </li>
                <li class="breadcrumb-item active">${event.title}</li>
            </ol>
        </nav>
        
        <div class="row">
            <!-- Main Content -->
            <div class="col-lg-8">
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-start mb-3">
                            <h1 class="h3 mb-0">${event.title}</h1>
                            <span class="badge bg-${event.status == 'published' ? 'success' : 
                                                  event.status == 'cancelled' ? 'danger' : 
                                                  event.status == 'completed' ? 'info' : 'secondary'} fs-6">
                                ${event.statusDisplayName}
                            </span>
                        </div>
                        
                        <p class="mb-3">
                            <a href="${pageContext.request.contextPath}/clubs/${event.clubId}" 
                               class="text-decoration-none">
                                <i class="bi bi-people-fill me-1"></i>${event.club.name}
                            </a>
                        </p>
                        
                        <div class="row g-3 mb-4">
                            <div class="col-md-6">
                                <div class="d-flex align-items-center">
                                    <div class="bg-primary bg-opacity-10 rounded p-3 me-3">
                                        <i class="bi bi-calendar-event text-primary fs-4"></i>
                                    </div>
                                    <div>
                                        <small class="text-muted d-block">Thời gian bắt đầu</small>
                                        <strong>
                                            ${event.startsAt.dayOfMonth}/${event.startsAt.monthValue}/${event.startsAt.year}
                                            ${event.startsAt.hour}:${event.startsAt.minute < 10 ? '0' : ''}${event.startsAt.minute}
                                        </strong>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${not empty event.endsAt}">
                                <div class="col-md-6">
                                    <div class="d-flex align-items-center">
                                        <div class="bg-secondary bg-opacity-10 rounded p-3 me-3">
                                            <i class="bi bi-calendar-check text-secondary fs-4"></i>
                                        </div>
                                        <div>
                                            <small class="text-muted d-block">Thời gian kết thúc</small>
                                            <strong>
                                                ${event.endsAt.dayOfMonth}/${event.endsAt.monthValue}/${event.endsAt.year}
                                                ${event.endsAt.hour}:${event.endsAt.minute < 10 ? '0' : ''}${event.endsAt.minute}
                                            </strong>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${not empty event.location}">
                                <div class="col-md-6">
                                    <div class="d-flex align-items-center">
                                        <div class="bg-success bg-opacity-10 rounded p-3 me-3">
                                            <i class="bi bi-geo-alt text-success fs-4"></i>
                                        </div>
                                        <div>
                                            <small class="text-muted d-block">Địa điểm</small>
                                            <strong>${event.location}</strong>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${not empty event.capacity}">
                                <div class="col-md-6">
                                    <div class="d-flex align-items-center">
                                        <div class="bg-info bg-opacity-10 rounded p-3 me-3">
                                            <i class="bi bi-person text-info fs-4"></i>
                                        </div>
                                        <div>
                                            <small class="text-muted d-block">Số lượng tối đa</small>
                                            <strong>${event.capacity} người</strong>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        
                        <c:if test="${not empty event.description}">
                            <h5>Mô tả sự kiện</h5>
                            <p class="card-text">${event.description}</p>
                        </c:if>
                    </div>
                </div>
                
                <!-- External Registrations -->
                <c:if test="${canManage && not empty registrations}">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="bi bi-person-plus me-2"></i>Đăng ký tham gia bên ngoài
                                <span class="badge bg-primary">${registrations.size()}</span>
                            </h5>
                        </div>
                        <div class="card-body p-0">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th>Họ tên</th>
                                            <th>Email</th>
                                            <th>Điện thoại</th>
                                            <th>Trạng thái</th>
                                            <th>Ngày đăng ký</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="reg" items="${registrations}">
                                            <tr>
                                                <td>${reg.participant.fullName}</td>
                                                <td>${reg.participant.email}</td>
                                                <td>${reg.participant.phone}</td>
                                                <td>
                                                    <span class="badge bg-${reg.status == 'approved' ? 'success' : 
                                                                          reg.status == 'rejected' ? 'danger' : 
                                                                          reg.status == 'attended' ? 'info' : 'warning'}">
                                                        ${reg.statusDisplayName}
                                                    </span>
                                                </td>
                                                <td>
                                                    <small>${reg.registeredAt.dayOfMonth}/${reg.registeredAt.monthValue}/${reg.registeredAt.year}</small>
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
            
            <!-- Sidebar -->
            <div class="col-lg-4">
                <!-- Actions -->
                <c:if test="${canManage}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0"><i class="bi bi-gear me-2"></i>Quản lý</h5>
                        </div>
                        <div class="card-body">
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/events/${event.id}/edit" 
                                   class="btn btn-outline-primary">
                                    <i class="bi bi-pencil me-1"></i>Chỉnh sửa
                                </a>
                                <c:if test="${event.status == 'draft'}">
                                    <form action="${pageContext.request.contextPath}/events/${event.id}/publish" 
                                          method="post" style="display: inline;">
                                        <button type="submit" class="btn btn-success w-100">
                                            <i class="bi bi-check-circle me-1"></i>Xuất bản
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${event.status == 'published'}">
                                    <form action="${pageContext.request.contextPath}/events/${event.id}/cancel" 
                                          method="post" style="display: inline;">
                                        <button type="submit" class="btn btn-warning w-100" 
                                                onclick="return confirm('Bạn có chắc muốn hủy sự kiện này?')">
                                            <i class="bi bi-x-circle me-1"></i>Hủy sự kiện
                                        </button>
                                    </form>
                                </c:if>
                                <button type="button" class="btn btn-outline-danger" 
                                        data-bs-toggle="modal" data-bs-target="#deleteModal">
                                    <i class="bi bi-trash me-1"></i>Xóa sự kiện
                                </button>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Club Info -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="bi bi-people me-2"></i>Câu lạc bộ tổ chức</h5>
                    </div>
                    <div class="card-body">
                        <h6 class="card-title">${event.club.name}</h6>
                        <c:if test="${not empty event.club.description}">
                            <p class="card-text text-muted small">
                                ${event.club.description.length() > 100 ? 
                                  event.club.description.substring(0, 100).concat('...') : 
                                  event.club.description}
                            </p>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/clubs/${event.clubId}" 
                           class="btn btn-outline-primary btn-sm">
                            Xem câu lạc bộ
                        </a>
                    </div>
                </div>
                
                <!-- Đăng ký tham gia sự kiện (cho thành viên CLB) -->
                <c:if test="${event.status == 'published' && not empty sessionScope.currentUser}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="bi bi-person-plus me-2"></i>Đăng ký tham gia
                                <span class="badge bg-primary ms-2">
                                    ${registrationCount}<c:if test="${not empty event.capacity}">/${event.capacity}</c:if>
                                </span>
                            </h5>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${isRegistered}">
                                    <div class="alert alert-success mb-3">
                                        <i class="bi bi-check-circle me-2"></i>Bạn đã đăng ký tham gia sự kiện này
                                    </div>
                                    <form action="${pageContext.request.contextPath}/events/${event.id}/unregister" method="post">
                                        <button type="submit" class="btn btn-outline-danger" 
                                                onclick="return confirm('Bạn có chắc muốn hủy đăng ký?')">
                                            <i class="bi bi-x-circle me-1"></i>Hủy đăng ký
                                        </button>
                                    </form>
                                </c:when>
                                <c:when test="${isMember}">
                                    <c:choose>
                                        <c:when test="${not empty event.capacity && registrationCount >= event.capacity}">
                                            <div class="alert alert-warning mb-0">
                                                <i class="bi bi-exclamation-triangle me-2"></i>Sự kiện đã đủ số lượng đăng ký
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="${pageContext.request.contextPath}/events/${event.id}/register" method="post">
                                                <button type="submit" class="btn btn-primary">
                                                    <i class="bi bi-check me-1"></i>Đăng ký tham gia
                                                </button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info mb-0">
                                        <i class="bi bi-info-circle me-2"></i>
                                        Bạn cần là thành viên của câu lạc bộ để đăng ký tham gia sự kiện này.
                                        <a href="${pageContext.request.contextPath}/clubs/${event.clubId}" class="alert-link">
                                            Xin tham gia câu lạc bộ
                                        </a>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:if>
                
                <!-- Danh sách người đăng ký (cho quản lý) -->
                <c:if test="${canManage && not empty registeredUsers}">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="bi bi-list-check me-2"></i>Danh sách đăng ký
                                <span class="badge bg-primary">${registeredUsers.size()}</span>
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Họ tên</th>
                                            <th>MSSV</th>
                                            <th>Lớp</th>
                                            <th>Email</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="user" items="${registeredUsers}" varStatus="loop">
                                            <tr>
                                                <td>${loop.index + 1}</td>
                                                <td>${user.fullName}</td>
                                                <td>${user.studentCode}</td>
                                                <td>${user.className}</td>
                                                <td>${user.email}</td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- External Registration -->
                <c:if test="${event.status == 'published' && event.externalRegistrationEnabled}">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0"><i class="bi bi-person-plus me-2"></i>Đăng ký tham gia</h5>
                        </div>
                        <div class="card-body">
                            <p class="card-text text-muted small">
                                Sự kiện này cho phép đăng ký từ bên ngoài. Vui lòng điền thông tin bên dưới.
                            </p>
                            <form action="${pageContext.request.contextPath}/events/${event.id}/register" method="post">
                                <div class="mb-3">
                                    <label for="fullName" class="form-label">Họ tên *</label>
                                    <input type="text" class="form-control" id="fullName" name="fullName" required>
                                </div>
                                <div class="mb-3">
                                    <label for="email" class="form-label">Email *</label>
                                    <input type="email" class="form-control" id="email" name="email" required>
                                </div>
                                <div class="mb-3">
                                    <label for="phone" class="form-label">Số điện thoại</label>
                                    <input type="tel" class="form-control" id="phone" name="phone">
                                </div>
                                <button type="submit" class="btn btn-primary w-100">
                                    <i class="bi bi-check me-1"></i>Đăng ký
                                </button>
                            </form>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</main>

<!-- Delete Modal -->
<c:if test="${canManage}">
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Xóa sự kiện</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Bạn có chắc chắn muốn xóa sự kiện <strong>${event.title}</strong>?</p>
                    <p class="text-danger small">
                        <i class="bi bi-exclamation-triangle me-1"></i>
                        Hành động này không thể hoàn tác.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <form action="${pageContext.request.contextPath}/events/${event.id}/delete" method="post">
                        <button type="submit" class="btn btn-danger">
                            <i class="bi bi-trash me-1"></i>Xóa
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</c:if>

<jsp:include page="../common/footer.jsp"/>
