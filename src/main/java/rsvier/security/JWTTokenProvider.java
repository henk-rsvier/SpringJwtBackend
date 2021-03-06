package rsvier.security;

import static rsvier.security.SecurityConstants.EXPIRATION_TIME;
import static rsvier.security.SecurityConstants.SECRET;
import static rsvier.security.SecurityConstants.TOKEN_PREFIX;

import java.security.Principal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import rsvier.domain.Account;

@Component
public class JWTTokenProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);
	
	
	public String generateToken(Authentication authentication) {
		
		User user = (User)authentication.getPrincipal();
				
		return Jwts.builder()
				.setSubject(user.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();		
	}
	
	public String getUsernameFromJWT(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				.getBody()
				.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token.replace(TOKEN_PREFIX, ""));
			return true;
		} catch (SignatureException ex) {
			LOG.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
            LOG.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            LOG.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            LOG.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            LOG.error("JWT claims string is empty.");
}
		return false;	
	}
		
}
