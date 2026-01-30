package com.clubmanager.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng clubs
 */
public class Club implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    public static final String STATUS_SUSPENDED = "suspended";
    
    private Long id;
    private String name;
    private String description;
    private String category;
    private Long supervisorUserId;
    private String status;
    private Timestamp createdAt;
    
    // Thông tin bổ sung từ join
    private User supervisor;
    private int memberCount;
    
    public Club() {
        this.status = STATUS_ACTIVE;
    }
    
    public Club(Long id, String name, String description, String category, 
                Long supervisorUserId, String status, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.supervisorUserId = supervisorUserId;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    // Getters và Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Long getSupervisorUserId() {
        return supervisorUserId;
    }
    
    public void setSupervisorUserId(Long supervisorUserId) {
        this.supervisorUserId = supervisorUserId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public User getSupervisor() {
        return supervisor;
    }
    
    public void setSupervisor(User supervisor) {
        this.supervisor = supervisor;
    }
    
    public int getMemberCount() {
        return memberCount;
    }
    
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
    
    @Override
    public String toString() {
        return "Club{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
