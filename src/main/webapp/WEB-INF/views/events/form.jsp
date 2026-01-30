<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${empty event ? 'Tạo sự kiện mới' : 'Chỉnh sửa sự kiện'}"/>
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
                <c:if test="${not empty event}">
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/events/${event.id}">${event.title}</a>
                    </li>
                </c:if>
                <li class="breadcrumb-item active">
                    ${empty event ? 'Tạo sự kiện mới' : 'Chỉnh sửa'}
                </li>
            </ol>
        </nav>
        
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">
                            <i class="bi bi-calendar-plus me-2"></i>
                            ${empty event ? 'Tạo sự kiện mới' : 'Chỉnh sửa sự kiện'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <form action="${pageContext.request.contextPath}/events${empty event ? '/create' : '/'.concat(event.id).concat('/edit')}" 
                              method="post" class="needs-validation" novalidate>
                            
                            <div class="mb-3">
                                <label for="title" class="form-label">Tên sự kiện *</label>
                                <input type="text" class="form-control" id="title" name="title" 
                                       value="${event.title}" required maxlength="200"
                                       placeholder="Nhập tên sự kiện">
                                <div class="invalid-feedback">Vui lòng nhập tên sự kiện</div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="clubId" class="form-label">Câu lạc bộ *</label>
                                <select class="form-select" id="clubId" name="clubId" required>
                                    <option value="">-- Chọn câu lạc bộ --</option>
                                    <c:forEach var="club" items="${clubs}">
                                        <option value="${club.id}" ${club.id == event.clubId ? 'selected' : ''}>
                                            ${club.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <div class="invalid-feedback">Vui lòng chọn câu lạc bộ</div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="description" class="form-label">Mô tả</label>
                                <textarea class="form-control" id="description" name="description" 
                                          rows="4" placeholder="Mô tả chi tiết về sự kiện...">${event.description}</textarea>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="startsAt" class="form-label">Thời gian bắt đầu *</label>
                                    <input type="datetime-local" class="form-control" id="startsAt" 
                                           name="startsAt" required
                                           value="${not empty event.startsAt ? event.startsAt.toString().substring(0, 16) : ''}">
                                    <div class="invalid-feedback">Vui lòng chọn thời gian bắt đầu</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="endsAt" class="form-label">Thời gian kết thúc</label>
                                    <input type="datetime-local" class="form-control" id="endsAt" 
                                           name="endsAt"
                                           value="${not empty event.endsAt ? event.endsAt.toString().substring(0, 16) : ''}">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-8 mb-3">
                                    <label for="location" class="form-label">Địa điểm</label>
                                    <input type="text" class="form-control" id="location" name="location" 
                                           value="${event.location}" maxlength="300"
                                           placeholder="Ví dụ: Hội trường A, Tầng 2">
                                </div>
                                <div class="col-md-4 mb-3">
                                    <label for="capacity" class="form-label">Số lượng tối đa</label>
                                    <input type="number" class="form-control" id="capacity" name="capacity" 
                                           value="${event.capacity}" min="1"
                                           placeholder="Không giới hạn">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="status" class="form-label">Trạng thái</label>
                                    <select class="form-select" id="status" name="status">
                                        <option value="draft" ${event.status == 'draft' || empty event.status ? 'selected' : ''}>
                                            Bản nháp
                                        </option>
                                        <option value="published" ${event.status == 'published' ? 'selected' : ''}>
                                            Đã xuất bản
                                        </option>
                                        <option value="cancelled" ${event.status == 'cancelled' ? 'selected' : ''}>
                                            Đã hủy
                                        </option>
                                        <option value="completed" ${event.status == 'completed' ? 'selected' : ''}>
                                            Đã hoàn thành
                                        </option>
                                    </select>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label d-block">Tùy chọn</label>
                                    <div class="form-check mt-2">
                                        <input class="form-check-input" type="checkbox" id="allowExternal" 
                                               name="allowExternal" ${event.allowExternal ? 'checked' : ''}>
                                        <label class="form-check-label" for="allowExternal">
                                            Cho phép đăng ký từ bên ngoài
                                        </label>
                                    </div>
                                </div>
                            </div>
                            
                            <hr class="my-4">
                            
                            <div class="d-flex justify-content-between">
                                <a href="${empty event ? pageContext.request.contextPath.concat('/events') : 
                                          pageContext.request.contextPath.concat('/events/').concat(event.id)}" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left me-1"></i>Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-check-lg me-1"></i>
                                    ${empty event ? 'Tạo sự kiện' : 'Lưu thay đổi'}
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
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
    
    // Validate end time > start time
    var startsAt = document.getElementById('startsAt');
    var endsAt = document.getElementById('endsAt');
    
    endsAt.addEventListener('change', function() {
        if (startsAt.value && endsAt.value && endsAt.value < startsAt.value) {
            endsAt.setCustomValidity('Thời gian kết thúc phải sau thời gian bắt đầu');
        } else {
            endsAt.setCustomValidity('');
        }
    });
})();
</script>

<jsp:include page="../common/footer.jsp"/>
