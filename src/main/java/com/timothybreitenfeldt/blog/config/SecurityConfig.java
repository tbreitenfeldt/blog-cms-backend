package com.timothybreitenfeldt.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.timothybreitenfeldt.blog.filter.AdministratorJWTFilter;
import com.timothybreitenfeldt.blog.filter.UserJWTFilter;
import com.timothybreitenfeldt.blog.service.AdministratorDetailsServiceImpl;
import com.timothybreitenfeldt.blog.service.UserDetailsServiceImpl;

@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Order(1)
    @Configuration
    public class UserSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private UserDetailsServiceImpl authorDetailsServiceImpl;

        @Autowired
        private UserJWTFilter userJWTFilter;

        @Primary
        @Bean(name = "userAuthenticationManager")
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(this.authorDetailsServiceImpl).passwordEncoder(SecurityConfig.this.passwordEncoder);
        }

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable();
            httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/api/login", "/api/register").permitAll();
            httpSecurity.requestMatchers().antMatchers(HttpMethod.POST, "/api/posts")
                    .antMatchers(HttpMethod.GET, "/api/author/posts/headers")
                    .antMatchers(HttpMethod.PUT, "/api/posts/{id}").and().authorizeRequests().anyRequest()
                    .hasRole("AUTHOR");

            httpSecurity.addFilterBefore(this.userJWTFilter, UsernamePasswordAuthenticationFilter.class);
        }

    }

    @Order(2)
    @Configuration
    public class AdministratorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AdministratorDetailsServiceImpl administratorDetailsServiceImpl;

        @Autowired
        private AdministratorJWTFilter administratorJWTFilter;

        @Bean(name = "administratorAuthenticationManager")
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
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
            httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/api/admin/login", "/api/admin/register")
                    .permitAll();
            httpSecurity.authorizeRequests().antMatchers("/api/admin/**").hasRole("ADMINISTRATOR");

            httpSecurity.addFilterBefore(this.administratorJWTFilter, UsernamePasswordAuthenticationFilter.class);
        }

    }

    @Order(3)
    @Configuration
    public class PublicAccessSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable();
            httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, "/api/posts/{id}", "/api/posts/all/headers")
                    .permitAll();
        }

    }

}
