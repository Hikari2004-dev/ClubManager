package com.clubmanager.servlet;

import com.clubmanager.dao.*;
import com.clubmanager.model.*;
import com.clubmanager.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet quản lý Users (Admin)
 */
@WebServlet(urlPatterns = {"/admin/users", "/admin/users/*"})
public class AdminUserServlet extends HttpServlet {
    
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
        
        // Kiểm tra quyền admin
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listUsers(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.matches("/\\d+")) {
            viewUser(request, response, Long.parseLong(pathInfo.substring(1)));
        } else if (pathInfo.matches("/\\d+/edit")) {
            showEditForm(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else if (pathInfo.equals("/create")) {
            createUser(request, response);
        } else if (pathInfo.matches("/\\d+/edit")) {
            updateUser(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/delete")) {
            deleteUser(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/toggle-active")) {
            toggleActive(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/roles")) {
            updateRoles(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return false;
        
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return false;
        
        return userDAO.hasRole(currentUser.getId(), Role.ADMIN);
    }
    
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        String pageStr = request.getParameter("page");
        int page = pageStr != null ? Integer.parseInt(pageStr) : 1;
        int pageSize = 20;
        
        List<User> users;
        int totalUsers;
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            users = userDAO.search(keyword.trim());
            totalUsers = users.size();
        } else {
            users = userDAO.findAll(page, pageSize);
            totalUsers = userDAO.count();
        }
        
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);
        
        request.setAttribute("users", users);
        request.setAttribute("keyword", keyword);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalUsers", totalUsers);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/users/list.jsp").forward(request, response);
    }
    
    private void viewUser(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        User user = userDAO.findById(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        List<Role> userRoles = userDAO.getUserRoles(userId);
        List<Role> allRoles = roleDAO.findAll();
        
        request.setAttribute("user", user);
        request.setAttribute("userRoles", userRoles);
        request.setAttribute("allRoles", allRoles);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/users/view.jsp").forward(request, response);
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Role> roles = roleDAO.findAll();
        request.setAttribute("roles", roles);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/users/form.jsp").forward(request, response);
    }
    
    private void createUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String className = request.getParameter("className");
        String studentCode = request.getParameter("studentCode");
        String[] roleIds = request.getParameterValues("roleIds");
        
        // Validate
        if (fullName == null || fullName.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin bắt buộc");
            showCreateForm(request, response);
            return;
        }
        
        if (userDAO.findByUsername(username.trim()) != null) {
            request.setAttribute("error", "Tên đăng nhập đã tồn tại");
            showCreateForm(request, response);
            return;
        }
        
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email != null && !email.isEmpty() ? email.trim() : null);
        user.setUsername(username.trim());
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setPhone(phone != null && !phone.isEmpty() ? phone.trim() : null);
        user.setClassName(className != null && !className.isEmpty() ? className.trim() : null);
        user.setStudentCode(studentCode != null && !studentCode.isEmpty() ? studentCode.trim() : null);
        user.setActive(true);
        
        Long userId = userDAO.insert(user);
        
        if (userId != null) {
            // Gán roles
            if (roleIds != null) {
                for (String roleId : roleIds) {
                    userDAO.assignRole(userId, Long.parseLong(roleId));
                }
            }
            
            response.sendRedirect(request.getContextPath() + "/admin/users/" + userId);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi tạo người dùng");
            showCreateForm(request, response);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        User user = userDAO.findById(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        List<Role> roles = roleDAO.findAll();
        List<Role> userRoles = userDAO.getUserRoles(userId);
        
        request.setAttribute("user", user);
        request.setAttribute("roles", roles);
        request.setAttribute("userRoles", userRoles);
        request.setAttribute("isEdit", true);
        
        request.getRequestDispatcher("/WEB-INF/views/admin/users/form.jsp").forward(request, response);
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        User user = userDAO.findById(userId);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String className = request.getParameter("className");
        String studentCode = request.getParameter("studentCode");
        String isActive = request.getParameter("isActive");
        
        user.setFullName(fullName.trim());
        user.setEmail(email != null && !email.isEmpty() ? email.trim() : null);
        user.setUsername(username.trim());
        user.setPhone(phone != null && !phone.isEmpty() ? phone.trim() : null);
        user.setClassName(className != null && !className.isEmpty() ? className.trim() : null);
        user.setStudentCode(studentCode != null && !studentCode.isEmpty() ? studentCode.trim() : null);
        user.setActive("on".equals(isActive) || "true".equals(isActive));
        
        userDAO.update(user);
        
        // Cập nhật password nếu có
        if (password != null && !password.isEmpty()) {
            userDAO.updatePassword(userId, PasswordUtil.hashPassword(password));
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users/" + userId);
    }
    
    private void deleteUser(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Không cho xóa chính mình
        if (currentUser.getId().equals(userId)) {
            session.setAttribute("error", "Không thể xóa tài khoản của chính mình");
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }
        
        userDAO.delete(userId);
        session.setAttribute("success", "Đã xóa người dùng");
        
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }
    
    private void toggleActive(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        User user = userDAO.findById(userId);
        if (user != null) {
            user.setActive(!user.isActive());
            userDAO.update(user);
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/users/" + userId);
    }
    
    private void updateRoles(HttpServletRequest request, HttpServletResponse response, Long userId)
            throws ServletException, IOException {
        
        String[] roleIds = request.getParameterValues("roleIds");
        List<Role> currentRoles = userDAO.getUserRoles(userId);
        
        // Xóa tất cả roles hiện tại
        for (Role role : currentRoles) {
            userDAO.removeRole(userId, role.getId());
        }
        
        // Thêm roles mới
        if (roleIds != null) {
            for (String roleId : roleIds) {
                userDAO.assignRole(userId, Long.parseLong(roleId));
            }
        }
        
        HttpSession session = request.getSession();
        session.setAttribute("success", "Đã cập nhật vai trò");
        
        response.sendRedirect(request.getContextPath() + "/admin/users/" + userId);
    }
}
