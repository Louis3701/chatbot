package org.ldv.chatbot.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain {

        http
            .authorizeHttpRequests { auth ->
                auth
                    // Ressources statiques accessibles aux visiteurs
                    .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/images/**",
                        "/vendor/**"
                    ).permitAll()

                    // Pages réservées aux administrateurs (ROLE_ADMIN)
                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")

                    // Toutes les autres routes demandent une connexion
                    // (anyRequest doit rester en dernier)
                    .anyRequest()
                    .authenticated()
            }
            .formLogin { form ->
                form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", false)
                    .failureUrl("/login?error")
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            }

        return http.build()
    }
}