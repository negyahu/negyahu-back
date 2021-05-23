package ga.negyahu.music.security;

import ga.negyahu.music.account.Account;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
@Getter
public class AccountContext extends User {

    private Account account;

    public AccountContext(Account account,
        Collection<? extends GrantedAuthority> authorities) {
        super(account.getEmail(),account.getPassword(), authorities);
        this.account = account;
    }
}
