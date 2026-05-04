package com.innowise.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class GatewayHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws IOException, ServletException {

        System.out.println("🔵🔵🔵 GATEWAY HEADER FILTER CALLED for URI: " + request.getRequestURI());
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");

        System.out.println("=== GatewayHeaderFilter ===");
        System.out.println("X-User-Id: " + userId);
        System.out.println("X-User-Role: " + role);
        System.out.println("All headers:");
        request.getHeaderNames().asIterator().forEachRemaining(h ->
                System.out.println("  " + h + ": " + request.getHeader(h))
        );


        if (userId != null && role != null) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null,
                            List.of(new SimpleGrantedAuthority("ROLE_"+role)));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }
}