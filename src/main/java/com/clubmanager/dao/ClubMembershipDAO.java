package com.clubmanager.dao;

import com.clubmanager.model.ClubMembership;
import com.clubmanager.model.User;
import com.clubmanager.model.Club;
import com.clubmanager.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho ClubMembership
 */
public class ClubMembershipDAO {
    
    /**
     * Tìm membership theo ID
     */
    public ClubMembership findById(Long id) {
        String sql = "SELECT cm.*, u.full_name, u.email, u.student_code, c.name as club_name " +
                     "FROM club_memberships cm " +
                     "INNER JOIN users u ON cm.user_id = u.id " +
                     "INNER JOIN clubs c ON cm.club_id = c.id " +
                     "WHERE cm.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMembership(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return null;
    }
    
    /**
     * Tìm membership theo club và user
     */
    public ClubMembership findByClubAndUser(Long clubId, Long userId) {
        String sql = "SELECT cm.*, u.full_name, u.email, u.student_code, c.name as club_name " +
                     "FROM club_memberships cm " +
                     "INNER JOIN users u ON cm.user_id = u.id " +
                     "INNER JOIN clubs c ON cm.club_id = c.id " +
                     "WHERE cm.club_id = ? AND cm.user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
            stmt.setLong(2, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMembership(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return null;
    }
    
    /**
     * Lấy tất cả members của một club
     */
    public List<ClubMembership> findByClubId(Long clubId) {
        String sql = "SELECT cm.*, u.full_name, u.email, u.student_code, u.class_name, c.name as club_name " +
                     "FROM club_memberships cm " +
                     "INNER JOIN users u ON cm.user_id = u.id " +
                     "INNER JOIN clubs c ON cm.club_id = c.id " +
                     "WHERE cm.club_id = ? ORDER BY cm.club_role, u.full_name";
        List<ClubMembership> memberships = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                memberships.add(mapResultSetToMembership(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return memberships;
    }
    
    /**
     * Lấy members của club theo status
     */
    public List<ClubMembership> findByClubIdAndStatus(Long clubId, String status) {
        String sql = "SELECT cm.*, u.full_name, u.email, u.student_code, u.class_name, c.name as club_name " +
                     "FROM club_memberships cm " +
                     "INNER JOIN users u ON cm.user_id = u.id " +
                     "INNER JOIN clubs c ON cm.club_id = c.id " +
                     "WHERE cm.club_id = ? AND cm.status = ? ORDER BY cm.requested_at DESC";
        List<ClubMembership> memberships = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
            stmt.setString(2, status);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                memberships.add(mapResultSetToMembership(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return memberships;
    }
    
    /**
     * Lấy tất cả memberships của một user
     */
    public List<ClubMembership> findByUserId(Long userId) {
        String sql = "SELECT cm.*, u.full_name, u.email, u.student_code, c.name as club_name " +
                     "FROM club_memberships cm " +
                     "INNER JOIN users u ON cm.user_id = u.id " +
                     "INNER JOIN clubs c ON cm.club_id = c.id " +
                     "WHERE cm.user_id = ? ORDER BY c.name";
        List<ClubMembership> memberships = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                memberships.add(mapResultSetToMembership(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return memberships;
    }
    
    /**
     * Đếm số members của club theo status
     */
    public int countByClubIdAndStatus(Long clubId, String status) {
        String sql = "SELECT COUNT(*) FROM club_memberships WHERE club_id = ? AND status = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
            stmt.setString(2, status);
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
     * Thêm membership mới (đăng ký tham gia club)
     */
    public Long insert(ClubMembership membership) {
        String sql = "INSERT INTO club_memberships (club_id, user_id, club_role, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, membership.getClubId());
            stmt.setLong(2, membership.getUserId());
            stmt.setString(3, membership.getClubRole());
            stmt.setString(4, membership.getStatus());
            
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
     * Duyệt membership
     */
    public boolean approve(Long membershipId, Long approvedBy) {
        String sql = "UPDATE club_memberships SET status = 'approved', approved_by = ?, approved_at = NOW() WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, approvedBy);
            stmt.setLong(2, membershipId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Từ chối membership
     */
    public boolean reject(Long membershipId, Long approvedBy, String reason) {
        String sql = "UPDATE club_memberships SET status = 'rejected', approved_by = ?, approved_at = NOW(), rejected_reason = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, approvedBy);
            stmt.setString(2, reason);
            stmt.setLong(3, membershipId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Cập nhật role trong club
     */
    public boolean updateClubRole(Long membershipId, String clubRole) {
        String sql = "UPDATE club_memberships SET club_role = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, clubRole);
            stmt.setLong(2, membershipId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Rời khỏi club
     */
    public boolean leave(Long membershipId) {
        String sql = "UPDATE club_memberships SET status = 'left', left_at = NOW() WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, membershipId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Xóa membership
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM club_memberships WHERE id = ?";
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
     * Kiểm tra user có phải là chủ nhiệm (president) của club không
     */
    public boolean isClubLeader(Long clubId, Long userId) {
        String sql = "SELECT 1 FROM club_memberships WHERE club_id = ? AND user_id = ? AND club_role = 'president' AND status = 'approved'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
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
     * Kiểm tra user có phải là thành viên approved của club không
     */
    public boolean isMember(Long clubId, Long userId) {
        String sql = "SELECT 1 FROM club_memberships WHERE club_id = ? AND user_id = ? AND status = 'approved'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
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
     * Map ResultSet sang ClubMembership object
     */
    private ClubMembership mapResultSetToMembership(ResultSet rs) throws SQLException {
        ClubMembership membership = new ClubMembership();
        membership.setId(rs.getLong("id"));
        membership.setClubId(rs.getLong("club_id"));
        membership.setUserId(rs.getLong("user_id"));
        membership.setClubRole(rs.getString("club_role"));
        membership.setStatus(rs.getString("status"));
        membership.setRequestedAt(rs.getTimestamp("requested_at"));
        
        Long approvedBy = rs.getLong("approved_by");
        if (!rs.wasNull()) {
            membership.setApprovedBy(approvedBy);
        }
        membership.setApprovedAt(rs.getTimestamp("approved_at"));
        membership.setRejectedReason(rs.getString("rejected_reason"));
        membership.setLeftAt(rs.getTimestamp("left_at"));
        
        // Map user info
        User user = new User();
        user.setId(membership.getUserId());
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setStudentCode(rs.getString("student_code"));
        try {
            user.setClassName(rs.getString("class_name"));
        } catch (SQLException ignored) {}
        membership.setUser(user);
        
        // Map club info
        Club club = new Club();
        club.setId(membership.getClubId());
        club.setName(rs.getString("club_name"));
        membership.setClub(club);
        
        return membership;
    }
    
    /**
     * Kiểm tra user có phải là chủ nhiệm hoặc phó chủ nhiệm của club không
     * Dùng để kiểm tra quyền tạo/sửa/xóa event
     */
    public boolean isClubManager(Long clubId, Long userId) {
        String sql = "SELECT 1 FROM club_memberships WHERE club_id = ? AND user_id = ? AND club_role IN ('president', 'vice_president') AND status = 'approved'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, clubId);
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
}
