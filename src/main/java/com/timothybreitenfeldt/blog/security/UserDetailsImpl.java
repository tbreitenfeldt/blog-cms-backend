package com.timothybreitenfeldt.blog.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = -5037647431058066456L;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isAccountNonLocked;
    private Long userId;

    public UserDetailsImpl(String username, Collection<? extends GrantedAuthority> authorities,
            boolean isAccountNonLocked, Long userId) {
        this(username, "", authorities, isAccountNonLocked, userId);
    }

    public UserDetailsImpl(String username, String password, Collection<? extends GrantedAuthority> authorities,
            boolean isAccountNonLocked, Long userId) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isAccountNonLocked = isAccountNonLocked;
        this.userId = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return this.userId;
    }

}
