package es.uv.sersomon.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uv.sersomon.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = jwtService.getTokenFromHeader(authHeader);
                int id = jwtService.getIdFromToken(token);
                SimpleGrantedAuthority authority = jwtService.getRoleFromToken(token);
                // a Collection is created to comply with `UsernamePasswordAuthenticationToken
                // constructor, even though
                // the requirements specify that every user has one or none role (authority)
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(authority);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        id,
                        null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);

            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(403);
                System.out.println(403);
                Map<String, String> error = new HashMap<>();
                error.put("error_msg", exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
