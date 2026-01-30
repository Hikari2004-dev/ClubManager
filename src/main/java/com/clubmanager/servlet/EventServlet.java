package com.clubmanager.servlet;

import com.clubmanager.dao.*;
import com.clubmanager.model.*;
import com.clubmanager.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servlet quản lý Events
 */
@WebServlet(urlPatterns = {"/events", "/events/*"})
public class EventServlet extends HttpServlet {
    
    private EventDAO eventDAO;
    private ClubDAO clubDAO;
    private ClubMembershipDAO membershipDAO;
    private UserDAO userDAO;
    
    @Override
    public void init() throws ServletException {
        eventDAO = new EventDAO();
        clubDAO = new ClubDAO();
        membershipDAO = new ClubMembershipDAO();
        userDAO = new UserDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            listEvents(request, response);
        } else if (pathInfo.equals("/create")) {
            showCreateForm(request, response);
        } else if (pathInfo.matches("/\\d+")) {
            viewEvent(request, response, Long.parseLong(pathInfo.substring(1)));
        } else if (pathInfo.matches("/\\d+/edit")) {
            showEditForm(request, response, Long.parseLong(pathInfo.split("/")[1]));
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
            createEvent(request, response);
        } else if (pathInfo.matches("/\\d+/edit")) {
            updateEvent(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/delete")) {
            deleteEvent(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/cancel")) {
            cancelEvent(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/register")) {
            registerForEvent(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else if (pathInfo.matches("/\\d+/unregister")) {
            unregisterFromEvent(request, response, Long.parseLong(pathInfo.split("/")[1]));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void listEvents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        String clubIdStr = request.getParameter("clubId");
        
        List<Event> events;
        if (keyword != null && !keyword.trim().isEmpty()) {
            events = eventDAO.search(keyword.trim());
        } else if (clubIdStr != null && !clubIdStr.isEmpty()) {
            events = eventDAO.findByClubId(Long.parseLong(clubIdStr));
        } else {
            events = eventDAO.findAll();
        }
        
        List<Club> clubs = clubDAO.findAll();
        
        // Kiểm tra quyền tạo event (chủ nhiệm/phó chủ nhiệm của ít nhất 1 club hoặc admin)
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        boolean canCreateEvent = false;
        if (currentUser != null) {
            if (userDAO.hasRole(currentUser.getId(), Role.ADMIN)) {
                canCreateEvent = true;
            } else {
                List<Club> managedClubs = clubDAO.findManagedByUser(currentUser.getId());
                canCreateEvent = managedClubs != null && !managedClubs.isEmpty();
            }
        }
        
        request.setAttribute("events", events);
        request.setAttribute("clubs", clubs);
        request.setAttribute("keyword", keyword);
        request.setAttribute("selectedClubId", clubIdStr);
        request.setAttribute("canCreateEvent", canCreateEvent);
        
        request.getRequestDispatcher("/WEB-INF/views/events/list.jsp").forward(request, response);
    }
    
    private void viewEvent(HttpServletRequest request, HttpServletResponse response, Long eventId)
            throws ServletException, IOException {
        
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sự kiện");
            return;
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Kiểm tra quyền quản lý (chỉ chủ nhiệm hoặc phó chủ nhiệm)
        boolean canManage = false;
        boolean isMember = false;
        boolean isRegistered = false;
        
        if (currentUser != null) {
            canManage = membershipDAO.isClubManager(event.getClubId(), currentUser.getId()) ||
                       userDAO.hasRole(currentUser.getId(), Role.ADMIN);
            isMember = membershipDAO.isMember(event.getClubId(), currentUser.getId());
            isRegistered = eventDAO.isUserRegistered(eventId, currentUser.getId());
        }
        
        // Lấy số người đã đăng ký
        int registrationCount = eventDAO.getRegistrationCount(eventId);
        
        // Lấy danh sách người đăng ký (nếu là quản lý)
        List<User> registeredUsers = null;
        if (canManage) {
            registeredUsers = eventDAO.getRegisteredUsers(eventId);
        }
        
        request.setAttribute("event", event);
        request.setAttribute("canManage", canManage);
        request.setAttribute("isMember", isMember);
        request.setAttribute("isRegistered", isRegistered);
        request.setAttribute("registrationCount", registrationCount);
        request.setAttribute("registeredUsers", registeredUsers);
        
        request.getRequestDispatcher("/WEB-INF/views/events/view.jsp").forward(request, response);
    }
    
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Lấy clubs mà user có quyền tạo event (là chủ nhiệm hoặc phó chủ nhiệm)
        List<Club> clubs;
        if (userDAO.hasRole(currentUser.getId(), Role.ADMIN)) {
            clubs = clubDAO.findAll();
        } else {
            clubs = clubDAO.findManagedByUser(currentUser.getId());
        }
        
        // Nếu không có club nào để quản lý
        if (clubs == null || clubs.isEmpty()) {
            request.setAttribute("error", "Bạn cần là chủ nhiệm hoặc phó chủ nhiệm của một câu lạc bộ để tạo sự kiện");
            response.sendRedirect(request.getContextPath() + "/events");
            return;
        }
        
        String clubIdStr = request.getParameter("clubId");
        
        request.setAttribute("clubs", clubs);
        request.setAttribute("selectedClubId", clubIdStr);
        
        request.getRequestDispatcher("/WEB-INF/views/events/form.jsp").forward(request, response);
    }
    
    private void createEvent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        String clubIdStr = request.getParameter("clubId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String startsAtStr = request.getParameter("startsAt");
        String endsAtStr = request.getParameter("endsAt");
        String capacityStr = request.getParameter("capacity");
        String externalRegistration = request.getParameter("externalRegistrationEnabled");
        String externalDeadlineStr = request.getParameter("externalRegistrationDeadline");
        String externalApproval = request.getParameter("externalRegistrationRequiresApproval");
        
        // Validate
        StringBuilder errors = new StringBuilder();
        if (clubIdStr == null || clubIdStr.isEmpty()) {
            errors.append("Vui lòng chọn câu lạc bộ. ");
        } else {
            // Kiểm tra quyền tạo event cho club này
            Long clubId = Long.parseLong(clubIdStr);
            if (!membershipDAO.isClubManager(clubId, currentUser.getId()) && 
                !userDAO.hasRole(currentUser.getId(), Role.ADMIN)) {
                errors.append("Bạn không có quyền tạo sự kiện cho câu lạc bộ này. ");
            }
        }
        if (title == null || title.trim().isEmpty()) {
            errors.append("Tiêu đề không được để trống. ");
        }
        if (startsAtStr == null || startsAtStr.isEmpty()) {
            errors.append("Thời gian bắt đầu không được để trống. ");
        }
        
        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString());
            showCreateForm(request, response);
            return;
        }
        
        Event event = new Event();
        event.setClubId(Long.parseLong(clubIdStr));
        event.setTitle(title.trim());
        event.setDescription(description);
        event.setLocation(location);
        event.setStartsAt(DateUtil.parseHtmlDateTime(startsAtStr));
        if (endsAtStr != null && !endsAtStr.isEmpty()) {
            event.setEndsAt(DateUtil.parseHtmlDateTime(endsAtStr));
        }
        if (capacityStr != null && !capacityStr.isEmpty()) {
            event.setCapacity(Integer.parseInt(capacityStr));
        }
        event.setExternalRegistrationEnabled("on".equals(externalRegistration));
        if (externalDeadlineStr != null && !externalDeadlineStr.isEmpty()) {
            event.setExternalRegistrationDeadline(DateUtil.parseHtmlDateTime(externalDeadlineStr));
        }
        event.setExternalRegistrationRequiresApproval("on".equals(externalApproval));
        event.setStatus(Event.STATUS_PUBLISHED);
        event.setCreatedBy(currentUser.getId());
        
        Long eventId = eventDAO.insert(event);
        
        if (eventId != null) {
            response.sendRedirect(request.getContextPath() + "/events/" + eventId);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi tạo sự kiện");
            showCreateForm(request, response);
        }
    }
    
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, Long eventId)
            throws ServletException, IOException {
        
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Kiểm tra quyền (chỉ chủ nhiệm hoặc phó chủ nhiệm)
        boolean canManage = membershipDAO.isClubManager(event.getClubId(), currentUser.getId()) ||
                           userDAO.hasRole(currentUser.getId(), Role.ADMIN);
        
        if (!canManage) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ chủ nhiệm và phó chủ nhiệm mới có quyền chỉnh sửa sự kiện");
            return;
        }
        
        List<Club> clubs;
        if (userDAO.hasRole(currentUser.getId(), Role.ADMIN)) {
            clubs = clubDAO.findAll();
        } else {
            clubs = clubDAO.findByUserId(currentUser.getId());
        }
        
        request.setAttribute("event", event);
        request.setAttribute("clubs", clubs);
        request.setAttribute("isEdit", true);
        
        request.getRequestDispatcher("/WEB-INF/views/events/form.jsp").forward(request, response);
    }
    
