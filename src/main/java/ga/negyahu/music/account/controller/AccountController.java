package ga.negyahu.music.account.controller;

import static ga.negyahu.music.account.controller.AccountController.ROOT_URI;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.account.dto.AccountOwnerDto;
import ga.negyahu.music.account.dto.AccountUpdateDto;
import ga.negyahu.music.account.service.AccountService;
import ga.negyahu.music.mapstruct.AccountMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.validator.AccountCreateDtoValidator;
import ga.negyahu.music.validator.AccountUpdateDtoValidator;
import java.net.URI;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ROOT_URI,consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    public static final String ROOT_URI = "/api/accounts";
    public final AccountMapper accountMapper = AccountMapper.INSTANCE;

    private final AccountService accountService;
    private final AccountCreateDtoValidator createDtoValidator;
    private final AccountUpdateDtoValidator updateDtoValidator;

    @InitBinder("accountCreateDto")
    public void binder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(createDtoValidator);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid AccountCreateDto accountCreateDto,
        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Account account = accountMapper.from(accountCreateDto);

        Account save = accountService.signUp(account);
        URI uri = WebMvcLinkBuilder.linkTo(AccountController.class).slash(save.getId()).toUri();
        AccountDto accountDto = this.accountMapper.toDto(save);
        return ResponseEntity.created(uri).body(accountDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity fetch(@PathVariable Long id, @LoginUser Account loginUser) {
        Account account = this.accountService.fetch(id);

        if (Objects.nonNull(loginUser) && Objects.equals(loginUser.getId(), account.getId())) {
            AccountOwnerDto dto = accountMapper.toOwnerDto(account);
            return ResponseEntity.ok().body(dto);
        }

        AccountDto accountDto = accountMapper.toDto(account);
        return ResponseEntity.ok().body(accountDto);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity patch(@PathVariable Long id, @LoginUser Account loginUser,
        @Valid @RequestBody AccountUpdateDto accountDto,Errors errors) {
        // Check owner
        if(Objects.isNull(loginUser) || !Objects.equals(loginUser.getId(),id)) {
            return ResponseEntity.status(403).build();
        }
        // Form validation
        updateDtoValidator.validate(accountDto,errors);
        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        // Update
        Account account = accountMapper.from(accountDto);
        account.setId(id);
        Account update = accountService.update(account);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public String testMethod(@PathVariable Long id){
        return id.toString();
    }

}
