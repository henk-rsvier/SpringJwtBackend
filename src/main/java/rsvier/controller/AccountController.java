package rsvier.controller;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rsvier.domain.Account;
import rsvier.domain.AccountRole;
import rsvier.repository.AccountRepository;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	private AccountRepository accountRepository;
	private PasswordEncoder passwordEncoder;
	
	public AccountController(AccountRepository applicationUserRepository,
			PasswordEncoder passwordEncoder) {
		this.accountRepository = applicationUserRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping("/list")
	public List<Account> getAccountList() {
		return accountRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Account getAccountById(@PathVariable long id) {
		System.out.println("IN GETACCOUNTBYID: " + id);
		// TODO check how to handle Optional correctly here
		return accountRepository.findById(id).get();
	}
	
	@PutMapping("/{id}")
	public Account updateAccount(@RequestBody Account account) {
		return accountRepository.save(account);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable long id) {
		accountRepository.deleteById(id);
	}
}
