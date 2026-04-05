package com.klef.fsad.educationalwebinars.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "resources_table")
public class StudentResources 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String event;

    @Column(nullable = false, length = 200)
    private String resourceName;

    @Column(nullable = false, length = 500)
    private String resourceUrl;

    // Uploaded Date
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceUrl() {
		return resourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}

	public LocalDateTime getUploadedDate() {
		return uploadedDate;
	}

	public void setUploadedDate(LocalDateTime uploadedDate) {
		this.uploadedDate = uploadedDate;
	}

	@Override
	public String toString() {
		return "StudentResources [id=" + id + ", event=" + event + ", resourceName=" + resourceName + ", resourceUrl="
				+ resourceUrl + ", uploadedDate=" + uploadedDate + "]";
	}
}