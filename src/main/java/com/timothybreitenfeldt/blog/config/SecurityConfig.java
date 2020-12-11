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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
            httpSecurity.authorizeRequests()
                    .requestMatchers(new AntPathRequestMatcher("/api/admin/login", HttpMethod.POST.toString()))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/admin/register", HttpMethod.POST.toString()))
                    .permitAll().antMatchers("/api/admin/**").hasRole("ADMINISTRATOR");
        }

    }

    @Order(2)
    @Configuration
    public class AuthorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private AuthorDetailsServiceImpl authorDetailsServiceImpl;

        @Primary
        @Bean(name = "authorAuthenticationManager")
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
            httpSecurity.authorizeRequests()
                    .requestMatchers(new AntPathRequestMatcher("/api/login", HttpMethod.POST.toString())).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/register", HttpMethod.POST.toString())).permitAll()
                    .antMatchers("/api/**").hasRole("AUTHOR");
        }

    }

    @Order(3)
    @Configuration
    public class PublicAccessSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            httpSecurity.csrf().disable();
            httpSecurity.authorizeRequests()
                    .requestMatchers(new AntPathRequestMatcher("/api/posts/all/headers", HttpMethod.GET.toString()))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/api/author/posts/headers", HttpMethod.GET.toString()))
                    .permitAll();
        }

    }

}
