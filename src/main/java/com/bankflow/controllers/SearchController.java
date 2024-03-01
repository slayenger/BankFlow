package com.bankflow.controllers;

import com.bankflow.exceptions.UserNotFoundException;
import com.bankflow.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/by-email")
    public ResponseEntity<?> searchByEmail(@RequestParam String email)
    {
        try
        {
            return ResponseEntity.status(HttpStatus.OK).body(searchService.searchByEmail(email));
        }
        catch (UserNotFoundException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @GetMapping("/by-phone-number")
    public ResponseEntity<?> searchByPhoneNumber(@RequestParam String phoneNumber)
    {
        try
        {
            return ResponseEntity.status(HttpStatus.OK).body(searchService.searchByPhoneNumber(phoneNumber));
        }
        catch(UserNotFoundException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @GetMapping("/by-date-of-birth")
    public ResponseEntity<?> searchByDateOfBirth(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam String dateOfBirth)
    {
        try
        {
            return ResponseEntity.status(HttpStatus.OK).body(searchService.findByDateOfBirth(page, size, dateOfBirth));
        }
        catch (ParseException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error date parsing.");
        }
        catch (UserNotFoundException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @GetMapping("/by-full-name")
    public ResponseEntity<?> searchByFullName(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam String fullName)
    {
        try
        {
            return ResponseEntity.status(HttpStatus.OK).body(searchService.searchByFullName(fullName, page, size));
        }
        catch (UserNotFoundException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }



}
