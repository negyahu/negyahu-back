package ga.negyahu.music.utils;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.entity.Address;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.service.AccountService;
import ga.negyahu.music.security.AccountContext;
import ga.negyahu.music.security.utils.JwtTokenProvider;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class TestUtils {

    public static final String DEFAULT_EMAIL = "yangfriendship.dev@gmail.com";
    public static final String DEFAULT_NAME = "양우정";
    public static final String DEFAULT_NICKNAME = "갓우정";
    public static final String DEFAULT_PASSWORD = "dbwjd123";
    public static final Address DEFAULT_ADDRESS = new Address("02058", "서울시 성북구 북악산로 1111",
        "성신여대입구역 5번출구");

    private final AccountService accountService;
    private final JwtTokenProvider provider;

    public TestUtils(AccountService accountService,
        JwtTokenProvider provider) {
        this.accountService = accountService;
        this.provider = provider;
    }

    public static final Account createDefaultAccount() {
        return Account.builder()
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .password(DEFAULT_PASSWORD)
            .mobile("01011112222")
            .username(DEFAULT_NAME)
            .role(Role.USER)
            .build();
    }

    public static final List<Account> createAccounts(int index) {
        List<Account> result = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            Account account = Account.builder()
                .email(String.format("email%d@email.com", i))
                .address(DEFAULT_ADDRESS)
                .mobile(String.format("0101111%04d", i))
                .password(DEFAULT_PASSWORD)
                .username(DEFAULT_NAME)
                .role(Role.USER)
                .build();
            result.add(account);
        }
        return result;
    }

    public static final AccountCreateDto createAccountCreateDto() {
        return AccountCreateDto.builder()
            .password(DEFAULT_PASSWORD)
            .email(DEFAULT_EMAIL)
            .username(DEFAULT_NAME)
            .build();
    }

    public static AccountContext createAccountContext(Account account) {
        Role role = account.getRole();
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role.toString()));
        return new AccountContext(account, roles);
    }

    public Account signUpAccount(Account account) {
        Account account1 = accountService.signUp(account);
        return account1;
    }

    public String signSupAndLogin(Account account) {
        Account save = signUpAccount(account);
        AccountContext accountContest = createAccountContext(save);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            accountContest, null, accountContest.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return provider.createToken(authenticationToken);
    }

    public String loginAccount(Account account) {
        AccountContext accountContext = createAccountContext(account);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            accountContext, null, accountContext.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return provider.createToken(authenticationToken);
    }

}
