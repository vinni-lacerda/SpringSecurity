package study.spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private SecurityDatabaseService securityService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/login").permitAll()
                        .requestMatchers("/users").hasAnyRole("USERS", "MANAGERS")
                        .requestMatchers("/managers").hasRole("MANAGERS")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.withUsername("user")
//                .password("{noop}user123")
//                .roles("USERS")
//                .build();
//
//        UserDetails admin = User.withUsername("admin")
//                .password("{noop}master123")
//                .roles("MANAGERS")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}