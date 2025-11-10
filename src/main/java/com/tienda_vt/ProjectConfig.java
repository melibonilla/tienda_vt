package com.tienda_vt;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class ProjectConfig implements WebMvcConfigurer {

    /* Los siguiente métodos son para implementar el tema de seguridad dentro del proyecto */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/ejemplo2").setViewName("ejemplo2");
        registry.addViewController("/multimedia").setViewName("multimedia");
        registry.addViewController("/iframes").setViewName("iframes");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/registro/nuevo").setViewName("/registro/nuevo");
    }

    /* El siguiente método se utilizar para publicar en la nube, independientemente  */
    @Bean
    public SpringResourceTemplateResolver templateResolver_0() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setOrder(0);
        resolver.setCheckExistence(true);
        return resolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        var slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.getDefault());
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        var lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registro) {
        registro.addInterceptor(localeChangeInterceptor());
    }

    //Bean para poder acceder a los messages.properties en código...
    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Value("${firebase.json.path}")
    private String jsonPath;

    @Value("${firebase.json.file}")
    private String jsonFile;

    @Bean
    public Storage storage() throws IOException {
        ClassPathResource resource = new ClassPathResource(jsonPath + File.separator + jsonFile);
        try (InputStream inputStream = resource.getInputStream()) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        }
    }

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
    public  SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(requests -> requests
        .requestMatchers(PUBLIC_URLS).permitAll()
        .requestMatchers(USUARIO_URLS).hasRole("USUARIO")
        .requestMatchers(VENDEDOR_URLS).hasAnyRole("VENDEDOR", "ADMIN")
        .requestMatchers(ADMIN_URLS).hasRole("ADMIN"));
        
         http.formLogin(login -> login.loginPage("/login")
        .loginProcessingUrl("/login")
        .defaultSuccessUrl("/",true)
        .defaultSuccessUrl("/",true)
        .failureUrl("/login?error=true")
         .permitAll());
                 
         http.logout(logout -> logout.logoutUrl ("/logout")
         .logoutSuccessUrl("/login?logout=true")
         .invalidateHttpSession(true)
         .deleteCookies("JSESSIONID")
         .permitAll());

         http.exceptionHandling(exception -> exception.accessDeniedPage("/acceso_denegado"));
         http.sessionManagement(session ->session.maximumSessions(1)
         .maxSessionsPreventsLogin(false));
         
        return http.build();
    }
    
    //Método para crear el procedimiento de encriptación
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    //Método para crear usuarios "en memoria" la próxima semana ya no se usa...
    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        UserDetails juan= User.builder().username("juan")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN").build();
         UserDetails rebeca= User.builder().username("rebeca")
                .password(passwordEncoder.encode("456"))
                .roles("VENDEDOR").build();
         UserDetails pedro= User.builder().username("pedro")
                .password(passwordEncoder.encode("789"))
                .roles("USUARIO").build();
         return new InMemoryUserDetailsManager(juan,rebeca,pedro);
    }
}
