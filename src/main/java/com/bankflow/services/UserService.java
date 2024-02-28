package com.bankflow.services;

import com.bankflow.dtos.CustomUserDetails;
import com.bankflow.dtos.RegistrationRequest;
import com.bankflow.entities.BankAccount;
import com.bankflow.entities.ContactInfo;
import com.bankflow.entities.User;
import com.bankflow.exceptions.DublicateDataException;
import com.bankflow.exceptions.NegativeBalanceException;
import com.bankflow.repositories.BankAccountRepository;
import com.bankflow.repositories.ContactInfoRepository;
import com.bankflow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ContactInfoRepository infoRepository;
    private final BankAccountRepository bankAccountRepository;
    private final PasswordEncoder passwordEncoder;


    public User createUser(RegistrationRequest request) throws DublicateDataException
    {
        performUserCreationVerification(request);

        User user = mapRegistrationUser(request);
        ContactInfo contactInfo = mapRegistrationContacts(request, user);
        BankAccount bankAccount = mapRegistrationBankAccount(request, user);

        userRepository.save(user);
        infoRepository.save(contactInfo);
        bankAccountRepository.save(bankAccount);

        return user;
    }

    public void setFullName (String fullName, UUID userId)
    {
        User user = userRepository.getReferenceById(userId);
        user.setFullName(fullName);
        user.setUpdatedAt(new Date());
        // TODO сделать проверку что это фио а не вьыфвфоифыфзхлыщф (?)
        userRepository.save(user);
    }


    public void setDateOfBirth(String dateOfBirthString, UUID userId) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        //dateFormat.setLenient(false);
        User user = userRepository.getReferenceById(userId);
        user.setDateOfBirth(dateFormat.parse(dateOfBirthString));
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    private void performUserCreationVerification(RegistrationRequest request)
    {
        if (request.getInitialAmount().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new NegativeBalanceException("Balance cannot be negative.");
        }

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

    private User mapRegistrationUser(RegistrationRequest request)
    {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(new Date());
        return user;
    }

    private ContactInfo mapRegistrationContacts(RegistrationRequest request, User user)
    {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setUser(user);
        Set<String> email = new HashSet<>();
        email.add(request.getEmail());
        Set<String> phoneNumber = new HashSet<>();
        phoneNumber.add(request.getPhoneNumber());
        contactInfo.setEmail(email);
        contactInfo.setPhoneNumber(phoneNumber);
        contactInfo.setCreatedAt(new Date());
        return contactInfo;
    }

    private BankAccount mapRegistrationBankAccount(RegistrationRequest request, User user)
    {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUser(user);
        bankAccount.setBalance(request.getInitialAmount());
        bankAccount.setCreatedAt(new Date());
        return bankAccount;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("User  '%s' not found", username)
                )
        );

        return new CustomUserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}
