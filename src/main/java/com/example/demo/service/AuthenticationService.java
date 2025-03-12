package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.request.AccountRequest;
import com.example.demo.entity.request.AuthenticationRequest;
import com.example.demo.entity.response.AuthenticationResponse;
import com.example.demo.enums.RoleEnum;
import com.example.demo.repository.AuthenticationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    public Account register(AccountRequest accountRequest) {
        // xử lý logic

        // lưu xuống database
        Account account = new Account();
        account.setEmail(accountRequest.getEmail()); // Dùng email thay vì username
        account.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        account.setFullName(accountRequest.getFullName());
        account.setPhonenumber(accountRequest.getPhonenumber());
        account.setAddress(accountRequest.getAddress());
        account.setSkintypeEnum(accountRequest.getSkintypeEnum());
        account.setRoleEnum(RoleEnum.CUSTOMER);
        account.setPoint(0); // Mặc định điểm thưởng là 0


        Account newAccount = authenticationRepository.save(account);
        return newAccount;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authenticationRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    }


    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new NullPointerException("Wrong uername or password");
        }
        Account account = authenticationRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        String token = tokenService.generateToken(account);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setEmail(account.getEmail());
        authenticationResponse.setId(account.getId());
        authenticationResponse.setFullName(account.getFullName());
        authenticationResponse.setPhonenumber(account.getPhonenumber());
        authenticationResponse.setAddress(account.getAddress());
        authenticationResponse.setSkintypeEnum(account.getSkintypeEnum());
        authenticationResponse.setPoint(account.getPoint());
        authenticationResponse.setRoleEnum(account.getRoleEnum());
        authenticationResponse.setToken(token);

        return authenticationResponse;
    }
}
