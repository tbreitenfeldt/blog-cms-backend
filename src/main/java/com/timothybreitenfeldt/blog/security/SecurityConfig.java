package com.timothybreitenfeldt.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImpl).passwordEncoder(SecurityConfig.this.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.cors().and().authorizeRequests()
                // Author Role
                .antMatchers(HttpMethod.POST, "/api/posts").hasRole("AUTHOR")
                .antMatchers(HttpMethod.GET, "/api/author/posts/headers").hasRole("AUTHOR")
                .antMatchers(HttpMethod.PUT, "/api/posts/{id}").hasRole("AUTHOR")
                .antMatchers(HttpMethod.DELETE, "/api/posts/{id}").hasRole("AUTHOR")
                // Administrator Role
                .antMatchers(HttpMethod.DELETE, "/api/admin/posts/{id}").hasRole("ADMINISTRATOR")
                .antMatchers(HttpMethod.PUT, "/api/admin/posts/{id}").hasRole("ADMINISTRATOR")
                // publicly accessible
                .antMatchers(HttpMethod.POST, "/api/login", "/api/register").permitAll()
                .antMatchers(HttpMethod.GET, "/api/posts/{id}", "/api/posts/all/headers").permitAll();

        httpSecurity.addFilterBefore(SecurityConfig.this.jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

}
