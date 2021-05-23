package ga.negyahu.music.security.utils;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.security.AccountContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    public static final String SUB = "Authentication";
    public static final String AUTHENTICATION_KEY = "auth";
    public static final String HOST = "http//:www.negyahu.ga";

    private final String secret;
    private final long expireTime;
    private Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
        @Value("${jwt.expire-time}") Long expireTime) {
        this.secret = secret;
        this.expireTime = expireTime * 1000L;

        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Authentication authentication) {

        AccountContext context = (AccountContext) authentication.getPrincipal();
        Account account = context.getAccount();

        return Jwts.builder()
            .setSubject(SUB)
            .setIssuer(HOST)
            .setAudience(account.getEmail())
            .claim(AUTHENTICATION_KEY, account.getRole().name())
            .claim("nickname", account.getNickname())
            .claim("id", account.getId())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(parseToDate())
            .compact();
    }

    private Date parseToDate() {
        long now = (new Date().getTime());
        return new Date(now + this.expireTime);
    }

    public Authentication resolveToken(String token) {

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        String email = (String) claims.get("aud");
        String role = (String) claims.get("auth");
        String nickname = (String) claims.get("nickname");

        Account account = Account.builder()
            .role(Role.valueOf(role))
            .email(email)
            .nickname(nickname)
            .build();

      return getUserToken(account);
    }
    private UsernamePasswordAuthenticationToken getUserToken(Account account){
        AccountContext accountContext = new AccountContext(account,
            List.of(new SimpleGrantedAuthority(account.getRole().toString())));

        return new UsernamePasswordAuthenticationToken(accountContext, null,
            accountContext.getAuthorities());
    }

    public boolean validate(String token) {
        Claims claims;
        // 기간이 초과한 토큰이거나 유효하지 않을 경우 false 반환
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        } catch (Exception e) {
            return false;
        }
        // 토큰의 발행처를 확인한다.
        String iss = (String) claims.get("iss");
        if (Objects.isNull(iss) || !iss.equals(HOST)) {
            return false;
        }
        // 인증용 토큰인지 확인한다.
        String sub = (String) claims.get("sub");
        if (Objects.isNull(sub) || !sub.equals(SUB)) {
            return false;
        }
        return true;
    }

}
