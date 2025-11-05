package com.taskmanager.config;

import com.taskmanager.model.Role;
import com.taskmanager.model.User;
import com.taskmanager.repository.RoleRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            Role userRole = new Role("USER");
            Role adminRole = new Role("ADMIN");

            roleRepository.save(userRole);
            roleRepository.save(adminRole);

            log.info("Roles inicializados: USER, ADMIN");
        }
    }

    private void initializeUsers() {
        if (userRepository.count() == 0) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));

            User normalUser = User.builder()
                    .username("user")
                    .email("user@taskmanager.com")
                    .password(passwordEncoder.encode("password123"))
                    .roles(Set.of(userRole))
                    .enabled(true)
                    .build();

            userRepository.save(normalUser);
            log.info("Usuario creado: user / password123 (Rol: USER)");

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

            User adminUser = User.builder()
                    .username("admin")
                    .email("admin@taskmanager.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole))
                    .enabled(true)
                    .build();

            userRepository.save(adminUser);
            log.info("Usuario creado: admin / admin123 (Rol: ADMIN)");
        }
    }
}