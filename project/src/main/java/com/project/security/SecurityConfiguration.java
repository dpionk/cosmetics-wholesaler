package com.project.security;

import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;


    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        List<com.project.domains.User> users = new ArrayList<com.project.domains.User>();
        users.addAll((Collection<? extends com.project.domains.User>) userRepository.findAll());

        InMemoryUserDetailsManager inMemoryManager = new InMemoryUserDetailsManager();

        for (com.project.domains.User user : users) {
            UserDetails userDetails = null;
            if (user.getIs_admin()) {
                userDetails = User.withUsername(user.getUsername()).password(passwordEncoder.encode(user.getPassword()))
                        .roles("ADMIN", "USER").build();
            } else if (!user.getIs_admin()) {
                userDetails = User.withUsername(user.getUsername()).password(passwordEncoder.encode(user.getPassword()))
                        .roles("USER").build();
            }
            if (userDetails != null) {
                inMemoryManager.createUser(userDetails);
            }
        }
        return inMemoryManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        authorize all requests
        http.authorizeRequests()
//               allow only if user is an admin
                .antMatchers("/users/**").hasRole("ADMIN")
                .antMatchers("/categories/**").hasRole("ADMIN")
                .antMatchers("/administration").hasRole("ADMIN")
                .antMatchers("/cosmetics").hasAnyRole("ADMIN", "USER")
                .antMatchers("/cosmetics/{id}").hasAnyRole("ADMIN", "USER")
                .antMatchers("/cosmetics/{id}/**").hasRole("ADMIN")

                .antMatchers("/css/**").permitAll()

                .antMatchers("/**").authenticated()
                .and().formLogin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}
