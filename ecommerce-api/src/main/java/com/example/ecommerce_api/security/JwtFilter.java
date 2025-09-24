package com.example.ecommerce_api.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // 1. Define the list of paths to be excluded from filtering
    private final List<String> excludedPaths = List.of(
            "/auth",
            "/v3/api-docs",
            "/swagger-ui"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // 2. Add the bypass logic at the very beginning of the filter
        boolean isExcluded = excludedPaths.stream().anyMatch(requestURI::startsWith);
        if (isExcluded) {
            filterChain.doFilter(request, response); // Skip the filter and continue the chain
            return;
        }

        // 3. Your existing JWT validation logic runs only for non-excluded paths
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            try {
                String email = jwtUtil.validateAndGetSubject(token);
                // create a simple Authentication with email as principal
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (io.jsonwebtoken.JwtException e) {
                // invalid token; you can optionally send error response and return here
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid/Expired Token");
                return;
            }
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Request not authenticated.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}