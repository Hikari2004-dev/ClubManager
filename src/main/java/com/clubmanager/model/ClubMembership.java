package com.clubmanager.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng club_memberships
 */
public class ClubMembership implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Club roles (khớp với database)
    public static final String ROLE_PRESIDENT = "president";
    public static final String ROLE_VICE_PRESIDENT = "vice_president";
    public static final String ROLE_SECRETARY = "secretary";
    public static final String ROLE_MEMBER = "member";
    
    // Status
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_LEFT = "left";
    
    private Long id;
    private Long clubId;
    private Long userId;
    private String clubRole;
    private String status;
    private Timestamp requestedAt;
    private Long approvedBy;
    private Timestamp approvedAt;
    private String rejectedReason;
    private Timestamp leftAt;
    
    // Thông tin bổ sung từ join
    private User user;
    private Club club;
    private User approver;
    
    public ClubMembership() {
        this.clubRole = ROLE_MEMBER;
        this.status = STATUS_PENDING;
    }
    
    // Getters và Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getClubId() {
        return clubId;
    }
    
    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getClubRole() {
        return clubRole;
    }
    
    public void setClubRole(String clubRole) {
        this.clubRole = clubRole;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getRequestedAt() {
        return requestedAt;
    }
    
    public void setRequestedAt(Timestamp requestedAt) {
        this.requestedAt = requestedAt;
    }
    
    public Long getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public Timestamp getApprovedAt() {
        return approvedAt;
    }
    
    public void setApprovedAt(Timestamp approvedAt) {
        this.approvedAt = approvedAt;
    }
    
    public String getRejectedReason() {
        return rejectedReason;
    }
    
    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }
    
    public Timestamp getLeftAt() {
        return leftAt;
    }
    
    public void setLeftAt(Timestamp leftAt) {
        this.leftAt = leftAt;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Club getClub() {
        return club;
    }
    
    public void setClub(Club club) {
        this.club = club;
    }
    
    public User getApprover() {
        return approver;
    }
    
    public void setApprover(User approver) {
        this.approver = approver;
    }
    
    public String getClubRoleDisplayName() {
        if (clubRole == null) return "Thành viên";
        switch (clubRole) {
            case ROLE_PRESIDENT: return "Chủ nhiệm";
            case ROLE_VICE_PRESIDENT: return "Phó chủ nhiệm";
            case ROLE_SECRETARY: return "Thư ký";
            case ROLE_MEMBER: return "Thành viên";
            default: return clubRole;
        }
    }
    
    public String getStatusDisplayName() {
        switch (status) {
            case STATUS_PENDING: return "Chờ duyệt";
            case STATUS_APPROVED: return "Đã duyệt";
            case STATUS_REJECTED: return "Đã từ chối";
            case STATUS_LEFT: return "Đã rời";
            default: return status;
        }
    }
}
