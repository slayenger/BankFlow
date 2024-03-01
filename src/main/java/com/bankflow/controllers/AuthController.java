package com.bankflow.controllers;

import com.bankflow.dtos.AuthenticateRequest;
import com.bankflow.services.AuthService;
import com.bankflow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
        try
        {
            return ResponseEntity.status(HttpStatus.OK).body(authService.authenticateUser(request));
        }
        catch (BadCredentialsException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }

    }
}
