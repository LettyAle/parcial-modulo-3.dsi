package co.edu.unbosque.parcial.config;

import co.edu.unbosque.parcial.model.AppUser;
import co.edu.unbosque.parcial.repository.UserRepository;
import co.edu.unbosque.parcial.security.JwtAuthFilter;
import co.edu.unbosque.parcial.security.JwtService;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService, UserDetailsService userDetailsService)
			throws Exception {
		http.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
			@Override
			public void customize(CsrfConfigurer<HttpSecurity> csrf) {
				csrf.disable();
			}
		});
		http.cors(Customizer.withDefaults());
		http.sessionManagement(new Customizer<SessionManagementConfigurer<HttpSecurity>>() {
			@Override
			public void customize(SessionManagementConfigurer<HttpSecurity> session) {
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			}
		});
		http.authorizeHttpRequests(
				new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
					@Override
					public void customize(
							AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
						auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
						auth.requestMatchers("/api/auth/**").permitAll();
						auth.anyRequest().authenticated();
					}
				});
		http.exceptionHandling(new Customizer<ExceptionHandlingConfigurer<HttpSecurity>>() {
			@Override
			public void customize(ExceptionHandlingConfigurer<HttpSecurity> exceptions) {
				exceptions.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
			}
		});
		http.addFilterBefore(new JwtAuthFilter(jwtService, userDetailsService),
				UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository repository) {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Optional<AppUser> result = repository.findByUsername(username);
				if (result.isEmpty()) {
					throw new UsernameNotFoundException("Usuario no encontrado");
				}
				AppUser user = result.get();
				return User.withUsername(user.getUsername()).password(user.getPassword()).roles(user.getRole()).build();
			}
		};
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource(@Value("${app.cors.allowed-origin}") String origin) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(origin));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
