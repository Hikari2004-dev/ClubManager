package com.clubmanager.servlet;

import com.clubmanager.dao.*;
import com.clubmanager.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet quản lý Clubs
 */
@WebServlet(urlPatterns = {"/clubs", "/clubs/*"})
public class ClubServlet extends HttpServlet {
    
    private ClubDAO clubDAO;
    private ClubMembershipDAO membershipDAO;
    private UserDAO userDAO;
    private EventDAO eventDAO;
    
    @Override
    public void init() throws ServletException {
        clubDAO = new ClubDAO();
        membershipDAO = new ClubMembershipDAO();
        userDAO = new UserDAO();
        eventDAO = new EventDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listClubs(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.matches("/\\d+")) {
            viewClub(request, response, Long.parseLong(pathInfo.substring(1)));
        } else if (pathInfo.matches("/\\d+/edit")) {
            showEditForm(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/members")) {
            listMembers(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/join")) {
            joinClub(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else if (pathInfo.equals("/create")) {
            createClub(request, response);
        } else if (pathInfo.matches("/\\d+/edit")) {
            updateClub(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/delete")) {
            deleteClub(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/join")) {
            processJoinClub(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/leave")) {
            leaveClub(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/members/\\d+/approve")) {
            approveMember(request, response, pathInfo);
        } else if (pathInfo.matches("/\\d+/members/\\d+/reject")) {
            rejectMember(request, response, pathInfo);
        } else if (pathInfo.matches("/\\d+/members/\\d+/remove")) {
            removeMember(request, response, pathInfo);
        } else if (pathInfo.matches("/\\d+/members/\\d+/role")) {
            updateMemberRole(request, response, pathInfo);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void listClubs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        String category = request.getParameter("category");
        
        List<Club> clubs;
        if (keyword != null && !keyword.trim().isEmpty()) {
            clubs = clubDAO.search(keyword.trim());
        } else if (category != null && !category.trim().isEmpty()) {
            clubs = clubDAO.findByCategory(category.trim());
        } else {
            clubs = clubDAO.findAll();
        }
        
        List<String> categories = clubDAO.getCategories();
        
        // Kiểm tra quyền tạo club (chỉ Admin hoặc Teacher)
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        boolean canCreateClub = false;
        if (currentUser != null) {
            canCreateClub = userDAO.hasRole(currentUser.getId(), Role.ADMIN) ||
                           userDAO.hasRole(currentUser.getId(), Role.TEACHER);
        }
        
        request.setAttribute("clubs", clubs);
        request.setAttribute("categories", categories);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedCategory", category);
        request.setAttribute("canCreateClub", canCreateClub);
        
        request.getRequestDispatcher("/WEB-INF/views/clubs/list.jsp").forward(request, response);
    }
    
    private void viewClub(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        Club club = clubDAO.findById(clubId);
        if (club == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy câu lạc bộ");
            return;
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Lấy danh sách members approved
        List<ClubMembership> members = membershipDAO.findByClubIdAndStatus(clubId, ClubMembership.STATUS_APPROVED);
        
        // Lấy events của club
        List<Event> events = eventDAO.findByClubId(clubId);
        
        // Kiểm tra user có phải là member không
        ClubMembership userMembership = null;
        if (currentUser != null) {
            userMembership = membershipDAO.findByClubAndUser(clubId, currentUser.getId());
        }
        
        // Đếm số pending requests
        int pendingCount = membershipDAO.countByClubIdAndStatus(clubId, ClubMembership.STATUS_PENDING);
        
        request.setAttribute("club", club);
        request.setAttribute("members", members);
        request.setAttribute("events", events);
        request.setAttribute("userMembership", userMembership);
        request.setAttribute("pendingCount", pendingCount);
        
        // Kiểm tra quyền quản lý
        boolean canManage = false;
        if (currentUser != null) {
            canManage = membershipDAO.isClubLeader(clubId, currentUser.getId()) ||
                       userDAO.hasRole(currentUser.getId(), Role.ADMIN);
        }
        request.setAttribute("canManage", canManage);
        
        request.getRequestDispatcher("/WEB-INF/views/clubs/view.jsp").forward(request, response);
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Lấy danh sách teachers để chọn supervisor
        List<User> teachers = userDAO.findByRole(Role.TEACHER);
        request.setAttribute("teachers", teachers);
        
        List<String> categories = clubDAO.getCategories();
        request.setAttribute("categories", categories);
        
        request.getRequestDispatcher("/WEB-INF/views/clubs/form.jsp").forward(request, response);
    }
    
    private void createClub(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String supervisorIdStr = request.getParameter("supervisorUserId");
        
        // Validate
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên câu lạc bộ không được để trống");
            showCreateForm(request, response);
            return;
        }
        
        Club club = new Club();
        club.setName(name.trim());
        club.setDescription(description);
        club.setCategory(category);
        if (supervisorIdStr != null && !supervisorIdStr.isEmpty()) {
            club.setSupervisorUserId(Long.parseLong(supervisorIdStr));
        }
        club.setStatus(Club.STATUS_ACTIVE);
        
        Long clubId = clubDAO.insert(club);
        
        if (clubId != null) {
            // Thêm người tạo làm chủ nhiệm
            ClubMembership membership = new ClubMembership();
            membership.setClubId(clubId);
            membership.setUserId(currentUser.getId());
            membership.setClubRole(ClubMembership.ROLE_PRESIDENT);
            membership.setStatus(ClubMembership.STATUS_APPROVED);
            membershipDAO.insert(membership);
            
            // Approve membership ngay
            ClubMembership created = membershipDAO.findByClubAndUser(clubId, currentUser.getId());
            if (created != null) {
                membershipDAO.approve(created.getId(), currentUser.getId());
            }
            
            response.sendRedirect(request.getContextPath() + "/clubs/" + clubId);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi tạo câu lạc bộ");
            showCreateForm(request, response);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        Club club = clubDAO.findById(clubId);
        if (club == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Kiểm tra quyền
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        boolean canManage = membershipDAO.isClubLeader(clubId, currentUser.getId()) ||
                           userDAO.hasRole(currentUser.getId(), Role.ADMIN);
        
        if (!canManage) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền chỉnh sửa");
            return;
        }
        
        List<User> teachers = userDAO.findByRole(Role.TEACHER);
        List<String> categories = clubDAO.getCategories();
        
        request.setAttribute("club", club);
        request.setAttribute("teachers", teachers);
        request.setAttribute("categories", categories);
        request.setAttribute("isEdit", true);
        
        request.getRequestDispatcher("/WEB-INF/views/clubs/form.jsp").forward(request, response);
    }
    
    private void updateClub(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        Club club = clubDAO.findById(clubId);
        if (club == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String supervisorIdStr = request.getParameter("supervisorUserId");
        String status = request.getParameter("status");
        
        club.setName(name.trim());
        club.setDescription(description);
        club.setCategory(category);
        if (supervisorIdStr != null && !supervisorIdStr.isEmpty()) {
            club.setSupervisorUserId(Long.parseLong(supervisorIdStr));
        } else {
            club.setSupervisorUserId(null);
        }
        if (status != null) {
            club.setStatus(status);
        }
        
        if (clubDAO.update(club)) {
            response.sendRedirect(request.getContextPath() + "/clubs/" + clubId);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật");
            request.setAttribute("club", club);
            request.getRequestDispatcher("/WEB-INF/views/clubs/form.jsp").forward(request, response);
        }
    }
    
    private void deleteClub(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Chỉ admin mới được xóa club
        if (!userDAO.hasRole(currentUser.getId(), Role.ADMIN)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        clubDAO.delete(clubId);
        response.sendRedirect(request.getContextPath() + "/clubs");
    }
    
    private void joinClub(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        // GET request - hiển thị confirm page hoặc redirect với message
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId);
    }
    
    private void processJoinClub(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Kiểm tra đã là member chưa
        ClubMembership existing = membershipDAO.findByClubAndUser(clubId, currentUser.getId());
        if (existing != null) {
            if (ClubMembership.STATUS_PENDING.equals(existing.getStatus())) {
                session.setAttribute("message", "Bạn đã gửi yêu cầu tham gia, vui lòng chờ duyệt");
            } else if (ClubMembership.STATUS_APPROVED.equals(existing.getStatus())) {
                session.setAttribute("message", "Bạn đã là thành viên của câu lạc bộ này");
            }
            response.sendRedirect(request.getContextPath() + "/clubs/" + clubId);
            return;
        }
        
        // Tạo membership mới
        ClubMembership membership = new ClubMembership();
        membership.setClubId(clubId);
        membership.setUserId(currentUser.getId());
        membership.setClubRole(ClubMembership.ROLE_MEMBER);
        membership.setStatus(ClubMembership.STATUS_PENDING);
        
        membershipDAO.insert(membership);
        session.setAttribute("success", "Đã gửi yêu cầu tham gia, vui lòng chờ duyệt");
        
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId);
    }
    
    private void leaveClub(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        ClubMembership membership = membershipDAO.findByClubAndUser(clubId, currentUser.getId());
        if (membership != null) {
            // Không cho chủ nhiệm rời nếu chưa chuyển giao
            if (ClubMembership.ROLE_PRESIDENT.equals(membership.getClubRole())) {
                session.setAttribute("error", "Bạn cần chuyển giao vị trí chủ nhiệm trước khi rời khỏi câu lạc bộ");
            } else {
                membershipDAO.leave(membership.getId());
                session.setAttribute("success", "Bạn đã rời khỏi câu lạc bộ");
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId);
    }
    
    private void listMembers(HttpServletRequest request, HttpServletResponse response, Long clubId)
            throws ServletException, IOException {
        
        Club club = clubDAO.findById(clubId);
        if (club == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        String status = request.getParameter("status");
        List<ClubMembership> members;
        
        if (status != null && !status.isEmpty()) {
            members = membershipDAO.findByClubIdAndStatus(clubId, status);
        } else {
            members = membershipDAO.findByClubId(clubId);
        }
        
        request.setAttribute("club", club);
        request.setAttribute("members", members);
        request.setAttribute("selectedStatus", status);
        
        // Kiểm tra quyền quản lý
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        boolean canManage = membershipDAO.isClubLeader(clubId, currentUser.getId()) ||
                           userDAO.hasRole(currentUser.getId(), Role.ADMIN);
        request.setAttribute("canManage", canManage);
        
        request.getRequestDispatcher("/WEB-INF/views/clubs/members.jsp").forward(request, response);
    }
    
    private void approveMember(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        
        String[] parts = pathInfo.split("/");
        Long clubId = Long.parseLong(parts[1]);
        Long membershipId = Long.parseLong(parts[3]);
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        membershipDAO.approve(membershipId, currentUser.getId());
        session.setAttribute("success", "Đã duyệt thành viên");
        
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId + "/members?status=pending");
    }
    
    private void rejectMember(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        
        String[] parts = pathInfo.split("/");
        Long clubId = Long.parseLong(parts[1]);
        Long membershipId = Long.parseLong(parts[3]);
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        String reason = request.getParameter("reason");
        
        membershipDAO.reject(membershipId, currentUser.getId(), reason);
        session.setAttribute("success", "Đã từ chối yêu cầu");
        
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId + "/members?status=pending");
    }
    
    private void removeMember(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        
        String[] parts = pathInfo.split("/");
        Long clubId = Long.parseLong(parts[1]);
        Long membershipId = Long.parseLong(parts[3]);
        
        HttpSession session = request.getSession();
        
        membershipDAO.delete(membershipId);
        session.setAttribute("success", "Đã xóa thành viên");
        
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId + "/members");
    }
    
    private void updateMemberRole(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        
        String[] parts = pathInfo.split("/");
        Long clubId = Long.parseLong(parts[1]);
        Long membershipId = Long.parseLong(parts[3]);
        String newRole = request.getParameter("role");
        
        HttpSession session = request.getSession();
        
        if (newRole != null && !newRole.isEmpty()) {
            membershipDAO.updateClubRole(membershipId, newRole);
            session.setAttribute("success", "Đã cập nhật vai trò thành viên");
        }
        
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId + "/members");
    }
}
