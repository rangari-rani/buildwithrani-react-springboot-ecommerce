package com.buildwithrani.backend.auth.security;

import com.buildwithrani.backend.auth.model.User;
import com.buildwithrani.backend.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            @Value("${jwt.secret}") String secret,
            UserRepository userRepository
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String email = claims.getSubject();
                String role = claims.get("role", String.class);
                Integer tokenVersion = claims.get("tokenVersion", Integer.class);

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // 🔐 TOKEN REVOCATION CHECK
                if (!tokenVersion.equals(user.getTokenVersion())) {
                    throw new RuntimeException("Token revoked");
                }

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(authority)
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

            } catch (Exception e) {
                // Invalid or revoked token
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

