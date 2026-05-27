package com.example.blackspace.Recaptcha;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RecaptchaFilter extends OncePerRequestFilter {

    private final RecaptchaService recaptchaService;

    public RecaptchaFilter(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        boolean isLogin =
                "POST".equalsIgnoreCase(request.getMethod())
                        && "/login".equals(request.getServletPath());

        boolean isRegister =
                "POST".equalsIgnoreCase(request.getMethod())
                        && "/register".equals(request.getServletPath());

        if (isLogin || isRegister) {
            String captchaResponse = request.getParameter("g-recaptcha-response");

            if (!recaptchaService.verify(captchaResponse)) {
                response.sendRedirect(
                        isLogin
                                ? "/login?captchaError=true"
                                : "/register?captchaError=true"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
