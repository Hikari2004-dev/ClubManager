<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Club Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #4e73df 0%, #224abe 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            padding: 2rem 0;
        }
        .register-card {
            border: none;
            border-radius: 1rem;
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.15);
        }
        .register-header {
            background: linear-gradient(90deg, #4e73df, #224abe);
            color: white;
            border-radius: 1rem 1rem 0 0;
            padding: 2rem;
            text-align: center;
        }
        .form-control:focus {
            border-color: #4e73df;
            box-shadow: 0 0 0 0.2rem rgba(78, 115, 223, 0.25);
        }
        .btn-register {
            background: linear-gradient(90deg, #4e73df, #224abe);
            border: none;
        }
        .btn-register:hover {
            background: linear-gradient(90deg, #224abe, #1a3a8a);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card register-card">
                    <div class="register-header">
                        <i class="bi bi-person-plus-fill display-4 mb-2"></i>
                        <h3 class="mb-0">Đăng ký tài khoản</h3>
                        <p class="mb-0 opacity-75">Tham gia cộng đồng Club Manager</p>
                    </div>
                    <div class="card-body p-4">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="bi bi-exclamation-circle me-2"></i>${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>
                        
                        <form action="${pageContext.request.contextPath}/register" method="post">
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="fullName" class="form-label">
                                        Họ và tên <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="fullName" name="fullName" 
                                           value="${fullName}" required placeholder="">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="username" class="form-label">
                                        Tên đăng nhập <span class="text-danger">*</span>
                                    </label>
                                    <input type="text" class="form-control" id="username" name="username" 
                                           value="${username}" required placeholder="">
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="${email}" placeholder="">
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="password" class="form-label">
                                        Mật khẩu <span class="text-danger">*</span>
                                    </label>
                                    <input type="password" class="form-control" id="password" name="password" 
                                           required minlength="6" placeholder="">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="confirmPassword" class="form-label">
                                        Xác nhận mật khẩu <span class="text-danger">*</span>
                                    </label>
                                    <input type="password" class="form-control" id="confirmPassword" 
                                           name="confirmPassword" required placeholder="">
                                </div>
                            </div>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="phone" class="form-label">Số điện thoại</label>
                                    <input type="tel" class="form-control" id="phone" name="phone" 
                                           value="${phone}" placeholder="">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="studentCode" class="form-label">Mã sinh viên <small class="text-muted">(Tùy chọn)</small></label>
                                    <input type="text" class="form-control" id="studentCode" name="studentCode" 
                                           value="${studentCode}" placeholder="">
                                </div>
                            </div>
                            
                            <div class="mb-4">
                                <label for="className" class="form-label">Lớp <small class="text-muted">(Tùy chọn)</small></label>
                                <input type="text" class="form-control" id="className" name="className" 
                                       value="${className}" placeholder="">
                            </div>
                            
                            <button type="submit" class="btn btn-register btn-primary btn-lg w-100 mb-3">
                                <i class="bi bi-person-plus me-2"></i>Đăng ký
                            </button>
                        </form>
                        
                        <hr>
                        
                        <div class="text-center">
                            <p class="mb-0">Đã có tài khoản? 
                                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/" class="text-white text-decoration-none">
                        <i class="bi bi-arrow-left me-1"></i>Quay về trang chủ
                    </a>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Validate password match
        document.getElementById('confirmPassword').addEventListener('input', function() {
            var password = document.getElementById('password').value;
            if (this.value !== password) {
                this.setCustomValidity('Mật khẩu xác nhận không khớp');
            } else {
                this.setCustomValidity('');
            }
        });
    </script>
</body>
</html>
