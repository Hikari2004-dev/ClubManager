package com.clubmanager.dao;

import com.clubmanager.model.Event;
import com.clubmanager.model.Club;
import com.clubmanager.model.User;
import com.clubmanager.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Event
 */
public class EventDAO {
    
    /**
     * Tìm event theo ID
     */
    public Event findById(Long id) {
        String sql = "SELECT e.*, c.name as club_name, u.full_name as creator_name " +
                     "FROM events e " +
                     "INNER JOIN clubs c ON e.club_id = c.id " +
                     "INNER JOIN users u ON e.created_by = u.id " +
                     "WHERE e.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEvent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return null;
    }
    
    /**
     * Lấy tất cả events
     */
    public List<Event> findAll() {
        String sql = "SELECT e.*, c.name as club_name, u.full_name as creator_name " +
                     "FROM events e " +
                     "INNER JOIN clubs c ON e.club_id = c.id " +
                     "INNER JOIN users u ON e.created_by = u.id " +
                     "ORDER BY e.starts_at DESC";
        List<Event> events = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return events;
    }
    
    /**
     * Lấy events với phân trang
     */
    public List<Event> findAll(int page, int pageSize) {
        String sql = "SELECT e.*, c.name as club_name, u.full_name as creator_name " +
                     "FROM events e " +
                     "INNER JOIN clubs c ON e.club_id = c.id " +
                     "INNER JOIN users u ON e.created_by = u.id " +
                     "ORDER BY e.starts_at DESC LIMIT ? OFFSET ?";
        List<Event> events = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return events;
    }
    
    /**
     * Lấy events của một club
     */
    public List<Event> findByClubId(Long clubId) {
        String sql = "SELECT e.*, c.name as club_name, u.full_name as creator_name " +
                     "FROM events e " +
                     "INNER JOIN clubs c ON e.club_id = c.id " +
                     "INNER JOIN users u ON e.created_by = u.id " +
                     "WHERE e.club_id = ? ORDER BY e.starts_at DESC";
        List<Event> events = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return events;
    }
    
    /**
     * Lấy events sắp tới
     */
    public List<Event> findUpcoming(int limit) {
        String sql = "SELECT e.*, c.name as club_name, u.full_name as creator_name " +
                     "FROM events e " +
                     "INNER JOIN clubs c ON e.club_id = c.id " +
                     "INNER JOIN users u ON e.created_by = u.id " +
                     "WHERE e.starts_at > NOW() AND e.status = 'published' " +
                     "ORDER BY e.starts_at ASC LIMIT ?";
        List<Event> events = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return events;
    }
    
    /**
     * Tìm kiếm events
     */
    public List<Event> search(String keyword) {
        String sql = "SELECT e.*, c.name as club_name, u.full_name as creator_name " +
                     "FROM events e " +
                     "INNER JOIN clubs c ON e.club_id = c.id " +
                     "INNER JOIN users u ON e.created_by = u.id " +
                     "WHERE e.title LIKE ? OR e.description LIKE ? OR e.location LIKE ? " +
                     "ORDER BY e.starts_at DESC";
        List<Event> events = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return events;
    }
    
    /**
     * Đếm tổng số events
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM events";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return 0;
    }
    
    /**
     * Đếm events của club
     */
    public int countByClubId(Long clubId) {
        String sql = "SELECT COUNT(*) FROM events WHERE club_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return 0;
    }
    
