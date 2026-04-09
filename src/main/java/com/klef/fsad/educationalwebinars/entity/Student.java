package com.klef.fsad.educationalwebinars.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_table")

public class Student 
{
	@Id
	@Column(length = 50,unique=true)
	private String username;
	@Column(length = 50 , nullable = false)
	private String password;
	@Column(length = 50,unique=true)
    private String email;
	 @Column(nullable = false, length = 50)
	 private String name;
	 @Column(nullable = false, unique = true, length = 20)
	 private String contact;
	 @CreationTimestamp
	 @Column(updatable = false)
	 private LocalDateTime registeredAt;
	 public String getUsername() {
		 return username;
	 }
	 public void setUsername(String username) {
		 this.username = username;
	 }
	 public String getPassword() {
		 return password;
	 }
	 public void setPassword(String password) {
		 this.password = password;
	 }
	 public String getEmail() {
		 return email;
	 }
	 public void setEmail(String email) {
		 this.email = email;
	 }
	 public String getName() {
		 return name;
	 }
	 public void setName(String name) {
		 this.name = name;
	 }
	 public String getContact() {
		 return contact;
	 }
	 public void setContact(String contact) {
		 this.contact = contact;
	 }
	 public LocalDateTime getRegisteredAt() {
		 return registeredAt;
	 }
	 public void setRegisteredAt(LocalDateTime registeredAt) {
		 this.registeredAt = registeredAt;
	 }
	 @Override
	 public String toString() {
		return "Student [username=" + username + ", password=" + password + ", email=" + email + ", name=" + name
				+ ", contact=" + contact + ", registeredAt=" + registeredAt + "]";
	 }

	
}
