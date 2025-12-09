package com.handson.basic.jwt;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    // הסיסמה שמורה ב־DB (לצורך הדוגמה פשוט במפה)
    private Map<String, DBUser> users = new HashMap<>();

    private final String SECRET_KEY = "mySecretKey";

    public void save(DBUser user) {
        users.put(user.getUsername(), user);
    }

    public String authenticate(String username, String password) {
        DBUser user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return generateToken(user);
        }
        return null;
    }

    private String generateToken(DBUser user) {
        long expirationTime = 1000 * 60 * 60; // 1 hour
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
