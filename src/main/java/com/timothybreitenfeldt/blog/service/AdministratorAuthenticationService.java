package com.timothybreitenfeldt.blog.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.timothybreitenfeldt.blog.dto.AuthenticationRequest;
import com.timothybreitenfeldt.blog.dto.AuthenticationResponse;
import com.timothybreitenfeldt.blog.dto.RegisterRequest;
import com.timothybreitenfeldt.blog.model.User;
import com.timothybreitenfeldt.blog.repository.UserRepository;
import com.timothybreitenfeldt.blog.util.JWTUtil;

@Service
public class AdministratorAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "administratorAuthenticationManager")
    private AuthenticationManager administratorAuthenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    public void registerAdministrator(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(User.Role.ROLE_ADMINISTRATOR);

        this.userRepository.save(user);
    }

    public AuthenticationResponse administratorLogin(AuthenticationRequest authenticationRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());
        Authentication authentication = this.administratorAuthenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.jwtUtil.generateToken(authentication);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();
        return new AuthenticationResponse(jwt, user.getUsername());
    }
}
