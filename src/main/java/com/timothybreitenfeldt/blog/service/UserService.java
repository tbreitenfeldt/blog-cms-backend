package com.timothybreitenfeldt.blog.service;

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
import com.timothybreitenfeldt.blog.repository.UserRepository;
import com.timothybreitenfeldt.blog.security.JWTUtil;

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
        String jwt = this.jwtUtil.generateToken(authentication);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();
        return new AuthenticationResponseDto(jwt, user.getUsername(), user.getAuthorities().toString());
    }

    public void registerAuthor(RegisterRequestDto registerRequestDto) {
        User user = this.matFromRegisterRequestDtoToUserModel(registerRequestDto);
        user.setRole(User.Role.ROLE_AUTHOR);
        this.userRepository.save(user);
    }

    public void registerAdministrator(RegisterRequestDto registerRequestDto) {
        User user = this.matFromRegisterRequestDtoToUserModel(registerRequestDto);
        user.setRole(User.Role.ROLE_ADMINISTRATOR);
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