package com.study.manager.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.common.base.Objects;

@NamedQueries({ @NamedQuery(name = "UserEntity.findByEmail", query = "SELECT u FROM UserEntity u WHERE u.email = ?1"),
		@NamedQuery(name = "UserEntity.findByCredentials", query = "SELECT u FROM UserEntity u WHERE u.email = ?1 and u.password = ?2"),
		@NamedQuery(name = "UserEntity.findByUserIdAndAuthToken", query = "SELECT u FROM UserEntity u WHERE u.id = ?1 and u.authToken = ?2"),
		@NamedQuery(name = "UserEntity.findByUserIdAndEmailToken", query = "SELECT u FROM UserEntity u WHERE u.id = ?1 and u.emailVerifyToken = ?2"),
		@NamedQuery(name = "UserEntity.verifyUser", query = "UPDATE UserEntity u SET u.verified = true WHERE u.id = ?1") })

@Entity
@Table(name = "USER")
public class UserEntity extends BaseEntity {

	@NotNull
	private String email;

	@NotNull
	private String password;

	private String authToken;

	private String emailVerifyToken;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;

	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@NotNull
	private Boolean verified = false;

	public String getEmailVerifyToken() {
		return emailVerifyToken;
	}

	public void setEmailVerifyToken(String emailVerifyToken) {
		this.emailVerifyToken = emailVerifyToken;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("id", id).add("email", email).add("authToken", authToken).toString();
	}
}
