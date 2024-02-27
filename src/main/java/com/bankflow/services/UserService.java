package com.bankflow.services;

import com.bankflow.dtos.CustomUserDetails;
import com.bankflow.dtos.RegistrationRequest;
import com.bankflow.entities.BankAccount;
import com.bankflow.entities.ContactInfo;
import com.bankflow.entities.User;
import com.bankflow.exceptions.DublicateDataException;
import com.bankflow.repositories.BankAccountRepository;
import com.bankflow.repositories.ContactInfoRepository;
import com.bankflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ContactInfoRepository infoRepository;
    private final BankAccountRepository bankAccountRepository;


    public void createUser(RegistrationRequest request) throws DublicateDataException
    {
        dataVerification(request);

        User user = new User();
        user.setUsername(request.getUsername());
        //TODO need to encode
        user.setPassword(request.getPassword());
        user.setCreatedAt(new Date());

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUser(user);
        Set<String> email = new HashSet<>();
        email.add(request.getEmail());
        Set<String> phoneNumber = new HashSet<>();
        phoneNumber.add(request.getPhoneNumber());
        contactInfo.setEmail(email);
        contactInfo.setPhoneNumber(phoneNumber);

        contactInfo.setCreatedAt(new Date());

        BankAccount bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setBalance(request.getInitialAmount());
        bankAccount.setCreatedAt(new Date());

        userRepository.saveAndFlush(user);
        infoRepository.save(contactInfo);
        bankAccountRepository.save(bankAccount);

    }



    private void dataVerification(RegistrationRequest request)
    {
        if (userRepository.existsByUsername(request.getUsername()))
        {
            throw new DublicateDataException("User with username " + request.getUsername() + " is already exist." +
                    " Please, choose a different username.");
        }

        List<ContactInfo> contactInfosByPhoneNumber = infoRepository.findByPhoneNumberContains(request.getPhoneNumber());
        if (!contactInfosByPhoneNumber.isEmpty())
        {
            throw new DublicateDataException("User with number " + request.getPhoneNumber() + " is already exist." +
                    " Please, choose a different phone number.");
        }
        List<ContactInfo> contactInfosByEmail = infoRepository.findByEmailContains(request.getEmail());
        if (!contactInfosByEmail.isEmpty())
        {
            throw new DublicateDataException("User with email " + request.getEmail() + " is already exist." +
                    " Please, choose a different email.");
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User  '%s' not found", username)
                )
                //TODO redirect to registerUser
        );

        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}
