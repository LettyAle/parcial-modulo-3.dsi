package co.edu.unbosque.parcial.config;

import co.edu.unbosque.parcial.model.AppUser;
import co.edu.unbosque.parcial.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoadUsers implements CommandLineRunner {
	private final UserRepository users;
	private final PasswordEncoder encoder;

	public LoadUsers(UserRepository users, PasswordEncoder encoder) {
		this.users = users;
		this.encoder = encoder;
	}

	@Override
	public void run(String... args) {
		createIfMissing("admin", "admin123", "ADMIN");
		createIfMissing("user", "user123", "USER");
	}

	private void createIfMissing(String username, String password, String role) {
		if (users.existsByUsername(username)) {
			return;
		}
		AppUser user = new AppUser();
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setRole(role);
		users.save(user);
	}
}
