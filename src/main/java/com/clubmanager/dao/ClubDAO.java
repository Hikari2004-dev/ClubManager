package com.clubmanager.dao;

import com.clubmanager.model.Club;
import com.clubmanager.model.User;
import com.clubmanager.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Club
 */
public class ClubDAO {
    
    /**
     * Tìm club theo ID
     */
    public Club findById(Long id) {
        String sql = "SELECT c.*, u.full_name as supervisor_name FROM clubs c " +
                     "LEFT JOIN users u ON c.supervisor_user_id = u.id WHERE c.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Club club = mapResultSetToClub(rs);
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                return club;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return null;
    }
    
    /**
     * Lấy tất cả clubs
     */
    public List<Club> findAll() {
        String sql = "SELECT c.*, u.full_name as supervisor_name, " +
                     "(SELECT COUNT(*) FROM club_memberships cm WHERE cm.club_id = c.id AND cm.status = 'approved') as member_count " +
                     "FROM clubs c LEFT JOIN users u ON c.supervisor_user_id = u.id ORDER BY c.name";
        List<Club> clubs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Club club = mapResultSetToClub(rs);
                club.setMemberCount(rs.getInt("member_count"));
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return clubs;
    }
    
    /**
     * Lấy clubs với phân trang
     */
    public List<Club> findAll(int page, int pageSize) {
        String sql = "SELECT c.*, u.full_name as supervisor_name, " +
                     "(SELECT COUNT(*) FROM club_memberships cm WHERE cm.club_id = c.id AND cm.status = 'approved') as member_count " +
                     "FROM clubs c LEFT JOIN users u ON c.supervisor_user_id = u.id ORDER BY c.name LIMIT ? OFFSET ?";
        List<Club> clubs = new ArrayList<>();
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
                Club club = mapResultSetToClub(rs);
                club.setMemberCount(rs.getInt("member_count"));
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return clubs;
    }
    
    /**
     * Lấy clubs theo status
     */
    public List<Club> findByStatus(String status) {
        String sql = "SELECT c.*, u.full_name as supervisor_name, " +
                     "(SELECT COUNT(*) FROM club_memberships cm WHERE cm.club_id = c.id AND cm.status = 'approved') as member_count " +
                     "FROM clubs c LEFT JOIN users u ON c.supervisor_user_id = u.id WHERE c.status = ? ORDER BY c.name";
        List<Club> clubs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Club club = mapResultSetToClub(rs);
                club.setMemberCount(rs.getInt("member_count"));
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return clubs;
    }
    
    /**
     * Lấy clubs theo category
     */
    public List<Club> findByCategory(String category) {
        String sql = "SELECT c.*, u.full_name as supervisor_name, " +
                     "(SELECT COUNT(*) FROM club_memberships cm WHERE cm.club_id = c.id AND cm.status = 'approved') as member_count " +
                     "FROM clubs c LEFT JOIN users u ON c.supervisor_user_id = u.id WHERE c.category = ? ORDER BY c.name";
        List<Club> clubs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Club club = mapResultSetToClub(rs);
                club.setMemberCount(rs.getInt("member_count"));
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return clubs;
    }
    
    /**
     * Tìm kiếm clubs
     */
    public List<Club> search(String keyword) {
        String sql = "SELECT c.*, u.full_name as supervisor_name, " +
                     "(SELECT COUNT(*) FROM club_memberships cm WHERE cm.club_id = c.id AND cm.status = 'approved') as member_count " +
                     "FROM clubs c LEFT JOIN users u ON c.supervisor_user_id = u.id " +
                     "WHERE c.name LIKE ? OR c.description LIKE ? OR c.category LIKE ? ORDER BY c.name";
        List<Club> clubs = new ArrayList<>();
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
                Club club = mapResultSetToClub(rs);
                club.setMemberCount(rs.getInt("member_count"));
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return clubs;
    }
    
    /**
     * Đếm tổng số clubs
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM clubs";
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
     * Lấy danh sách categories
     */
    public List<String> getCategories() {
        String sql = "SELECT DISTINCT category FROM clubs WHERE category IS NOT NULL ORDER BY category";
        List<String> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return categories;
    }
    
    /**
     * Thêm club mới
     */
    public Long insert(Club club) {
        String sql = "INSERT INTO clubs (name, description, category, supervisor_user_id, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, club.getName());
            stmt.setString(2, club.getDescription());
            stmt.setString(3, club.getCategory());
            if (club.getSupervisorUserId() != null) {
                stmt.setLong(4, club.getSupervisorUserId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            stmt.setString(5, club.getStatus());
            
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
     * Cập nhật club
     */
    public boolean update(Club club) {
        String sql = "UPDATE clubs SET name = ?, description = ?, category = ?, supervisor_user_id = ?, status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, club.getName());
            stmt.setString(2, club.getDescription());
            stmt.setString(3, club.getCategory());
            if (club.getSupervisorUserId() != null) {
                stmt.setLong(4, club.getSupervisorUserId());
            } else {
                stmt.setNull(4, Types.BIGINT);
            }
            stmt.setString(5, club.getStatus());
            stmt.setLong(6, club.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, null);
        }
        return false;
    }
    
    /**
     * Xóa club
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM clubs WHERE id = ?";
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
     * Lấy clubs mà user là thành viên
     */
    public List<Club> findByUserId(Long userId) {
        String sql = "SELECT c.*, u.full_name as supervisor_name, cm.club_role, cm.status as membership_status " +
                     "FROM clubs c " +
                     "INNER JOIN club_memberships cm ON c.id = cm.club_id " +
                     "LEFT JOIN users u ON c.supervisor_user_id = u.id " +
                     "WHERE cm.user_id = ? AND cm.status = 'approved' ORDER BY c.name";
        List<Club> clubs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Club club = mapResultSetToClub(rs);
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return clubs;
    }
    
    /**
     * Lấy clubs mà user là chủ nhiệm hoặc phó chủ nhiệm (có quyền quản lý event)
     */
    public List<Club> findManagedByUser(Long userId) {
        String sql = "SELECT c.*, u.full_name as supervisor_name " +
                     "FROM clubs c " +
                     "INNER JOIN club_memberships cm ON c.id = cm.club_id " +
                     "LEFT JOIN users u ON c.supervisor_user_id = u.id " +
                     "WHERE cm.user_id = ? AND cm.club_role IN ('president', 'vice_president') " +
                     "AND cm.status = 'approved' ORDER BY c.name";
        List<Club> clubs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, userId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Club club = mapResultSetToClub(rs);
                if (rs.getString("supervisor_name") != null) {
                    User supervisor = new User();
                    supervisor.setId(club.getSupervisorUserId());
                    supervisor.setFullName(rs.getString("supervisor_name"));
                    club.setSupervisor(supervisor);
                }
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeAll(conn, stmt, rs);
        }
        return clubs;
    }
    
    /**
     * Map ResultSet sang Club object
     */
    private Club mapResultSetToClub(ResultSet rs) throws SQLException {
        Club club = new Club();
        club.setId(rs.getLong("id"));
        club.setName(rs.getString("name"));
        club.setDescription(rs.getString("description"));
        club.setCategory(rs.getString("category"));
        Long supervisorId = rs.getLong("supervisor_user_id");
        if (!rs.wasNull()) {
            club.setSupervisorUserId(supervisorId);
        }
        club.setStatus(rs.getString("status"));
        club.setCreatedAt(rs.getTimestamp("created_at"));
        return club;
    }
}
