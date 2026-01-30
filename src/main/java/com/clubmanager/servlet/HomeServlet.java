package com.clubmanager.servlet;

import com.clubmanager.dao.*;
import com.clubmanager.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet hiển thị trang chủ (public - không cần đăng nhập)
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {
    
    private ClubDAO clubDAO;
    private EventDAO eventDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        clubDAO = new ClubDAO();
        eventDAO = new EventDAO();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy danh sách clubs active
        List<Club> clubs = clubDAO.findByStatus(Club.STATUS_ACTIVE);
        request.setAttribute("clubs", clubs);
        
        // Lấy events sắp tới
        List<Event> upcomingEvents = eventDAO.findUpcoming(5);
        request.setAttribute("upcomingEvents", upcomingEvents);
        
        // Thống kê
        request.setAttribute("totalClubs", clubDAO.count());
        request.setAttribute("totalUsers", userDAO.count());
        request.setAttribute("totalEvents", eventDAO.count());
        
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
