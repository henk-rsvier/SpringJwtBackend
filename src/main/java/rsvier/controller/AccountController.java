package rsvier.controller;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rsvier.domain.Account;
import rsvier.repository.AccountRepository;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	private AccountRepository accountRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public AccountController(AccountRepository applicationUserRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.accountRepository = applicationUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@GetMapping("/list")
	public List<Account> getAccountList() {
		System.out.println("IN GETACCOUNTLIST: " + accountRepository.findAll());
		return accountRepository.findAll();
	}
	
	// TODO: create self-register functionality
	@PostMapping("/sign-up") 
	public void signUp(@RequestBody Account user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		accountRepository.save(user);
	}
}
