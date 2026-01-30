package com.clubmanager.filter;

import com.clubmanager.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter kiểm tra authentication
 */
@WebFilter(urlPatterns = {"/admin/*", "/clubs", "/clubs/*", "/events", "/events/*", "/profile", "/profile/*", "/dashboard"})
public class AuthFilter implements Filter {
    
    // Các đường dẫn cho phép khách truy cập (không cần đăng nhập)
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/clubs",
        "/events"
    );
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());
        
        // Kiểm tra nếu là đường dẫn công khai (xem danh sách và chi tiết clubs/events)
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpSession session = httpRequest.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;
        
        if (currentUser == null) {
            // Lưu URL hiện tại để redirect sau khi login
            String queryString = httpRequest.getQueryString();
            String redirectUrl = requestURI + (queryString != null ? "?" + queryString : "");
            
            httpRequest.getSession().setAttribute("redirectUrl", redirectUrl);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        
        // User đã đăng nhập, cho phép tiếp tục
        chain.doFilter(request, response);
    }
    
    /**
     * Kiểm tra đường dẫn có phải là công khai không
     * Cho phép: /clubs, /clubs/{id}, /events, /events/{id}
     * Không cho phép: /clubs/create, /clubs/{id}/edit, /events/create, ...
     */
    private boolean isPublicPath(String path) {
        // /clubs hoặc /events (danh sách)
        if (path.equals("/clubs") || path.equals("/events")) {
            return true;
        }
        
        // /clubs/{id} hoặc /events/{id} (chi tiết) - chỉ số, không có action khác
        if (path.matches("/clubs/\\d+") || path.matches("/events/\\d+")) {
            return true;
        }
        
        return false;
    }
    
    @Override
    public void destroy() {
    }
}
