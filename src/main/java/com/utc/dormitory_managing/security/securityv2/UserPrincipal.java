package com.utc.dormitory_managing.security.securityv2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utc.dormitory_managing.entity.User;

public class UserPrincipal implements UserDetails {
  private static final long serialVersionUID = 1L;

  private String id;

  private String username;

  private Long expired;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(String id, String username, Long expired, String password,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.username = username;
    this.expired = expired;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserPrincipal create(User user) {
	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRoleName());
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(authority);
    return new UserPrincipal(
        user.getUserId(), 
        user.getUsername(), 
        user.getExpired(),
        user.getPassword(), 
        authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public String getId() {
    return id;
  }

  public Long getExpired() {
    return expired;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    UserPrincipal user = (UserPrincipal) o;
    return Objects.equals(id, user.id);
  }
}

