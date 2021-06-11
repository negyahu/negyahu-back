package ga.negyahu.music.account.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.event.SignUpEvent;
import ga.negyahu.music.exception.AccountNotFoundException;
import ga.negyahu.music.mapstruct.AccountMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private AccountMapper accountMapper = AccountMapper.INSTANCE;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public Account signUp(Account account) {
        String encode = passwordEncoder.encode(account.getPassword());
        account.setPassword(encode);
        generateCode(account);

        Account save = this.accountRepository.save(account);
        eventPublisher.publishEvent(new SignUpEvent(save));
        return save;
    }

    private void generateCode(Account account) {
        String uuid = UUID.randomUUID().toString();
        String until = LocalDateTime.now().plusDays(1).format(formatter);
        account.setCertifyCode(until + "." + uuid);
    }

    @Override
    public Account fetch(Long id) {
        return accountRepository.findFirstByIdAndStateIsNot(id, State.DELETED).orElseThrow(() -> {
            throw new AccountNotFoundException();
        });
    }

    @Override
    public Account update(Account account) {
        Account fetch = fetch(account.getId());
        if (account.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        accountMapper.map(account, fetch);
        return fetch;
    }

    @Override
    public void delete(Long id) {
        Account account = this.fetch(id);
        account.setState(State.DELETED);
    }

    @Override
    public boolean certifyEmailCode(Long id, String code) {
        Account account = this.accountRepository.findById(id)
            .orElseThrow(() -> {
                throw new AccountNotFoundException();
            });
        String certifyCode = account.getCertifyCode();

        boolean result = validateCode(certifyCode, code);

        // 유효한 코드라면 true 로 변경
        if (result) {
            account.setCertifiedEmail(true);
            account.setCertifyCode(null);
        }
        return result;
    }

    private boolean validateCode(String original, String input) {
        // DB에 저장되어 있는 코드가 존재하는지 체크
        if (Objects.isNull(original) || !StringUtils.hasText(original)) {
            return false;
        }
        // 유효기간을 체크
        String[] originalCodes = original.split("\\.");
        LocalDateTime until = LocalDateTime.parse(originalCodes[0], formatter);
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(until)){
            return false;
        }
        // Code 가 같은지 체크
        if(!input.split("\\.")[1].equals(originalCodes[1])){
            return false;
        }
        return true;
    }
}
