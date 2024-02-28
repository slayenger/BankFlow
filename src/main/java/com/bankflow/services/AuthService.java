package com.bankflow.services;

import com.bankflow.dtos.AuthenticateRequest;
import com.bankflow.dtos.JwtResponseDTO;
import com.bankflow.security.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;


    public JwtResponseDTO authenticateUser(AuthenticateRequest request)
    {
        try
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),request.getPassword())
            );

            UserDetails userDetails = userService.loadUserByUsername(request.getUsername());

            String token = jwtTokenUtils.generateToken(userDetails);
            return new JwtResponseDTO(token);
        }
        catch (BadCredentialsException e)
        {

            throw new BadCredentialsException("Error authentication user "
                    + request.getUsername());
        }
    }
}
