package co.edu.unbosque.parcial.controller;

import co.edu.unbosque.parcial.dto.LoginRequest;
import co.edu.unbosque.parcial.dto.LoginResponse;
import co.edu.unbosque.parcial.dto.RegisterRequest;
import co.edu.unbosque.parcial.model.AppUser;
import co.edu.unbosque.parcial.repository.UserRepository;
import co.edu.unbosque.parcial.security.JwtService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserRepository users;
	private final PasswordEncoder encoder;

	public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository users,
			PasswordEncoder encoder) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.users = users;
		this.encoder = encoder;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
		try {
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			String role = "USER";
			for (GrantedAuthority authority : auth.getAuthorities()) {
				if (authority.getAuthority().equals("ROLE_ADMIN")) {
					role = "ADMIN";
				}
			}
			return ResponseEntity.ok(new LoginResponse(jwtService.generateToken(auth.getName()), auth.getName(), role));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Credenciales inválidas"));
		}
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
		String username = request.getUsername().trim();
		if (users.existsByUsername(username)) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Ese usuario ya existe"));
		}
		AppUser user = new AppUser();
		user.setUsername(username);
		user.setPassword(encoder.encode(request.getPassword()));
		user.setRole("USER");
		try {
			users.saveAndFlush(user);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Ese usuario ya existe"));
		}
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new LoginResponse(jwtService.generateToken(username), username, "USER"));
	}
}
