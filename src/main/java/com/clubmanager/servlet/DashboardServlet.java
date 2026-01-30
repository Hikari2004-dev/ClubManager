package com.clubmanager.servlet;

import com.clubmanager.dao.*;
import com.clubmanager.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet hiển thị Dashboard (cần đăng nhập)
 */
@WebServlet(urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    
    private ClubDAO clubDAO;
    private EventDAO eventDAO;
    private UserDAO userDAO;
    private ClubMembershipDAO membershipDAO;
    
    @Override
    public void init() throws ServletException {
        clubDAO = new ClubDAO();
        eventDAO = new EventDAO();
        userDAO = new UserDAO();
        membershipDAO = new ClubMembershipDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        showDashboard(request, response);
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Lấy clubs mà user là thành viên
        List<Club> myClubs = clubDAO.findByUserId(currentUser.getId());
        request.setAttribute("myClubs", myClubs);
        
        // Lấy memberships đang chờ duyệt của user
        List<ClubMembership> pendingMemberships = membershipDAO.findByUserId(currentUser.getId());
        request.setAttribute("myMemberships", pendingMemberships);
        
        // Lấy events sắp tới
        List<Event> upcomingEvents = eventDAO.findUpcoming(10);
        request.setAttribute("upcomingEvents", upcomingEvents);
        
        // Kiểm tra roles
        @SuppressWarnings("unchecked")
        List<Role> roles = (List<Role>) session.getAttribute("userRoles");
        boolean isAdmin = roles != null && roles.stream().anyMatch(r -> Role.ADMIN.equals(r.getName()));
        boolean isTeacher = roles != null && roles.stream().anyMatch(r -> Role.TEACHER.equals(r.getName()));
        
        request.setAttribute("isAdmin", isAdmin);
        request.setAttribute("isTeacher", isTeacher);
        
        // Nếu là admin hoặc teacher, thêm thống kê
        if (isAdmin || isTeacher) {
            request.setAttribute("totalClubs", clubDAO.count());
            request.setAttribute("totalUsers", userDAO.count());
            request.setAttribute("totalEvents", eventDAO.count());
        }
        
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
