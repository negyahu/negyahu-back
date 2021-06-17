package ga.negyahu.music.security.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.security.AccountContext;
import ga.negyahu.music.utils.TestUtils;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class JwtTokenProviderTest {

    private JwtTokenProvider provider;

    private final String secretKey = "enVpamluIGhlbnhpYW5nZmFuZ3FpeWlxaWUgeWlqaW5naGVubGVpbGUgc2hlbm1lc2hpcWluZ2RvdWJ1eGluZ2dhbiBtYW1hIGJhYmEgamllamllIHdvemhlbmRlaGVubGVpbGUgbWVpeWlzaQo=";
    private final long time = 600L;

    private Account account;
    private AccountContext accountContext;

    @BeforeEach
    public void init() {
        this.provider = new JwtTokenProvider(secretKey, time);
        this.account = TestUtils.createDefaultAccount();
        this.account.setId(1L);
        this.accountContext = TestUtils.createAccountContext(account);
    }

    @Test
    public void 토큰생성_테스트() {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            this.accountContext, null, accountContext.getAuthorities());

        String jwt = this.provider.createToken(authenticationToken);
    }

    @Test
    public void 토큰변환_테스트() {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            this.accountContext, null, accountContext.getAuthorities());

        String jwt = this.provider.createToken(authenticationToken);

        Authentication authentication = this.provider.resolveToken(jwt);
        AccountContext context = (AccountContext) authentication.getPrincipal();
        Account account = context.getAccount();

        assertNotNull(this.account);
        assertEquals(this.account.getRole(), account.getRole());
    }

    @Test
    public void 유효기간_초과한_토큰()
        throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        // given : 토큰의 유효시간을 1밀리세컨트로 변환
        Field expireTime = JwtTokenProvider.class.getDeclaredField("expireTime");
        expireTime.setAccessible(true);
        expireTime.setLong(this.provider, Long.valueOf(1));

        // when : 토큰 발급, 토큰변환
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            this.accountContext, null, accountContext.getAuthorities());
        String token = this.provider.createToken(authenticationToken);

        // then : 토큰 유효기간이 지났기 때문에, false 반환
        boolean result = this.provider.validate(token);
        assertFalse(result);
    }

    @Test
    public void 토큰_확인_성공()
        throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        // when : 토큰 발급, 토큰변환
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            this.accountContext, null, accountContext.getAuthorities());
        String token = this.provider.createToken(authenticationToken);

        // then : 토큰 유효시간이 10분(600s)이기 때문에 true 반환
        boolean result = this.provider.validate(token);
        assertTrue(result);
    }

    @Test
    public void 토큰_실패_유요하지않은_토큰_실패()
        throws InterruptedException, NoSuchFieldException, IllegalAccessException {

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        // then : 토큰 유효시간이 10분(600s)이기 때문에 true 반환
        boolean result = this.provider.validate(token);
        assertFalse(result);
    }


}