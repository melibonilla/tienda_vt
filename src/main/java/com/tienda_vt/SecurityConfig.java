package com.tienda_vt;


import com.tienda_vt.domain.Ruta;
import com.tienda_vt.service.RutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    //El siguiente arreglo contiene las rutas públicas que todos pueden acceder
    public static final String[] PUBLIC_URLS = {
        "/", "/index", "/consultas/**", "/carrito/**", "/registro/**", "/fav/**", "/js/**", "/css/**", "/webjars/**", "/login", "/acceso_denegado"
    };

    //El siguiente arreglo contiene las rutas QUE UN USUARIO SIMPLE puede acceder
    public static final String[] USUARIO_URLS = {"/facturar/carrito"};

    //El siguiente arreglo contiene las rutas QUE UN VENDERDOR puede acceder
    public static final String[] VENDEDOR_URLS = {
        "/categoria/listado", "/producto/listado"
    };

    //El siguiente arreglo contiene las rutas QUE UN ADMINISTRADOR puede acceder
    public static final String[] ADMIN_URLS = {
        "/categoria/**", "/producto/**", "/admin/**", "/usuario/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy RutaService rutaService)
            throws Exception {
        
        var rutas = rutaService.getRutas();
        http.authorizeHttpRequests(requests -> {
            for (Ruta ruta: rutas) {
                if(ruta.isRequiereRol()) {
                    requests.requestMatchers(ruta.getRuta()).hasRole(ruta.getRol().getRol());
                } else {
                    requests.requestMatchers(ruta.getRuta()).permitAll();
                }
            }
            requests.anyRequest().authenticated();
        });
               

        http.formLogin(login -> login.loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll());

        http.logout(logout -> logout.logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());

        http.exceptionHandling(exception -> exception.accessDeniedPage("/acceso_denegado"));
        http.sessionManagement(session -> session.maximumSessions(1)
                .maxSessionsPreventsLogin(false));

        return http.build();
    }

    //Método para crear el procedimiento de encriptación
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build,
            @Lazy PasswordEncoder passwordEncoder,
            @Lazy UserDetailsService userDetailsService) throws Exception {   
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
