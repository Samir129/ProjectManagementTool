package com.mypersonalproject.projectmanagementtool.security;

import com.mypersonalproject.projectmanagementtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication){
        User user = (User)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime() + Constants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", (Long.toString(user.getId())));
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, Constants.SECRET)
                .compact();
    }
    
    //validate the token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(Constants.SECRET).parseClaimsJws(token);
            return true;
        } catch(SignatureException e){
            System.out.println("Invalid JWT Signature");
        } catch(MalformedJwtException e){
            System.out.println("Invalid JWT Token");
        } catch(ExpiredJwtException e){
            System.out.println("Expired JWT Token");
        } catch(UnsupportedJwtException e){
            System.out.println("Unsupported JWT Token");
        } catch(IllegalArgumentException e){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }
    
    //Get user Id from the token
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(Constants.SECRET).parseClaimsJws(token).getBody();
        return Long.parseLong((String)claims.get("id"));
    }
}
