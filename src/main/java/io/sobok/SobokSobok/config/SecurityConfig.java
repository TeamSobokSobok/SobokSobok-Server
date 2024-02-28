package io.sobok.SobokSobok.config;

import io.sobok.SobokSobok.security.filter.ExceptionHandlerFilter;
import io.sobok.SobokSobok.security.filter.JwtCustomFilter;
import io.sobok.SobokSobok.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            // swagger
            "/v3/**",
            "/swagger-ui/**",

            // health check
            "/actuator/health",

            // api
            "/profile",
            "/auth/signup",
            "/auth/login",
            "/auth/refresh",
            "/user",
            "/pill/count/**"
    };

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
//                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(
                                Stream
                                        .of(WHITE_LIST)
                                        .map(AntPathRequestMatcher::antMatcher)
                                        .toArray(AntPathRequestMatcher[]::new)
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new JwtCustomFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new ExceptionHandlerFilter(),
                        JwtCustomFilter.class
                )
        ;

        return http.build();
    }
}
