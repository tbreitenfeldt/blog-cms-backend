package com.timothybreitenfeldt.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.timothybreitenfeldt.blog.dto.AuthenticationRequest;
import com.timothybreitenfeldt.blog.dto.AuthenticationResponse;
import com.timothybreitenfeldt.blog.dto.RegisterRequest;
import com.timothybreitenfeldt.blog.model.User;
import com.timothybreitenfeldt.blog.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerAuthor(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(User.Role.ROLE_AUTHOR);

        this.userRepository.save(user);
    }

    public void registerAdministrator(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(User.Role.ROLE_ADMINISTRATOR);

        this.userRepository.save(user);
    }

    public AuthenticationResponse authorLogin(AuthenticationRequest authenticationRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    public AuthenticationResponse administratorLogin(AuthenticationRequest authenticationRequest) {
        // TODO Auto-generated method stub
        return null;
    }
}
