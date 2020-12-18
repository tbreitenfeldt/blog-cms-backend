package com.timothybreitenfeldt.blog.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.timothybreitenfeldt.blog.model.User;
import com.timothybreitenfeldt.blog.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsernameOrEmailAllIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
        UserDetails userDetails = new UserDetailsImpl(user.getUsername(), user.getPassword(), authorities,
                user.isAccountNonLocked(), user.getId());
        return userDetails;
    }

}
