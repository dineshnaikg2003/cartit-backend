package com.cartit.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cartit.entity.User;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.UserRepository;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String phone = authentication.getName();

        return userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}