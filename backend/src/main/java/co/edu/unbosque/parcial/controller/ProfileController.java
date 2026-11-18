package co.edu.unbosque.parcial.controller;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

	@GetMapping
	public Map<String, String> profile(Authentication authentication) {
		return Map.of("message", "Sesión válida para " + authentication.getName());
	}
}
