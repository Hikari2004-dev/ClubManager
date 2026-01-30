package com.clubmanager.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng roles
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String ADMIN = "admin";
    public static final String TEACHER = "teacher";
    public static final String CLUB_LEADER = "club_leader";
    public static final String MEMBER = "member";
    
    private Long id;
    private String name;
    private String displayName;
    private String description;
    private Timestamp createdAt;
    
    public Role() {}
    
    public Role(Long id, String name, String displayName, String description) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
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
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
