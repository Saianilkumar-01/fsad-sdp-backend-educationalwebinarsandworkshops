package com.klef.fsad.educationalwebinars.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "manage_events")
public class ManageEvents 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Link to ScheduleEvent
    private int eventId;

    private String status; 
    // values: UPCOMING / LIVE / COMPLETED / CANCELLED

    private String approvalStatus; 
    // values: PENDING / APPROVED / REJECTED

    private String remarks;

    // ================= GETTERS & SETTERS =================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}