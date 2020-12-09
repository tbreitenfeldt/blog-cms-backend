package com.timothybreitenfeldt.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timothybreitenfeldt.blog.service.AdministratorDetailsServiceImpl;
import com.timothybreitenfeldt.blog.service.AuthorDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Order(1)
    @Configuration
    public class AdministratorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AdministratorDetailsServiceImpl administratorDetailsServiceImpl;

        @Bean
        @Qualifier("administratorAuthenticationManager")
        public AuthenticationManager administratorAuthenticationManager() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(this.administratorDetailsServiceImpl)
                    .passwordEncoder(SecurityConfig.this.passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable();
            httpSecurity.authorizeRequests().antMatchers("/api/admin/**").hasRole("ADMINISTRATOR");
        }

    }

    @Order(2)
    @Configuration
    public class AuthorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthorDetailsServiceImpl authorDetailsServiceImpl;

        @Bean
        @Qualifier("authorAuthenticationManager")
        @Primary
        public AuthenticationManager authorAuthenticationManager() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(this.authorDetailsServiceImpl).passwordEncoder(SecurityConfig.this.passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable();
            httpSecurity.authorizeRequests().antMatchers("/api/author/**").hasRole("AUTHOR");
        }

    }

    @Order(3)
    @Configuration
    public class PublicAccessSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable();
            httpSecurity.authorizeRequests().antMatchers("/api/**").permitAll();
        }

    }

}
