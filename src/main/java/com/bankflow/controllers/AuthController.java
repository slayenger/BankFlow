package com.bankflow.controllers;

import com.bankflow.dtos.AuthenticateRequest;
import com.bankflow.services.AuthService;
import com.bankflow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticateRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(request));
    }
}
