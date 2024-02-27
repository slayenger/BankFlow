package com.bankflow.controllers;

import com.bankflow.dtos.ChangeEmailRequest;
import com.bankflow.dtos.ChangeNumberRequest;
import com.bankflow.exceptions.DataNotFoundException;
import com.bankflow.exceptions.DublicateDataException;
import com.bankflow.exceptions.InvalidEmailOperationException;
import com.bankflow.exceptions.InvalidNumberOperationException;
import com.bankflow.services.ContactInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contact_info")
public class ContactInfoController {

    private final ContactInfoService infoService;

    @PatchMapping("/add-email")
    public ResponseEntity<?> addEmailToUser(@RequestBody String email, @RequestParam UUID userId)
    {
        try
        {
            infoService.addEmailToUser(email, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Email " + email + " successfully added.");
        }
        catch (DublicateDataException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @PatchMapping("/add-phone-number")
    public ResponseEntity<?> addPhoneNumber(@RequestBody String phoneNumber, @RequestParam UUID userId)
    {
        try
        {
            infoService.addPhoneNumberToUser(phoneNumber, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Phone number " + phoneNumber + " successfully added.");
        }
        catch (DublicateDataException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @PatchMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestBody ChangeEmailRequest changeEmailRequest, @RequestParam UUID userId)
    {
        try
        {
            infoService.changeMail(changeEmailRequest, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Email successfully changed.");
        }
        catch (DublicateDataException | DataNotFoundException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @PatchMapping("/change-phone-number")
    public ResponseEntity<?> changePhoneNumber(@RequestBody ChangeNumberRequest request, @RequestParam UUID userId)
    {
        try
        {
            infoService.changePhoneNumber(request, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Number successfully changed.");
        }
        catch (DublicateDataException | DataNotFoundException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @DeleteMapping("/remove-email")
    public ResponseEntity<?> removeEmail(@RequestBody String email, @RequestParam UUID userId)
    {
        try
        {
            infoService.removeEmail(email, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Email successfully deleted.");
        }
        catch (DataNotFoundException | InvalidEmailOperationException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @DeleteMapping("/remove-phone-number")
    public ResponseEntity<?> removePhoneNumber(@RequestBody String number, @RequestParam UUID userId)
    {
        try
        {
            infoService.removePhoneNumber(number, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Phone number successfully deleted.");
        }
        catch (DataNotFoundException | InvalidNumberOperationException err)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }
}
