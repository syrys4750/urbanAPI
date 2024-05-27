package es.uv.sersomon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import es.uv.sersomon.security.CustomAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    RestTemplate restTemplate;

    @Value("${app.repository.auth.url}")
    private String authServiceUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CustomAuthorizationFilter authorizationFilter = new CustomAuthorizationFilter(restTemplate, authServiceUrl);

        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/api/v1/aparcamientos", "/api/v1/aparcamiento/**/status").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/aparcamiento").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/aparcamiento/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/estacion/**").hasAnyAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/evento/**").hasAnyAuthority("ROLE_PARKING", "ROLE_ADMIN")
                .and()
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

}
