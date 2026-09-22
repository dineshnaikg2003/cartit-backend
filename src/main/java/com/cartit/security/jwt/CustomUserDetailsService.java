package com.cartit.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cartit.entity.User;
import com.cartit.repository.UserRepository;

@Service
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(
            UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(
            String phone)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByPhone(phone)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

        // Inactive users must not authenticate.
        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new UsernameNotFoundException(
                    "User account is inactive"
            );
        }

        return new CustomUserDetails(user);
    }
}