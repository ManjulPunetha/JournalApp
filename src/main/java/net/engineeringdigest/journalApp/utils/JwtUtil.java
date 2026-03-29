package net.engineeringdigest.journalApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    // Secret key generation

    /**
     * Secret key generation
     *
     * @return Secret key encrypted using SHA algorithm
     */
    private Key getSigningKey() {
        // Secret must be at least 32 bytes (256 bits) for HMAC SHA256
        byte[] keyBytes = "yourSecretKeyHereAtLeast32BytesLong".getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
        // Keys.hmacShaKeyFor() converts byte[] → SecretKey object needed by signWith()
    }

    //Token generation
    public String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .addClaims(claims)      // Payload custom data
                .setSubject(userName)   // "sub" claim = unique identifier
                .setHeaderParam("typ", "JWT")  // Header type
                .setIssuedAt(new Date())     // "iat" = current timestamp
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 1000)) // 2 minutes expiry (in milliseconds)
                .signWith(getSigningKey())          // Sign with HMAC SHA256 using secret key
                .compact();     // Build and return the JWT string
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        // Add extra claims here if needed, e.g.: claims.put("email", email);
        return createToken(claims, username);
    }

    //Extract Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())   // Re-sign with same key to verify
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpirationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    //Token Validation (Optimized Version)
    // Optimized: Only check expiry (no redundant DB lookup needed)
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
}
