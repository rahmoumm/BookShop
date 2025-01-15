package BookShop.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(customise -> customise.disable());

        // Here it says that any request for it to pass, the principal needs to be authenticated
        // but even if you pass the real username/password it won't pass. WHY??
        // => simply because the filterchain does not know how would you like to configure your auth
        // ==> You need to mention it also
        http
                .authorizeHttpRequests((authorize) -> authorize
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/checkLogin").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/nonAuth/**").permitAll()
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

//                        .requestMatchers("/books/reserved/**").hasAuthority("SELLER")
                        .anyRequest().authenticated()
                );
        http.cors( (cors) -> cors.configurationSource(corsConfigurationSource()) );
        http.headers(customise -> customise.frameOptions(cus -> cus.sameOrigin()));
        // Here you tell it to use the form login and processe username and password in the request
        // http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        log.info("i am in the auth provider");
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    @Bean
    static RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("ADMIN").implies("SELLER")
                .role("SELLER").implies("USER")
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Allow all origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow all HTTP methods
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers
        // to allow the location to be exposed after a creation of a ressource (used in creating a book that does not exist when we
        // want to add it in our stock )
        configuration.addExposedHeader("Location");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

//    @Bean
//    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
//        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy);
//        return expressionHandler;
//    }

}
