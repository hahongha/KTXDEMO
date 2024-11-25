package com.utc.dormitory_managing.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = false, exclude = {"role"}
)
public class User extends BaseModel{
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "user_id", updatable = false, nullable = false)
	private String userId;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	@Column
	private Long expired;
	
	@Column
	private String refreshToken;
	@Column
	private String accessToken;
	

}
