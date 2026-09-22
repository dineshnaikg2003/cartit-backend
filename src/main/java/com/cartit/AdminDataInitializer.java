package com.cartit;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.entity.User;
import com.cartit.enums.Role;
import com.cartit.repository.UserRepository;

@Component
public class AdminDataInitializer
        implements CommandLineRunner {

    private final UserRepository userRepository;

    public AdminDataInitializer(
            UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {

        System.out.println(
                "========================================"
        );
        System.out.println(
                "ADMIN DATA INITIALIZER RUNNING"
        );
        System.out.println(
                "========================================"
        );

        List<AdminSeed> admins = List.of(
                new AdminSeed(
                        "7411611476",
                        "admin1@cartit.com",
                        "Super Admin"
                ),
                new AdminSeed(
                        "9916197646",
                        "admin2@cartit.com",
                        "Store Admin"
                )
        );

        for (AdminSeed admin : admins) {
            createOrValidateAdmin(admin);
        }
    }

    private void createOrValidateAdmin(
            AdminSeed admin) {

        System.out.println(
                "Checking admin: " + admin.phone()
        );

        Optional<User> existingUser =
                userRepository.findByPhone(
                        admin.phone()
                );

        // -------------------------------------------------
        // PHONE ALREADY EXISTS
        // -------------------------------------------------
        if (existingUser.isPresent()) {

            User user = existingUser.get();

            if (user.getRole() == Role.ADMIN) {

                System.out.println(
                        "CartIT admin already exists: "
                                + admin.phone()
                );

                return;
            }

            // Never promote an existing customer
            // automatically.
            throw new IllegalStateException(
                    "Phone "
                            + admin.phone()
                            + " already belongs to a "
                            + user.getRole()
                            + " user."
            );
        }

        // -------------------------------------------------
        // EMAIL ALREADY EXISTS
        // -------------------------------------------------
        if (userRepository.existsByEmailIgnoreCase(
                admin.email())) {

            throw new IllegalStateException(
                    "Email "
                            + admin.email()
                            + " is already registered."
            );
        }

        // -------------------------------------------------
        // CREATE ADMIN
        // -------------------------------------------------
        User user = new User();

        user.setName(admin.name());
        user.setPhone(admin.phone());
        user.setEmail(admin.email());
        user.setRole(Role.ADMIN);

        userRepository.save(user);

        System.out.println(
                "CartIT ADMIN created successfully: "
                        + admin.phone()
        );
    }

    private record AdminSeed(
            String phone,
            String email,
            String name
    ) {
    }
}