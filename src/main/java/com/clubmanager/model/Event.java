package com.clubmanager.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Model class đại diện cho bảng events
 */
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String STATUS_DRAFT = "draft";
    public static final String STATUS_PUBLISHED = "published";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_COMPLETED = "completed";
    
    private Long id;
    private Long clubId;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Integer capacity;
    private boolean externalRegistrationEnabled;
    private LocalDateTime externalRegistrationDeadline;
    private boolean externalRegistrationRequiresApproval;
    private String status;
    private Long createdBy;
    private Timestamp createdAt;
    
    // Thông tin bổ sung từ join
    private Club club;
    private User creator;
    private int registrationCount;
    
    public Event() {
        this.status = STATUS_PUBLISHED;
        this.externalRegistrationEnabled = false;
        this.externalRegistrationRequiresApproval = false;
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
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public LocalDateTime getStartsAt() {
        return startsAt;
    }
    
    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }
    
    public LocalDateTime getEndsAt() {
        return endsAt;
    }
    
    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    
    public boolean isExternalRegistrationEnabled() {
        return externalRegistrationEnabled;
    }
    
    public void setExternalRegistrationEnabled(boolean externalRegistrationEnabled) {
        this.externalRegistrationEnabled = externalRegistrationEnabled;
    }
    
    public LocalDateTime getExternalRegistrationDeadline() {
        return externalRegistrationDeadline;
    }
    
    public void setExternalRegistrationDeadline(LocalDateTime externalRegistrationDeadline) {
        this.externalRegistrationDeadline = externalRegistrationDeadline;
    }
    
    public boolean isExternalRegistrationRequiresApproval() {
        return externalRegistrationRequiresApproval;
    }
    
    public void setExternalRegistrationRequiresApproval(boolean externalRegistrationRequiresApproval) {
        this.externalRegistrationRequiresApproval = externalRegistrationRequiresApproval;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Club getClub() {
        return club;
    }
    
    public void setClub(Club club) {
        this.club = club;
    }
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    public int getRegistrationCount() {
        return registrationCount;
    }
    
    public void setRegistrationCount(int registrationCount) {
        this.registrationCount = registrationCount;
    }
    
    public String getStatusDisplayName() {
        switch (status) {
            case STATUS_DRAFT: return "Bản nháp";
            case STATUS_PUBLISHED: return "Đã công bố";
            case STATUS_CANCELLED: return "Đã hủy";
            case STATUS_COMPLETED: return "Đã hoàn thành";
            default: return status;
        }
    }
    
    public boolean isUpcoming() {
        return startsAt != null && startsAt.isAfter(LocalDateTime.now());
    }
    
    public boolean isOngoing() {
        LocalDateTime now = LocalDateTime.now();
        return startsAt != null && endsAt != null 
                && now.isAfter(startsAt) && now.isBefore(endsAt);
    }
}
