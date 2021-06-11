package ga.negyahu.music.security.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.security.AccountContext;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//@Service("userDetailsService")
@Component("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = accountRepository.findFirstByEmail(email)
            .orElseThrow(() -> { throw new UsernameNotFoundException("");});

        Role role = account.getRole();
        ArrayList<GrantedAuthority> roles = getRolesAsList(role);
        return new AccountContext(account, roles);
    }

    private ArrayList<GrantedAuthority> getRolesAsList(Role role) {
        ArrayList<GrantedAuthority> roles = new ArrayList();
        roles.add(new SimpleGrantedAuthority(role.toString()));
        return roles;
    }
}
