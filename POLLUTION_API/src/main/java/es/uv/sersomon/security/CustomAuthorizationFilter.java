package es.uv.sersomon.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uv.sersomon.DTOs.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthorizationFilter.class);

    private RestTemplate restTemplate;
    private String authServiceUrl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader(AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                ResponseEntity<TokenInfo> responseAuthService = restTemplate.postForEntity(
                        authServiceUrl + "/jwt/auth", authHeader,
                        TokenInfo.class);

                switch (responseAuthService.getStatusCode().value()) {
                    case 200: // HttpStatus.OK.value()
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        TokenInfo tokenInfo = responseAuthService.getBody();
                        authorities.add(new SimpleGrantedAuthority(tokenInfo.getRole()));
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                tokenInfo.getId(),
                                null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                        break;
                    default:
                        filterChain.doFilter(request, response);
                        break;
                }
            } else {
                filterChain.doFilter(request, response);
            }
        } catch (HttpClientErrorException e) {
            int statusCode = e.getStatusCode().value();
            String errorMessage = e.getResponseBodyAsString();
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            Map<String, String> error = new HashMap<>();
            error.put("error_msg", errorMessage);

            switch (statusCode) {
                case 403: // HttpStatus.FORBIDDEN.value()
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setHeader("error", errorMessage);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                    break;
                case 422: // HttpStatus.UNPROCESSABLE_ENTITY.value()
                    response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                    response.setHeader("error", errorMessage);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                    break;
                default:
                    LOGGER.error("Unexpected status code: {}", statusCode);
                    response.setStatus(statusCode);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("There was an error decoding JWT token {}", e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

}
