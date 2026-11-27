package com.example.blackspace.Config;


import com.example.blackspace.Recaptcha.RecaptchaFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    private final CustomLogoutHandler customLogoutHandler;

    public SecurityConfig(CustomLogoutHandler customLogoutHandler) {
        this.customLogoutHandler = customLogoutHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RecaptchaFilter recaptchaFilter) throws Exception {

        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/api/**", "/ipn/**", "/callback/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/forgot-password",
                                "/reset-password",
                                "/stores/**",
                                "/store/**",
                                "/checkout/**",
                                "/category/**",
                                "/categories/**",
                                "/product/**",
                                "/subscription/**",
                                "/products/**",
                                "/products/search/**",
                                "/about",
                                "/help",
                                "/register",
                                "/verify-otp",
                                "/resend-otp",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**",
                                "/api/products/**",
                                "/api/payments/**",
                                "/api/subscription/**",
                                "/api/chatbot/**",
                                "/api/stores/**",
                                "/api/pesapal/**",
                                "/pay/**",
                                "/ipn/**",
                                "/api/pesapal/pay/**",
                                "/callback/**"
                        ).permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/account/**").hasAnyRole("SELLER")

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("username") // your form field is still 'username'
                        .passwordParameter("password")
                        .defaultSuccessUrl("/post-login", true)
                        .failureUrl("/login?error=true")
                        .permitAll()


                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(customLogoutHandler)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )


                // Add our reCAPTCHA filter **before** the UsernamePasswordAuthenticationFilter
        .addFilterBefore(recaptchaFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}

