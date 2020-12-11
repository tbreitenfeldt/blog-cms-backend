package com.timothybreitenfeldt.blog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.timothybreitenfeldt.blog.dto.AuthenticationRequestDto;
import com.timothybreitenfeldt.blog.dto.AuthenticationResponseDto;
import com.timothybreitenfeldt.blog.dto.RegisterRequestDto;
import com.timothybreitenfeldt.blog.service.AdministratorAuthenticationService;

@RestController
@RequestMapping("/api")
public class AdministratorAuthenticationController {

    @Autowired
    private AdministratorAuthenticationService administratorAuthenticationService;

    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAdministrator(@Valid @RequestBody RegisterRequestDto registerRequest) {
        this.administratorAuthenticationService.registerAdministrator(registerRequest);
    }

    @PostMapping("/admin/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponseDto administratorLogin(@Valid @RequestBody AuthenticationRequestDto authenticationRequest) {
        return this.administratorAuthenticationService.administratorLogin(authenticationRequest);
    }
}
