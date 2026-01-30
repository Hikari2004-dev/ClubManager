package com.clubmanager.servlet;

import com.clubmanager.dao.ClubMembershipDAO;
import com.clubmanager.dao.UserDAO;
import com.clubmanager.model.ClubMembership;
import com.clubmanager.model.User;
import com.clubmanager.util.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile", "/profile/*"})
public class ProfileServlet extends HttpServlet {
    
    private UserDAO userDAO;
    private ClubMembershipDAO membershipDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        membershipDAO = new ClubMembershipDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            // Get user's club memberships
            List<ClubMembership> memberships = membershipDAO.findByUserId(currentUser.getId());
            request.setAttribute("memberships", memberships);
            
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải thông tin hồ sơ");
            request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo != null && pathInfo.equals("/password")) {
                handleChangePassword(request, response, currentUser);
            } else {
                handleUpdateProfile(request, response, currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Đã xảy ra lỗi khi cập nhật thông tin");
            response.sendRedirect(request.getContextPath() + "/profile");
        }
    }
    
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws Exception {
        String fullName = request.getParameter("fullName");
        String studentCode = request.getParameter("studentCode");
        String phone = request.getParameter("phone");
        String className = request.getParameter("className");
        
        // Validate
        if (fullName == null || fullName.trim().isEmpty()) {
            request.getSession().setAttribute("error", "Họ tên không được để trống");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        // Update user
        currentUser.setFullName(fullName.trim());
        currentUser.setStudentCode(studentCode != null && !studentCode.trim().isEmpty() ? studentCode.trim() : null);
        currentUser.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);
        currentUser.setClassName(className != null && !className.trim().isEmpty() ? className.trim() : null);
        
        userDAO.update(currentUser);
        
        // Update session
        request.getSession().setAttribute("currentUser", currentUser);
        request.getSession().setAttribute("success", "Cập nhật thông tin thành công");
        response.sendRedirect(request.getContextPath() + "/profile");
    }
    
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response, User currentUser) 
            throws Exception {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmNewPassword = request.getParameter("confirmNewPassword");
        
        // Validate
        if (currentPassword == null || currentPassword.isEmpty()) {
            request.getSession().setAttribute("error", "Vui lòng nhập mật khẩu hiện tại");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        if (newPassword == null || newPassword.length() < 6) {
            request.getSession().setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        if (!newPassword.equals(confirmNewPassword)) {
            request.getSession().setAttribute("error", "Mật khẩu xác nhận không khớp");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        // Verify current password
        User dbUser = userDAO.findById(currentUser.getId());
        if (!PasswordUtil.checkPassword(currentPassword, dbUser.getPasswordHash())) {
            request.getSession().setAttribute("error", "Mật khẩu hiện tại không đúng");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        
        // Update password
        String newPasswordHash = PasswordUtil.hashPassword(newPassword);
        currentUser.setPasswordHash(newPasswordHash);
        userDAO.update(currentUser);
        
        request.getSession().setAttribute("success", "Đổi mật khẩu thành công");
        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
