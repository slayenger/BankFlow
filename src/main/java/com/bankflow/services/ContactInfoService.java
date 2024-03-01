package com.bankflow.services;

import com.bankflow.dtos.ChangeEmailRequest;
import com.bankflow.dtos.ChangeNumberRequest;
import com.bankflow.entities.ContactInfo;
import com.bankflow.exceptions.DataNotFoundException;
import com.bankflow.exceptions.DublicateDataException;
import com.bankflow.exceptions.InvalidEmailOperationException;
import com.bankflow.exceptions.InvalidNumberOperationException;
import com.bankflow.repositories.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactInfoService {

    private final ContactInfoRepository infoRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactInfoService.class);

    public void addEmailToUser(String email, UUID userId)
    {
        if (infoRepository.existsByEmail(email))
        {
            String message = "User with email " + email + " is already exist." +
                    " Please, choose a different email.";
            LOGGER.error(message);
            throw new DublicateDataException(message);
        }
        else
        {
            ContactInfo contactInfo = infoRepository.findByUser_UserId(userId);
            contactInfo.getEmail().add(email);
            infoRepository.save(contactInfo);
        }
    }

    public void addPhoneNumberToUser(String phoneNumber, UUID userId)
    {
        if (infoRepository.existsByPhoneNumber(phoneNumber))
        {
            String message = "User with phone number " + phoneNumber + " is already exist." +
                    " Please, choose a different phone number.";
            LOGGER.error(message);
            throw new DublicateDataException(message);
        }
        else
        {
            ContactInfo contactInfo = infoRepository.findByUser_UserId(userId);
            contactInfo.getPhoneNumber().add(phoneNumber);
            infoRepository.save(contactInfo);
        }
    }

    public void changeMail(ChangeEmailRequest changeEmailRequest, UUID userId)
    {
        ContactInfo contactInfo = infoRepository.findByUser_UserId(userId);
        String currentEmail = changeEmailRequest.getCurrentEmail();
        String newEmail = changeEmailRequest.getNewEmail();

        if (contactInfo.getEmail().contains(currentEmail))
        {
            if (!infoRepository.existsByEmail(newEmail))
            {
                contactInfo.getEmail().remove(currentEmail);
                contactInfo.getEmail().add(newEmail);

                infoRepository.save(contactInfo);
            }
            else
            {
                String message = "Email " + newEmail + " already exist.";
                LOGGER.error(message);
                throw new DublicateDataException(message);
            }
        }
        else
        {
            String message = "Email address not found for this user.";
            LOGGER.error(message);
            throw new DataNotFoundException(message);
        }
    }

    public void changePhoneNumber(ChangeNumberRequest request, UUID userId)
    {
        ContactInfo contactInfo = infoRepository.findByUser_UserId(userId);
        String currentNumber = request.getCurrentNumber();
        String newNumber = request.getNewNumber();

        if (contactInfo.getPhoneNumber().contains(currentNumber))
        {
            if (!infoRepository.existsByPhoneNumber(newNumber))
            {
                contactInfo.getPhoneNumber().remove(currentNumber);
                contactInfo.getPhoneNumber().add(newNumber);

                infoRepository.save(contactInfo);
            }
            else
            {
                String message = "Number " + currentNumber + " already exist.";
                LOGGER.error(message);
                throw new DublicateDataException(message);
            }
        }
        else
        {
            String message = "Number not found for this user.";
            LOGGER.error(message);
            throw new DataNotFoundException(message);
        }
    }

    public void removeEmail(String email, UUID userId)
    {
        ContactInfo contactInfo = infoRepository.findByUser_UserId(userId);

        if (!contactInfo.getEmail().contains(email))
        {
            String message = "Email address not found for this user.";
            LOGGER.error(message);
            throw new DataNotFoundException(message);
        }
        else if (contactInfo.getEmail().size() == 1)
        {
            String message = "You can't delete a single email.";
            LOGGER.error(message);
            throw new InvalidEmailOperationException(message);
        }
        else
        {
            contactInfo.getEmail().remove(email);
            infoRepository.save(contactInfo);
        }
    }

    public void removePhoneNumber(String number, UUID userId)
    {
        ContactInfo contactInfo = infoRepository.findByUser_UserId(userId);

        if (!contactInfo.getPhoneNumber().contains(number))
        {
            String message = "Phone number not found for this user.";
            LOGGER.error(message);
            throw new DataNotFoundException(message);
        }
        else if (contactInfo.getPhoneNumber().size() == 1)
        {
            String message = "You can't delete a single phone number.";
            LOGGER.error(message);
            throw new InvalidNumberOperationException(message);
        }
        else
        {
            contactInfo.getPhoneNumber().remove(number);
            infoRepository.save(contactInfo);
        }
    }

}
