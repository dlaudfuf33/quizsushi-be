package com.cmdlee.quizsushi.global.config.security;

import com.cmdlee.quizsushi.admin.domain.model.enums.AdminRole;
import com.cmdlee.quizsushi.global.auth.jwt.JwtAuthenticationFilter;
import com.cmdlee.quizsushi.global.auth.jwt.JwtTokenProvider;
import com.cmdlee.quizsushi.global.config.security.admin.CustomAdminDetails;
import com.cmdlee.quizsushi.member.service.RefreshTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] ALLOWED_URLS = {
            "/api/auth/**",
            "/api/auth/logout",
            "/api/quizzes/**",
            "/api/members/**"
    };

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider,
                          RefreshTokenService refreshTokenService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenService = refreshTokenService;
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, refreshTokenService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        AdminRole[] roleList = AdminRole.values();

        StringBuilder hierarchy = new StringBuilder();
        for (int i = 0; i < roleList.length - 1; i++) {
            hierarchy
                    .append(roleList[i].name())
                    .append(" > ")
                    .append(roleList[i + 1].name())
                    .append("\n");
        }

        return RoleHierarchyImpl.fromHierarchy(hierarchy.toString());
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> adminAccessManager(RoleHierarchy roleHierarchy) {
        return (authentication, context) -> {
            Collection<? extends GrantedAuthority> authorities =
                    roleHierarchy.getReachableGrantedAuthorities(authentication.get().getAuthorities());
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_VIEWER")) {
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        };
    }

    //  사용자용 JWT 기반 보안
    @Bean
    public SecurityFilterChain userSecurity(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ALLOWED_URLS).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //  관리자용 세션 기반 보안
    @Bean
    public SecurityFilterChain adminSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/cmdlee-qs/**")
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(form -> form
                        .loginProcessingUrl("/cmdlee-qs/login")
                        .defaultSuccessUrl("/cmdlee/dashboard")
                        .failureHandler((request, response, exception) -> {
                            String error = "unknown";

                            if (exception instanceof BadCredentialsException) {
                                error = "bad_credentials";
                            } else if (exception instanceof UsernameNotFoundException) {
                                error = "user_not_found";
                            }

                            response.sendRedirect("/cmdlee/login?error=" + error);
                        })
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/cmdlee-qs/logout")
                        .logoutSuccessUrl("/")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/cmdlee-qs/login").permitAll()
                        .requestMatchers("/cmdlee-qs/admin/**").access(AuthorityAuthorizationManager.hasAnyRole("ROOT", "ADMIN", "MANAGER"))
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                            System.out.println("\n==================[REQUEST LOG]==================");
                            System.out.println("🚫 인가 거부 (403)");
                            System.out.println("▶ 요청 URL      : " + request.getRequestURI());
                            System.out.println("▶ HTTP Method  : " + request.getMethod());
                            System.out.println("▶ 요청 IP       : " + request.getRemoteAddr());
                            System.out.println("▶ Principal     : " + auth.getPrincipal());
                            if (auth.getPrincipal() instanceof CustomAdminDetails adminDetails) {
                                System.out.println("▶ Admin ID      : " + adminDetails.getId());
                                System.out.println("▶ Username      : " + adminDetails.getUsername());
                                System.out.println("▶ Role Enum     : " + adminDetails.getRole());
                            }
                            System.out.println("▶ Authorities   : " + auth.getAuthorities());
                            for (GrantedAuthority authority : auth.getAuthorities()) {
                                System.out.println("▶ → " + authority.getAuthority());
                            }
                            System.out.println("▶ Message       : " + accessDeniedException.getMessage());
                            System.out.println("=================================================\n");
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println("\n==================[REQUEST LOG]==================");
                            System.out.println("❗ 인증 실패 (401)");
                            System.out.println("URL: " + request.getRequestURI());
                            System.out.println("IP: " + request.getRemoteAddr());
                            System.out.println("Message: " + authException.getMessage());
                            System.out.println("=================================================\n");
                        })
                );
        return http.build();
    }


}