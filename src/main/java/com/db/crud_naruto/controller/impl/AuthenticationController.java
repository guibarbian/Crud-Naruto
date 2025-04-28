package com.db.crud_naruto.controller.impl;

import com.db.crud_naruto.DTO.auth.AuthenticationDTO;
import com.db.crud_naruto.DTO.auth.AuthenticationResponse;
import com.db.crud_naruto.DTO.auth.RegisterDTO;
import com.db.crud_naruto.infra.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterDTO dto){
        return ResponseEntity.ok(authenticationService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationDTO dto){
        return ResponseEntity.ok(authenticationService.login(dto));
    }
}
