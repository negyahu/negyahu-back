package ga.negyahu.music.account.controller;

import static ga.negyahu.music.account.controller.AccountController.ROOT_URI;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.account.dto.AccountOwnerDto;
import ga.negyahu.music.account.dto.AccountUpdateDto;
import ga.negyahu.music.account.service.AccountService;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.mapstruct.AccountMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.swagger.annotation.AccountIDParam;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ROOT_URI)
@RequiredArgsConstructor
@ApiOperation(value = ROOT_URI, tags = {"Account API"})
public class AccountController {

    public static final String ROOT_URI = "/api/accounts";
    public final AccountMapper accountMapper = AccountMapper.INSTANCE;

    private final AccountService accountService;

    @ApiOperation(value = "?????? ??????", notes = "????????? ????????? ????????? ?????? ????????? ??????")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "????????? ?????? ?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "AgencyDto", implementation = AccountDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
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

    @ApiOperation(value = "?????? ??????", notes = "????????? ?????? API, ?????? ????????? ????????? ??????, \"mobile\", \"address\", \"role\", \"isMembership\" ????????? ????????? ??????")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(name = "AgencyDto", implementation = AccountDto.class))
        ),
        @ApiResponse(responseCode = "400", description = "?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @AccountIDParam
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


    @ApiOperation(value = "???????????? ??????", notes = "???????????? ??????")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
        @ApiResponse(responseCode = "400", description = "?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @AccountIDParam
    @PatchMapping(value = "/{id}")
    public ResponseEntity patch(@PathVariable Long id, @LoginUser Account loginUser,
        @Valid @RequestBody AccountUpdateDto accountDto, Errors errors) {
        // Check owner
        if (Objects.isNull(loginUser) || !Objects.equals(loginUser.getId(), id)) {
            return ResponseEntity.status(403).build();
        }

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // Update
        Account account = accountMapper.from(accountDto);
        account.setId(id);
        Account update = accountService.update(account);

        return ResponseEntity.noContent().build();
    }

    @AccountIDParam
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return id.toString();
    }


    @ApiOperation(value = "????????? ??????", notes = "????????? ??????")
    @ApiImplicitParams(
        {@ApiImplicitParam(dataTypeClass = String.class, name = "code", example = "String of UUID"
            , value = "??????????????? ???????????? ?????? ????????? ??????", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", example = "1", value = "??????????????? ????????????"
                , defaultValue = "1", dataTypeClass = Long.class, required = true, paramType = "path")}
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))),
        @ApiResponse(responseCode = "400", description = "?????? ??????"
            , content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ResultMessage.class))
        )
    })
    @GetMapping(value = "/{id}/email")
    public ResponseEntity certifyEmail(@PathVariable Long id, @RequestParam("code") String code) {
        try {
            boolean result = this.accountService.certifyEmailCode(id, code);
            if (result) {
                return ResponseEntity.ok().body(ResultMessage.createSuccessMessage("????????? ?????? ??????"));
            }
            return ResponseEntity.badRequest()
                .body(ResultMessage.createFailMessage("[ERROR] ????????? ????????? ????????????."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ResultMessage.createFailMessage("[ERROR] ????????? ????????? ????????????."));
        }
    }
}
