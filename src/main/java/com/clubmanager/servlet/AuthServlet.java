package com.clubmanager.servlet;

import com.clubmanager.dao.UserDAO;
import com.clubmanager.dao.RoleDAO;
import com.clubmanager.model.User;
import com.clubmanager.model.Role;
import com.clubmanager.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet xử lý đăng nhập, đăng xuất, đăng ký
 */
@WebServlet(urlPatterns = {"/login", "/logout", "/register"})
public class AuthServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private RoleDAO roleDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        roleDAO = new RoleDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/login":
                showLoginForm(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            case "/register":
                showRegisterForm(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/login");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        
        switch (path) {
            case "/login":
                processLogin(request, response);
                break;
            case "/register":
                processRegister(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/login");
        }
    }
    
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Nếu đã đăng nhập thì redirect về dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }
    
    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }
        
        // Tìm user
        User user = userDAO.findByUsername(username.trim());
        if (user == null) {
            user = userDAO.findByEmail(username.trim());
        }
        
        // Kiểm tra user và password
        if (user == null || !PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra user có active không
        if (!user.isActive()) {
            request.setAttribute("error", "Tài khoản đã bị khóa");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }
        
        // Lấy roles của user
        List<Role> roles = userDAO.getUserRoles(user.getId());
        
        // Tạo session
        HttpSession session = request.getSession(true);
        session.setAttribute("currentUser", user);
        session.setAttribute("userRoles", roles);
        session.setMaxInactiveInterval(30 * 60); // 30 phút
        
        // Remember me - tạo cookie
        if ("on".equals(remember)) {
            Cookie userCookie = new Cookie("remember_user", user.getUsername());
            userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
            userCookie.setPath("/");
            response.addCookie(userCookie);
        }
        
        // Redirect
        String redirectUrl = (String) session.getAttribute("redirectUrl");
        if (redirectUrl != null) {
            session.removeAttribute("redirectUrl");
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        // Xóa cookie remember
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_user".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
    }
    
    private void processRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phone = request.getParameter("phone");
        String className = request.getParameter("className");
        String studentCode = request.getParameter("studentCode");
        
        // Validate
        StringBuilder errors = new StringBuilder();
        
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.append("Họ tên không được để trống. ");
        }
        if (username == null || username.trim().isEmpty()) {
            errors.append("Tên đăng nhập không được để trống. ");
        } else if (userDAO.findByUsername(username.trim()) != null) {
            errors.append("Tên đăng nhập đã tồn tại. ");
        }
        if (email != null && !email.trim().isEmpty() && userDAO.findByEmail(email.trim()) != null) {
            errors.append("Email đã tồn tại. ");
        }
        if (password == null || password.length() < 6) {
            errors.append("Mật khẩu phải có ít nhất 6 ký tự. ");
        }
        if (!password.equals(confirmPassword)) {
            errors.append("Xác nhận mật khẩu không khớp. ");
        }
        
        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("username", username);
            request.setAttribute("phone", phone);
            request.setAttribute("className", className);
            request.setAttribute("studentCode", studentCode);
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        
        // Tạo user mới
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email != null && !email.trim().isEmpty() ? email.trim() : null);
        user.setUsername(username.trim());
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);
        user.setClassName(className != null && !className.trim().isEmpty() ? className.trim() : null);
        user.setStudentCode(studentCode != null && !studentCode.trim().isEmpty() ? studentCode.trim() : null);
        user.setActive(true);
        
        Long userId = userDAO.insert(user);
        
        if (userId != null) {
            // Gán role member mặc định
            Role memberRole = roleDAO.findByName(Role.MEMBER);
            if (memberRole != null) {
                userDAO.assignRole(userId, memberRole.getId());
            }
            
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
        }
    }
}