    private void updateEvent(HttpServletRequest request, HttpServletResponse response, Long eventId)
            throws ServletException, IOException {
        
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        String startsAtStr = request.getParameter("startsAt");
        String endsAtStr = request.getParameter("endsAt");
        String capacityStr = request.getParameter("capacity");
        String externalRegistration = request.getParameter("externalRegistrationEnabled");
        String externalDeadlineStr = request.getParameter("externalRegistrationDeadline");
        String externalApproval = request.getParameter("externalRegistrationRequiresApproval");
        String status = request.getParameter("status");
        
        event.setTitle(title.trim());
        event.setDescription(description);
        event.setLocation(location);
        event.setStartsAt(DateUtil.parseHtmlDateTime(startsAtStr));
        if (endsAtStr != null && !endsAtStr.isEmpty()) {
            event.setEndsAt(DateUtil.parseHtmlDateTime(endsAtStr));
        } else {
            event.setEndsAt(null);
        }
        if (capacityStr != null && !capacityStr.isEmpty()) {
            event.setCapacity(Integer.parseInt(capacityStr));
        } else {
            event.setCapacity(null);
        }
        event.setExternalRegistrationEnabled("on".equals(externalRegistration));
        if (externalDeadlineStr != null && !externalDeadlineStr.isEmpty()) {
            event.setExternalRegistrationDeadline(DateUtil.parseHtmlDateTime(externalDeadlineStr));
        } else {
            event.setExternalRegistrationDeadline(null);
        }
        event.setExternalRegistrationRequiresApproval("on".equals(externalApproval));
        if (status != null) {
            event.setStatus(status);
        }
        
        if (eventDAO.update(event)) {
            response.sendRedirect(request.getContextPath() + "/events/" + eventId);
        } else {
            request.setAttribute("error", "Có lỗi xảy ra khi cập nhật");
            request.setAttribute("event", event);
            request.getRequestDispatcher("/WEB-INF/views/events/form.jsp").forward(request, response);
        }
    }
    
