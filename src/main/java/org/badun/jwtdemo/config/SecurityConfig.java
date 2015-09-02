package org.badun.jwtdemo.config;

import org.badun.jwtdemo.service.security.ApiAuthenticationFilter;
import org.badun.jwtdemo.service.security.JwtAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by Artsiom Badun.
 */
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private ApiAuthenticationFilter apiAuthenticationFilter;
    @Autowired
    private JwtAuthenticationProvider apiAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                    .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                .addFilterAfter(apiAuthenticationFilter, BasicAuthenticationFilter.class)
                .authenticationProvider(apiAuthenticationProvider)
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("111").roles("USER").and()
                .withUser("admin").password("111").roles("USER", "ADMIN");
    }
}