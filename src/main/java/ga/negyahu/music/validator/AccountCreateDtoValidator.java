package ga.negyahu.music.validator;

import static java.util.Objects.*;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountCreateDtoValidator implements Validator {

    private final AccountRepository accountRepository;
    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountCreateDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountCreateDto dto = (AccountCreateDto) target;
        String inputEmail = dto.getEmail();
        String nickname = dto.getNickname();
        Account find = accountRepository.findFirstByEmailOrNickname(inputEmail, nickname);
        if(isNull(find)){
            return;
        }
        if (find.getEmail().equals(inputEmail)) {
            errors.rejectValue("email", "error.duplicatedEmail");
        }
        if(find.getNickname().equals(nickname)){
            errors.rejectValue("nickname", "error.duplicatedNickname");
        }

    }
}
