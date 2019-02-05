package rsvier.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import rsvier.domain.Account;
import rsvier.domain.AccountRole;
import rsvier.domain.ApiResponse;
import rsvier.repository.AccountRepository;
import rsvier.security.JWTAuthenticationResponse;
import rsvier.security.JWTTokenProvider;

@RestController
public class LoginController {

	AuthenticationManager authenticationManager;
	JWTTokenProvider jwtTokenProvider;
	AccountRepository accountRepository;
	PasswordEncoder passwordEncoder;

	public LoginController(AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider,
			AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateAccount(@Valid @RequestBody Account account) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(account.getUsername(), account.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JWTAuthenticationResponse(jwt));
	}

	@PostMapping("/account/register")
	public ResponseEntity<ApiResponse> register(@Valid @RequestBody Account account) {
		if (accountRepository.findByUsername(account.getUsername()) != null) {
			return new ResponseEntity<ApiResponse>(new ApiResponse(400, "Username does already exist", null), HttpStatus.BAD_REQUEST);
		}
		if (account.getAccountRole() == null) {
			// TODO: need different way to handle default role
			account.setAccountRole(new AccountRole(3L, "klant"));
		}
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		accountRepository.save(account);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/account/list")
				.buildAndExpand(account.getUsername()).toUri();
		return ResponseEntity.created(location).body(new ApiResponse(200, "User succesfully registered", null));
	}

}
