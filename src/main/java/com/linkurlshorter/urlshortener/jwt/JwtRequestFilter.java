package com.linkurlshorter.urlshortener.jwt;

import com.linkurlshorter.urlshortener.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter class for JWT authentication.
 *
 * <p>This class intercepts incoming requests, extracts JWT tokens, and authenticates users
 * based on the token information.
 *
 * @author Egor Sivenko
 * @see org.springframework.web.filter.OncePerRequestFilter
 * @see com.linkurlshorter.urlshortener.jwt.JwtUtil
 * @see com.linkurlshorter.urlshortener.security.CustomUserDetailsService
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * Utility class for JWT operations.
     */
    private final JwtUtil jwtUtil;

    /**
     * Custom implementation of UserDetailsService for loading user details.
     */
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Filters incoming HTTP requests and performs JWT authentication.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws IOException      if an I/O error occurs during filtering
     * @throws ServletException if a servlet error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            String username = jwtUtil.getUsernameFromToken(token.substring(7));

            if (StringUtils.hasText(username)) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
