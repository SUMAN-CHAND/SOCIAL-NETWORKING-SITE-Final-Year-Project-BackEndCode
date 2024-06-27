package com.insta.instagram.modal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.insta.instagram.dto.UserDto;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Comments")
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="id",column=@Column(name="user_id")),
		@AttributeOverride(name="email",column=@Column(name="user_email"))
	})
	private UserDto user;
	
	private String content;
	@Embedded
	@ElementCollection
	private Set<UserDto> likedbyUsers = new HashSet<>();
	
	private LocalDateTime cratedAt;
	
	

	public Comments() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Comments(Integer id, UserDto user, String content, Set<UserDto> likedbyUsers, LocalDateTime cratedAt) {
		super();
		this.id = id;
		this.user = user;
		this.content = content;
		this.likedbyUsers = likedbyUsers;
		this.cratedAt = cratedAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<UserDto> getLikedbyUsers() {
		return likedbyUsers;
	}

	public void setLikedbyUsers(Set<UserDto> likedbyUsers) {
		this.likedbyUsers = likedbyUsers;
	}

	public LocalDateTime getCratedAt() {
		return cratedAt;
	}

	public void setCratedAt(LocalDateTime cratedAt) {
		this.cratedAt = cratedAt;
	}

	@Override
	public String toString() {
		return "Comments [id=" + id + ", user=" + user + ", content=" + content + ", likedbyUsers=" + likedbyUsers
				+ ", cratedAt=" + cratedAt + "]";
	}
	
	
	
	
}
