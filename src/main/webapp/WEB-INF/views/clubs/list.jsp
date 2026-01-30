<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp">
    <jsp:param name="title" value="Danh sách câu lạc bộ"/>
</jsp:include>

<main class="main-content">
    <div class="container py-4">
        <jsp:include page="../common/alerts.jsp"/>
        
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1 class="h3 mb-0">
                <i class="bi bi-people me-2"></i>Danh sách câu lạc bộ
            </h1>
            <c:if test="${canCreateClub}">
                <a href="${pageContext.request.contextPath}/clubs/create" class="btn btn-primary">
                    <i class="bi bi-plus-circle me-1"></i>Tạo CLB mới
                </a>
            </c:if>
        </div>
        
        <!-- Search & Filter -->
        <div class="card mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/clubs" method="get" class="row g-3">
                    <div class="col-md-6">
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-search"></i></span>
                            <input type="text" class="form-control" name="keyword" 
                                   value="${keyword}" placeholder="Tìm kiếm câu lạc bộ...">
                        </div>
                    </div>
                    <div class="col-md-4">
                        <select class="form-select" name="category">
                            <option value="">-- Tất cả danh mục --</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat}" ${cat == selectedCategory ? 'selected' : ''}>
                                    ${cat}
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
        
        <!-- Clubs List -->
        <div class="row g-4">
            <c:forEach var="club" items="${clubs}">
                <div class="col-md-6 col-lg-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <h5 class="card-title mb-0">
                                    <a href="${pageContext.request.contextPath}/clubs/${club.id}" 
                                       class="text-decoration-none">
                                        ${club.name}
                                    </a>
                                </h5>
                                <span class="badge bg-${club.status == 'active' ? 'success' : 'secondary'}">
                                    ${club.status == 'active' ? 'Hoạt động' : club.status}
                                </span>
                            </div>
                            
                            <c:if test="${not empty club.category}">
                                <span class="badge bg-primary mb-2">${club.category}</span>
                            </c:if>
                            
                            <p class="card-text text-muted">
                                <c:choose>
                                    <c:when test="${not empty club.description}">
                                        ${club.description.length() > 150 ? 
                                          club.description.substring(0, 150).concat('...') : 
                                          club.description}
                                    </c:when>
                                    <c:otherwise>
                                        <em>Chưa có mô tả</em>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            
                            <c:if test="${not empty club.supervisor}">
                                <p class="mb-0">
                                    <small class="text-muted">
                                        <i class="bi bi-person-badge me-1"></i>
                                        GV phụ trách: ${club.supervisor.fullName}
                                    </small>
                                </p>
                            </c:if>
                        </div>
                        <div class="card-footer bg-transparent d-flex justify-content-between align-items-center">
                            <small class="text-muted">
                                <i class="bi bi-people me-1"></i>${club.memberCount} thành viên
                            </small>
                            <a href="${pageContext.request.contextPath}/clubs/${club.id}" 
                               class="btn btn-sm btn-outline-primary">
                                Xem chi tiết
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty clubs}">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body text-center py-5">
                            <i class="bi bi-inbox display-1 text-muted"></i>
                            <h5 class="mt-3">Không tìm thấy câu lạc bộ nào</h5>
                            <p class="text-muted">
                                <c:choose>
                                    <c:when test="${not empty keyword}">
                                        Không có kết quả cho từ khóa "${keyword}"
                                    </c:when>
                                    <c:otherwise>
                                        Chưa có câu lạc bộ nào được tạo
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
