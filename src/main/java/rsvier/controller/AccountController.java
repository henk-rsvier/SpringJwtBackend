package rsvier.controller;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		return accountRepository.findAll();
	}
	
	// TODO: implement correct error handling
	@PostMapping("/register") 
	public boolean register(@RequestBody Account account) {
		if (accountRepository.findByUsername(account.getUsername()) == null) {
			account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
			accountRepository.save(account);
			return true;
		}
		return false;
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
