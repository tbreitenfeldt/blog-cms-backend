package com.timothybreitenfeldt.blog.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.timothybreitenfeldt.blog.dto.AuthenticationRequestDto;
import com.timothybreitenfeldt.blog.dto.AuthenticationResponseDto;
import com.timothybreitenfeldt.blog.dto.RegisterRequestDto;
import com.timothybreitenfeldt.blog.model.User;
import com.timothybreitenfeldt.blog.model.User.Role;
import com.timothybreitenfeldt.blog.repository.UserRepository;
import com.timothybreitenfeldt.blog.security.JWTUtil;
import com.timothybreitenfeldt.blog.security.UserDetailsImpl;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        System.out.println("username from userDetailsImpl: " + userDetailsImpl.getUsername());
        System.out.println("user ID from UserDetailsImpl: " + userDetailsImpl.getUserId());
        String jwt = this.jwtUtil.generateToken(userDetailsImpl);
        return new AuthenticationResponseDto(jwt, userDetailsImpl.getUsername(),
                userDetailsImpl.getAuthorities().toString());
    }

    public void registerAuthor(RegisterRequestDto registerRequestDto) {
        User user = this.matFromRegisterRequestDtoToUserModel(registerRequestDto);
        user.setRoles(new HashSet<Role>(Collections.singletonList(Role.ROLE_AUTHOR)));
        this.userRepository.save(user);
    }

    public void registerAdministrator(RegisterRequestDto registerRequestDto) {
        User user = this.matFromRegisterRequestDtoToUserModel(registerRequestDto);
        user.setRoles(new HashSet<Role>(Arrays.asList(Role.ROLE_ADMINISTRATOR, Role.ROLE_AUTHOR)));
        this.userRepository.save(user);
    }

    private User matFromRegisterRequestDtoToUserModel(RegisterRequestDto registerRequestDto) {
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setPassword(this.passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setEmail(registerRequestDto.getEmail());
        return user;
    }

}
