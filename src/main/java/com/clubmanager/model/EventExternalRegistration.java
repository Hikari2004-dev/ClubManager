package com.clubmanager.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class đại diện cho bảng event_external_registrations
 */
public class EventExternalRegistration implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String STATUS_REGISTERED = "registered";
    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_REJECTED = "rejected";
    public static final String STATUS_CANCELLED = "cancelled";
    
    private Long id;
    private Long eventId;
    private Long externalParticipantId;
    private String status;
    private Timestamp registeredAt;
    private Long approvedBy;
    private Timestamp approvedAt;
    private String rejectedReason;
    
    // Thông tin bổ sung từ join
    private Event event;
    private ExternalParticipant participant;
    private User approver;
    
    public EventExternalRegistration() {
        this.status = STATUS_REGISTERED;
    }
    
    // Getters và Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getEventId() {
        return eventId;
    }
    
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
    
    public Long getExternalParticipantId() {
        return externalParticipantId;
    }
    
    public void setExternalParticipantId(Long externalParticipantId) {
        this.externalParticipantId = externalParticipantId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getRegisteredAt() {
        return registeredAt;
    }
    
    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
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
    
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    public ExternalParticipant getParticipant() {
        return participant;
    }
    
    public void setParticipant(ExternalParticipant participant) {
        this.participant = participant;
    }
    
    public User getApprover() {
        return approver;
    }
    
    public void setApprover(User approver) {
        this.approver = approver;
    }
    
    public String getStatusDisplayName() {
        switch (status) {
            case STATUS_REGISTERED: return "Đã đăng ký";
            case STATUS_APPROVED: return "Đã duyệt";
            case STATUS_REJECTED: return "Đã từ chối";
            case STATUS_CANCELLED: return "Đã hủy";
            default: return status;
        }
    }
}
