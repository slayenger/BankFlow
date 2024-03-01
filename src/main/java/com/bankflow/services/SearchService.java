package com.bankflow.services;

import com.bankflow.dtos.SearchResponse;
import com.bankflow.entities.ContactInfo;
import com.bankflow.entities.User;
import com.bankflow.exceptions.UserNotFoundException;
import com.bankflow.repositories.ContactInfoRepository;
import com.bankflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepository userRepository;
    private final ContactInfoRepository infoRepository;

    public SearchResponse searchByEmail(String email)
    {
        if (!infoRepository.existsByEmail(email))
        {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }

        ContactInfo contactInfo = infoRepository.findByEmail(email);

        return buildAndReturnRequest(contactInfo);
    }

    public SearchResponse searchByPhoneNumber(String phoneNumber)
    {
        if (!infoRepository.existsByPhoneNumber(phoneNumber))
        {
            throw new UserNotFoundException("User with phone number " + phoneNumber + " not found.");
        }
        ContactInfo contactInfo = infoRepository.findByPhoneNumber(phoneNumber);

        return buildAndReturnRequest(contactInfo);
    }

    public Page<SearchResponse> findByDateOfBirth(int page, int size, String dateOfBirth) throws ParseException {
        Pageable pageable = PageRequest.of(page, size);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Date dob = dateFormat.parse(dateOfBirth);
        Page<User> usersPage = userRepository.findByDateOfBirthAfter(dob, pageable);

        if (!usersPage.hasContent())
        {
            throw new UserNotFoundException("Users born after " + dateOfBirth + " have not been found.");
        }

        List<SearchResponse> searchResponses = usersPage.getContent().stream()
                .map(user -> new SearchResponse(user.getUsername(), user.getFullName()))
                .collect(Collectors.toList());

        return new PageImpl<>(searchResponses, pageable, usersPage.getTotalElements());
    }

    public Page<SearchResponse> searchByFullName(String fullName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> usersPage = userRepository.findByFullNameContains(fullName, pageable);
        if (!usersPage.hasContent())
        {
            throw new UserNotFoundException("Users with this data: " + fullName + " have not been found.");
        }
        List<SearchResponse> searchResponses = usersPage.getContent().stream()
                .map(user -> new SearchResponse(user.getUsername(), user.getFullName()))
                .collect(Collectors.toList());

        return new PageImpl<>(searchResponses, pageable, usersPage.getTotalElements());
    }

    private SearchResponse buildAndReturnRequest(ContactInfo contactInfo)
    {
        User user = contactInfo.getUser();
        SearchResponse response = new SearchResponse();
        response.setUsername(user.getUsername());
        if (user.getFullName() != null)
        {
            response.setFullName(user.getFullName());
        }
        else
        {
            response.setFullName("");
        }

        return response;
    }


}