    /**
     * Thêm event mới
     */
    public Long insert(Event event) {
        String sql = "INSERT INTO events (club_id, title, description, location, starts_at, ends_at, " +
                     "capacity, external_registration_enabled, external_registration_deadline, " +
                     "external_registration_requires_approval, status, created_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, event.getClubId());
            stmt.setString(2, event.getTitle());
            stmt.setString(3, event.getDescription());
            stmt.setString(4, event.getLocation());
            stmt.setTimestamp(5, Timestamp.valueOf(event.getStartsAt()));
            if (event.getEndsAt() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(event.getEndsAt()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }
            if (event.getCapacity() != null) {
                stmt.setInt(7, event.getCapacity());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            stmt.setBoolean(8, event.isExternalRegistrationEnabled());
            if (event.getExternalRegistrationDeadline() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(event.getExternalRegistrationDeadline()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }
            stmt.setBoolean(10, event.isExternalRegistrationRequiresApproval());
            stmt.setString(11, event.getStatus());
            stmt.setLong(12, event.getCreatedBy());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return null;
    }
    
    /**
     * Cập nhật event
     */
    public boolean update(Event event) {
        String sql = "UPDATE events SET title = ?, description = ?, location = ?, starts_at = ?, ends_at = ?, " +
                     "capacity = ?, external_registration_enabled = ?, external_registration_deadline = ?, " +
                     "external_registration_requires_approval = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, event.getTitle());
            stmt.setString(2, event.getDescription());
            stmt.setString(3, event.getLocation());
            stmt.setTimestamp(4, Timestamp.valueOf(event.getStartsAt()));
            if (event.getEndsAt() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(event.getEndsAt()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            if (event.getCapacity() != null) {
                stmt.setInt(6, event.getCapacity());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.setBoolean(7, event.isExternalRegistrationEnabled());
            if (event.getExternalRegistrationDeadline() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(event.getExternalRegistrationDeadline()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }
            stmt.setBoolean(9, event.isExternalRegistrationRequiresApproval());
            stmt.setString(10, event.getStatus());
            stmt.setLong(11, event.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Cập nhật status
     */
    public boolean updateStatus(Long eventId, String status) {
        String sql = "UPDATE events SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setLong(2, eventId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Xóa event
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM events WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Map ResultSet sang Event object
     */
    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setClubId(rs.getLong("club_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setLocation(rs.getString("location"));
        
        Timestamp startsAt = rs.getTimestamp("starts_at");
        if (startsAt != null) {
            event.setStartsAt(startsAt.toLocalDateTime());
        }
        
        Timestamp endsAt = rs.getTimestamp("ends_at");
        if (endsAt != null) {
            event.setEndsAt(endsAt.toLocalDateTime());
        }
        
        int capacity = rs.getInt("capacity");
        if (!rs.wasNull()) {
            event.setCapacity(capacity);
        }
        
        event.setExternalRegistrationEnabled(rs.getBoolean("external_registration_enabled"));
        
        Timestamp deadline = rs.getTimestamp("external_registration_deadline");
        if (deadline != null) {
            event.setExternalRegistrationDeadline(deadline.toLocalDateTime());
        }
        
        event.setExternalRegistrationRequiresApproval(rs.getBoolean("external_registration_requires_approval"));
        event.setStatus(rs.getString("status"));
        event.setCreatedBy(rs.getLong("created_by"));
        event.setCreatedAt(rs.getTimestamp("created_at"));
        
        // Map club info
        Club club = new Club();
        club.setId(event.getClubId());
        club.setName(rs.getString("club_name"));
        event.setClub(club);
        
        // Map creator info
        User creator = new User();
        creator.setId(event.getCreatedBy());
        creator.setFullName(rs.getString("creator_name"));
        event.setCreator(creator);
        
        return event;
    }
    
    /**
     * Kiểm tra user đã đăng ký sự kiện chưa
     */
    public boolean isUserRegistered(Long eventId, Long userId) {
        String sql = "SELECT 1 FROM event_registrations WHERE event_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, eventId);
            stmt.setLong(2, userId);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return false;
    }
    
    /**
     * Đếm số người đăng ký sự kiện
     */
    public int getRegistrationCount(Long eventId) {
        String sql = "SELECT COUNT(*) FROM event_registrations WHERE event_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, eventId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return 0;
    }
    
    /**
     * Đăng ký user tham gia sự kiện
     */
    public boolean registerUser(Long eventId, Long userId) {
        String sql = "INSERT INTO event_registrations (event_id, user_id, registered_at) VALUES (?, ?, NOW())";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, eventId);
            stmt.setLong(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Hủy đăng ký user khỏi sự kiện
     */
    public boolean unregisterUser(Long eventId, Long userId) {
        String sql = "DELETE FROM event_registrations WHERE event_id = ? AND user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, eventId);
            stmt.setLong(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Lấy danh sách user đã đăng ký sự kiện
     */
    public List<User> getRegisteredUsers(Long eventId) {
        String sql = "SELECT u.id, u.full_name, u.email, u.student_code, u.class_name, er.registered_at " +
                     "FROM event_registrations er " +
                     "INNER JOIN users u ON er.user_id = u.id " +
                     "WHERE er.event_id = ? ORDER BY er.registered_at";
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, eventId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setStudentCode(rs.getString("student_code"));
                user.setClassName(rs.getString("class_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return users;
    }
}
