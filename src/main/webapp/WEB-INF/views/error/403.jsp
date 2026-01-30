<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>403 - Không có quyền truy cập | Club Manager</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        .error-container {
            text-align: center;
            color: white;
        }
        .error-code {
            font-size: 150px;
            font-weight: bold;
            line-height: 1;
            text-shadow: 4px 4px 0 rgba(0,0,0,0.1);
        }
        .error-message {
            font-size: 24px;
            margin-bottom: 30px;
        }
        .btn-home {
            background: white;
            color: #f5576c;
            border: none;
            padding: 12px 30px;
            font-size: 18px;
            border-radius: 50px;
            transition: all 0.3s;
            margin: 0 10px;
        }
        .btn-home:hover {
            transform: translateY(-3px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.2);
            background: white;
            color: #f093fb;
        }
        .btn-login {
            background: transparent;
            color: white;
            border: 2px solid white;
        }
        .btn-login:hover {
            background: white;
            color: #f5576c;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-code">403</div>
        <div class="error-message">
            <i class="bi bi-shield-lock me-2"></i>
            Không có quyền truy cập
        </div>
        <p class="mb-4 opacity-75">
            Bạn không có quyền truy cập vào trang này. Vui lòng liên hệ quản trị viên nếu bạn cho rằng đây là lỗi.
        </p>
        <div>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-home btn-login">
                <i class="bi bi-box-arrow-in-right me-2"></i>Đăng nhập
            </a>
            <a href="${pageContext.request.contextPath}/" class="btn btn-home">
                <i class="bi bi-house me-2"></i>Về trang chủ
            </a>
        </div>
    </div>
</body>
</html>
