<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="${isEdit ? 'Chỉnh sửa CLB' : 'Tạo CLB mới'}"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <nav aria-label="breadcrumb" class="mb-3">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/clubs">Câu lạc bộ</a></li>
                <c:if test="${isEdit}">
                    <li class="breadcrumb-item">
                        <a href="${pageContext.request.contextPath}/clubs/${club.id}">${club.name}</a>
                    </li>
                </c:if>
                <li class="breadcrumb-item active">${isEdit ? 'Chỉnh sửa' : 'Tạo mới'}</li>
            </ol>
        </nav>
        
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-header">
                        <h4 class="mb-0">
                            <i class="bi bi-${isEdit ? 'pencil' : 'plus-circle'} me-2"></i>
                            ${isEdit ? 'Chỉnh sửa câu lạc bộ' : 'Tạo câu lạc bộ mới'}
                        </h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                <i class="bi bi-exclamation-circle me-2"></i>${error}
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/clubs/${isEdit ? club.id.toString().concat('/edit') : 'create'}" 
                              method="post">
                            
                            <div class="mb-3">
                                <label for="name" class="form-label">
                                    Tên câu lạc bộ <span class="text-danger">*</span>
                                </label>
                                <input type="text" class="form-control" id="name" name="name" 
                                       value="${club.name}" required maxlength="120"
                                       placeholder="Nhập tên câu lạc bộ">
                            </div>
                            
                            <div class="mb-3">
                                <label for="category" class="form-label">Danh mục</label>
                                <input type="text" class="form-control" id="category" name="category" 
                                       value="${club.category}" maxlength="60" list="categoryList"
                                       placeholder="Ví dụ: Học thuật, Thể thao, Nghệ thuật...">
                                <datalist id="categoryList">
                                    <c:forEach var="cat" items="${categories}">
                                        <option value="${cat}">
                                    </c:forEach>
                                </datalist>
                            </div>
                            
                            <div class="mb-3">
                                <label for="description" class="form-label">Mô tả</label>
                                <textarea class="form-control" id="description" name="description" 
                                          rows="5" placeholder="Giới thiệu về câu lạc bộ...">${club.description}</textarea>
                            </div>
                            
                            <div class="mb-3">
                                <label for="supervisorUserId" class="form-label">Giáo viên phụ trách</label>
                                <select class="form-select" id="supervisorUserId" name="supervisorUserId">
                                    <option value="">-- Chọn giáo viên --</option>
                                    <c:forEach var="teacher" items="${teachers}">
                                        <option value="${teacher.id}" 
                                                ${club.supervisorUserId == teacher.id ? 'selected' : ''}>
                                            ${teacher.fullName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                            
                            <c:if test="${isEdit}">
                                <div class="mb-3">
                                    <label for="status" class="form-label">Trạng thái</label>
                                    <select class="form-select" id="status" name="status">
                                        <option value="active" ${club.status == 'active' ? 'selected' : ''}>
                                            Hoạt động
                                        </option>
                                        <option value="inactive" ${club.status == 'inactive' ? 'selected' : ''}>
                                            Tạm ngưng
                                        </option>
                                        <option value="suspended" ${club.status == 'suspended' ? 'selected' : ''}>
                                            Đình chỉ
                                        </option>
                                    </select>
                                </div>
                            </c:if>
                            
                            <hr>
                            
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/clubs${isEdit ? '/'.concat(club.id) : ''}" 
                                   class="btn btn-secondary">
                                    <i class="bi bi-arrow-left me-1"></i>Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-${isEdit ? 'check' : 'plus-circle'} me-1"></i>
                                    ${isEdit ? 'Cập nhật' : 'Tạo câu lạc bộ'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="../common/footer.jsp"/>
