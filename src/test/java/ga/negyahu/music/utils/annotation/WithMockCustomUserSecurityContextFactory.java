package ga.negyahu.music.utils.annotation;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.security.AccountContext;
import ga.negyahu.music.utils.TestUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithTestUser> {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SecurityContext createSecurityContext(WithTestUser testUser) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Account account = TestUtils.createDefaultAccount();
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        if (testUser != null) {
            setInputData(account, testUser);
        }

        Account save = this.accountRepository.save(account);
        AccountContext accountContext = new AccountContext(save,
            List.of(new SimpleGrantedAuthority(account.getRole().toString())));

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(accountContext, null,
                accountContext.getAuthorities());
        context.setAuthentication(authentication);

        return context;
    }

    public void setInputData(Account account, WithTestUser testUser) {
        if (!testUser.userEmail().isEmpty()) {
            account.setEmail(testUser.userEmail());
        }
        if (testUser.userId() != 0) {
            account.setId(testUser.userId());
        }
        if (!testUser.username().isEmpty()) {
            account.setUsername(testUser.username());
        }
        if (testUser.role() != Role.USER) {
            account.setUsername(testUser.username());
        }
        if (!testUser.mobile().isEmpty()) {
            account.setMobile(testUser.mobile());
        }
        if (!testUser.password().isEmpty()) {
            account.setPassword(passwordEncoder.encode(testUser.password()));
        }
        if (!testUser.nickname().isEmpty()) {
            account.setNickname(testUser.nickname());
        }
    }
}
