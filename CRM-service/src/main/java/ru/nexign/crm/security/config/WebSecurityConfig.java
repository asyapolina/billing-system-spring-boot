package ru.nexign.crm.security.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.nexign.crm.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userService;
    public final PasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;

    public WebSecurityConfig(UserDetailsServiceImpl userService, PasswordEncoder passwordEncoder, JwtFilter jwtFilter) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtFilter = jwtFilter;
    }

    @SneakyThrows
    @Override
    public void configure(HttpSecurity http) {
        http.csrf().disable().cors().disable()
                .authorizeRequests()
                .antMatchers("/auth/register", "/auth/login").not().fullyAuthenticated()
                .antMatchers("/manager/**").hasRole("MANAGER")
                .antMatchers("/abonent/**").hasRole("ABONENT")
                .anyRequest().authenticated().and()
                .exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }
}
