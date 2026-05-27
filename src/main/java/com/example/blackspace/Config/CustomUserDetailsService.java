package com.example.blackspace.Config;

import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Prepend ROLE_ if not already present
        String role = user.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role.toUpperCase();
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), // email used as username
                user.getPassword(),
                user.getEnabled(), // account enabled
                true, true, true, // accountNonExpired, credentialsNonExpired, accountNonLocked
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
