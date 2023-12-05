package com.example.testcase.service;


import com.example.testcase.model.User;
import com.example.testcase.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User>user=repo.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("Username not found.");
        }
        return user.get();
    }
}