    private void deleteEvent(HttpServletRequest request, HttpServletResponse response, Long eventId)
            throws ServletException, IOException {
        
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Kiểm tra quyền (chỉ chủ nhiệm hoặc phó chủ nhiệm)
        if (!membershipDAO.isClubManager(event.getClubId(), currentUser.getId()) &&
            !userDAO.hasRole(currentUser.getId(), Role.ADMIN)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ chủ nhiệm và phó chủ nhiệm mới có quyền xóa sự kiện");
            return;
        }
        
        Long clubId = event.getClubId();
        eventDAO.delete(eventId);
        
        response.sendRedirect(request.getContextPath() + "/clubs/" + clubId);
    }
    
    private void cancelEvent(HttpServletRequest request, HttpServletResponse response, Long eventId)
            throws ServletException, IOException {
        
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Kiểm tra quyền (chỉ chủ nhiệm hoặc phó chủ nhiệm)
        if (!membershipDAO.isClubManager(event.getClubId(), currentUser.getId()) &&
            !userDAO.hasRole(currentUser.getId(), Role.ADMIN)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ chủ nhiệm và phó chủ nhiệm mới có quyền hủy sự kiện");
            return;
        }
        
        eventDAO.updateStatus(eventId, Event.STATUS_CANCELLED);
        response.sendRedirect(request.getContextPath() + "/events/" + eventId);
    }
    
    private void registerForEvent(HttpServletRequest request, HttpServletResponse response, Long eventId)
            throws ServletException, IOException {
        
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        // Kiểm tra event đang published
        if (!Event.STATUS_PUBLISHED.equals(event.getStatus())) {
            request.getSession().setAttribute("error", "Sự kiện không còn nhận đăng ký");
            response.sendRedirect(request.getContextPath() + "/events/" + eventId);
            return;
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Kiểm tra user là thành viên của CLB tổ chức sự kiện
        if (!membershipDAO.isMember(event.getClubId(), currentUser.getId())) {
            session.setAttribute("error", "Bạn cần là thành viên của câu lạc bộ để đăng ký sự kiện này");
            response.sendRedirect(request.getContextPath() + "/events/" + eventId);
            return;
        }
        
        // Kiểm tra đã đăng ký chưa
        if (eventDAO.isUserRegistered(eventId, currentUser.getId())) {
            session.setAttribute("error", "Bạn đã đăng ký sự kiện này rồi");
            response.sendRedirect(request.getContextPath() + "/events/" + eventId);
            return;
        }
        
        // Kiểm tra còn chỗ không (nếu có giới hạn)
        if (event.getCapacity() != null && event.getCapacity() > 0) {
            int currentCount = eventDAO.getRegistrationCount(eventId);
            if (currentCount >= event.getCapacity()) {
                session.setAttribute("error", "Sự kiện đã đủ số lượng đăng ký");
                response.sendRedirect(request.getContextPath() + "/events/" + eventId);
                return;
            }
        }
        
        // Đăng ký
        if (eventDAO.registerUser(eventId, currentUser.getId())) {
            session.setAttribute("success", "Đăng ký tham gia sự kiện thành công!");
        } else {
            session.setAttribute("error", "Có lỗi xảy ra khi đăng ký");
        }
        
        response.sendRedirect(request.getContextPath() + "/events/" + eventId);
    }
    
    private void unregisterFromEvent(HttpServletRequest request, HttpServletResponse response, Long eventId)
            throws ServletException, IOException {
        
        Event event = eventDAO.findById(eventId);
        if (event == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        
        // Hủy đăng ký
        if (eventDAO.unregisterUser(eventId, currentUser.getId())) {
            session.setAttribute("success", "Đã hủy đăng ký sự kiện");
        } else {
            session.setAttribute("error", "Có lỗi xảy ra khi hủy đăng ký");
        }
        
        response.sendRedirect(request.getContextPath() + "/events/" + eventId);
    }
}
