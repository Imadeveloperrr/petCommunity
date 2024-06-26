package com.example.petcommunity.config;


import com.example.petcommunity.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                // JWT 방식은 세션과 달리 Stateless 로 관리하기 때문에 csrf 공격에 대한 방어는 필요없기 때문에 disable 설정
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                // JWT 사용하기 때문에 세션 방식은 사용하지 않는다.
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 경로별 인가 작업
                .authorizeHttpRequests(auth -> auth

                        // 모든 권한 허용 (permitAll)
                        .requestMatchers(
                                "/",
                                "/api/register",
                                "/api/login",
                                "/api/check-userId",
                                "/api/boards",
                                "/api/boardWrite",
                                "/api/board/{boardNo}",
                                "/api/comment",
                                "/api/missingWrite",
                                "/api/getMissing",
                                "/api/calorie",
                                "/api/age",
                                "/api/notices",
                                "/api/noticeWrite",
                                "/api/noticeDetail/{noticeNo}",
                                "api/noticeEdit",
                                "api/delete",
                                // Swagger UI 경로 추가
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger-resources/",
                                "/error"
                        ).permitAll()

                        // USER, ADMIN 권한이 있어야 접근 가능한 경로
                        .requestMatchers(
                                "/api/user/info/{userNo}",
                                "/api/user/update",
                                "/api/user/delete"
                        ).hasAnyRole("USER", "ADMIN")

                        // ADMIN 권한이 있어야 접근 가능한 경로
                        .requestMatchers("/api/user/list").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8080", "http://43.201.19.22:3000", "http://43.201.19.22:8080")); // React 앱의 URL
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // 비밀번호 인코딩 작업
        return new BCryptPasswordEncoder();
    }
}