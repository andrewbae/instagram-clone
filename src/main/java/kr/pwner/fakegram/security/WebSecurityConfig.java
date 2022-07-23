package kr.pwner.fakegram.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    public WebSecurityConfig(final JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        JwtInterceptor UserInterceptor = (JwtInterceptor) jwtInterceptor.clone();
        JwtInterceptor AuthInterceptor = (JwtInterceptor) jwtInterceptor.clone();

        registry.addInterceptor(UserInterceptor.setExcludeMethodList(Arrays.asList("GET", "POST")))
                .order(0).addPathPatterns("/api/*/account");
        registry.addInterceptor(AuthInterceptor.setExcludeMethodList(Arrays.asList("POST", "PUT")))
                .order(0).addPathPatterns("/api/*/auth");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}