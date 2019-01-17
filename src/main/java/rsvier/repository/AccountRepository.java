package rsvier.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rsvier.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByUsername(String username);
}
