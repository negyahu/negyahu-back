package ga.negyahu.music.validator;

import static java.util.Objects.isNull;

import ga.negyahu.music.account.dto.AccountUpdateDto;
import ga.negyahu.music.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountUpdateDtoValidator implements Validator {

    private final AccountRepository accountRepository;
    private final MessageSource messageSource;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(AccountUpdateDto.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountUpdateDto dto = (AccountUpdateDto) target;
        String nickname = dto.getNickname();
        if (isNull(nickname)|| !accountRepository.existsByNickname(nickname)) {
            return;
        }
        errors.rejectValue("nickname", "error.duplicatedNickname");
    }
}
