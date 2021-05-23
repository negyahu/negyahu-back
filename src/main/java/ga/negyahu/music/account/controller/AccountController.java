package ga.negyahu.music.account.controller;

import static ga.negyahu.music.account.controller.AccountController.ROOT_URI;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.account.dto.AccountOwnerDto;
import ga.negyahu.music.account.service.AccountService;
import ga.negyahu.music.mapstruct.AccountMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.validator.AccountCreateDtoValidator;
import java.net.URI;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ROOT_URI)
@RequiredArgsConstructor
public class AccountController {

    public static final String ROOT_URI = "/accounts";
    public final AccountMapper accountMapper = AccountMapper.INSTANCE;

    private final AccountService accountService;
    private final AccountCreateDtoValidator createDtoValidator;

    @InitBinder("accountCreateDto")
    public void binder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(createDtoValidator);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody @Valid AccountCreateDto accountCreateDto,
        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Account account = accountMapper.from(accountCreateDto);

        Account save = accountService.signUp(account);
        URI uri = WebMvcLinkBuilder.linkTo(AccountController.class).slash(save.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fetch(@PathVariable Long id, @LoginUser Account loginUser) {
        Account account = this.accountService.fetch(id);

        if (Objects.nonNull(loginUser) && Objects.equals(loginUser.getId(), account.getId())) {
            AccountOwnerDto dto = accountMapper.toOwnerDto(account);
            return ResponseEntity.ok().body(dto);
        }

        AccountDto accountDto = accountMapper.toDto(account);
        return ResponseEntity.ok().body(accountDto);
    }


}
