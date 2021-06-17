package ga.negyahu.music.account.controller;

import static ga.negyahu.music.account.controller.AccountController.ROOT_URI;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountUpdateDto;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.account.service.AccountService;
import ga.negyahu.music.account.service.AccountServiceImpl;
import ga.negyahu.music.exception.AccountNotFoundException;
import ga.negyahu.music.utils.TestUtils;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@CustomSpringBootTest
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TestUtils testUtils;

    @Test
    public void 회원가입_성공() throws Exception {
        // given
        AccountCreateDto createDto = TestUtils.createAccountCreateDto();
        // when
        ResultActions step1 = this.mockMvc.perform(post(ROOT_URI).contentType(APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(createDto))
        );
        // then : 201 반환
        step1.andExpect(status().isCreated());
        // then : PasswordEncode, Default role is 'user'
        assertDoesNotThrow(() -> {
                Account account = accountRepository.findFirstByEmail(createDto.getEmail()).get();
                assertNotNull(account.getId());
                assertEquals(createDto.getEmail(), account.getEmail());
                assertTrue(passwordEncoder.matches(createDto.getPassword(), account.getPassword()));
                assertEquals(Role.USER, account.getRole());
            }
        );
    }

    @Test
    public void 회원가입_이미사용중인_이메일() throws Exception {

        // given : 이미 동일한 이메일로 계정이 존재한다.
        Account account = TestUtils.createDefaultAccount();
        Account save = testUtils.signUpAccount(account);

        AccountCreateDto createDto = TestUtils.createAccountCreateDto();

        // when : 동일한 이메일로 회원가입 요청
        ResultActions step1 = this.mockMvc.perform(post(ROOT_URI).contentType(APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(createDto))
        );

//        step1.andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].field").exists())
        ;
    }

    @Test
    public void 본인_계정_조회() throws Exception {
        Account account = TestUtils.createDefaultAccount();
        String token = testUtils.signSupAndLogin(account);
        ResultActions step1 = this.mockMvc.perform(get(ROOT_URI + "/{id}", account.getId())
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, token)
        );

        step1.andExpect(jsonPath("password").doesNotExist())
            .andExpect(jsonPath("memberShip").exists())
            .andExpect(jsonPath("mobile").exists())
        ;
    }

    @Test
    public void 다른_회원_조회() throws Exception {
        Account account = TestUtils.createDefaultAccount();
        Account save = testUtils.signUpAccount(account);
        ResultActions step1 = this.mockMvc.perform(get(ROOT_URI + "/{id}", account.getId())
            .contentType(APPLICATION_JSON_VALUE)
        );

        step1.andExpect(jsonPath("password").doesNotExist())
            .andExpect(jsonPath("memberShip").doesNotExist())
            .andExpect(jsonPath("mobile").doesNotExist())
        ;
    }

    @Test
    public void 존재하지않는_회원_조회() throws Exception {
        ResultActions step1 = this.mockMvc.perform(get(ROOT_URI + "/{id}", 1L)
            .contentType(APPLICATION_JSON_VALUE)
        );

        step1.andExpect(status().isNotFound());
    }

    @Test
    public void 회원정보수정_성공() throws Exception {
        Account account = TestUtils.createDefaultAccount();
        String jwt = testUtils.signSupAndLogin(account);

        AccountUpdateDto updateDto = crateUpdateDto();

        ResultActions step1 = this.mockMvc.perform(patch(ROOT_URI + "/{id}", account.getId())
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, jwt)
            .content(this.objectMapper.writeValueAsString(updateDto))
        );

        step1.andExpect(status().isNoContent());

        Account find = accountRepository.findFirstByEmail(account.getEmail())
            .orElseThrow(() -> {
                throw new AccountNotFoundException();
            });

        assertEquals(updateDto.getMobile(), find.getMobile());
    }

    @Test
    public void 다른_유저정보_수정_실패() throws Exception {
        // given : 두개의 게정
        Account account = TestUtils.createDefaultAccount();
        String jwt = testUtils.signSupAndLogin(account);

        Account account2 = TestUtils.createDefaultAccount();
        account2.setEmail("email@email.com");
        String jwt2 = testUtils.signSupAndLogin(account2);

        AccountUpdateDto updateDto = crateUpdateDto();

        // when : 1번 계정의 정보를 2번 계정이 수정
        ResultActions step1 = this.mockMvc.perform(patch(ROOT_URI + "/{id}", account.getId())
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, jwt2)
            .content(this.objectMapper.writeValueAsString(updateDto))
        );

        // then : Forbidden 403
        step1.andExpect(status().isForbidden());
    }

    @Test
    public void 이메일인증_테스트() throws Exception {

        // given
        Account account = TestUtils.createDefaultAccount();
        Account save = this.accountService.signUp(account);

        // when
        this.mockMvc.perform(get(ROOT_URI + "/{id}/email", save.getId())
            .param("code", save.getCertifyCode())
            .contentType(APPLICATION_JSON_VALUE)
        )
            .andDo(print());

        // then : 토큰일치 인증된 이메일로 변경되고 기존의 확인코드는 null 로 변경
        Account find = this.accountRepository.findById(save.getId()).get();
        assertEquals(true, find.isCertifiedEmail(), "true로 변경되어야 한다.");
        assertEquals(null, find.getCertifyCode(), "기존의 코드는 삭제된다.");
    }

    @Description("인증코드는 24시간동안 유효하다.")
    @Test
    public void 만료된_코드_테스트() throws Exception {

        // given
        Account account = TestUtils.createDefaultAccount();
        Account save = this.accountService.signUp(account);
        LocalDateTime targetTime = LocalDateTime.now().minusDays(2);
        String time = targetTime.format(AccountServiceImpl.formatter);
        String uuid = UUID.randomUUID().toString();
        String expiredCode = time + uuid;
        save.setCertifyCode(expiredCode);

        // when
        ResultActions actions = this.mockMvc.perform(get(ROOT_URI + "/{id}/email", save.getId())
            .param("code", save.getCertifyCode())
            .contentType(APPLICATION_JSON_VALUE)
        )
            .andDo(print());

        actions.andExpect(status().isBadRequest());

        // then : 토큰일치 인증된 이메일로 변경되고 기존의 확인코드는 null 로 변경
        Account find = this.accountRepository.findById(save.getId()).get();
        assertEquals(false, find.isCertifiedEmail(), "기본값 false 를 유지한다.");
        assertEquals(expiredCode, find.getCertifyCode(), "코드는 변경되지 않는다.");
    }

    @Test
    public void 잘못된_코드_테스트() throws Exception {

        // given
        Account account = TestUtils.createDefaultAccount();
        Account save = this.accountService.signUp(account);
        LocalDateTime targetTime = LocalDateTime.now().minusDays(2);
        String time = targetTime.format(AccountServiceImpl.formatter);
        String uuid = UUID.randomUUID().toString();
        String expiredCode = time + uuid;
        save.setCertifyCode(expiredCode);

        // when
        ResultActions actions = this.mockMvc.perform(get(ROOT_URI + "/{id}/email", save.getId())
            .param("code", save.getCertifyCode() + "addString")
            .contentType(APPLICATION_JSON_VALUE)
        )
            .andDo(print());

        actions.andExpect(status().isBadRequest());

        // then : 토큰일치 인증된 이메일로 변경되고 기존의 확인코드는 null 로 변경
        Account find = this.accountRepository.findById(save.getId()).get();
        assertEquals(false, find.isCertifiedEmail(), "기본값 false 를 유지한다.");
        assertEquals(expiredCode, find.getCertifyCode(), "코드는 변경되지 않는다.");
    }


    private AccountUpdateDto crateUpdateDto() {
        return AccountUpdateDto.builder()
            .nickname("정우양")
            .password("dnwjd123@@@@")
            .mobile("01033334444")
            .build();
    }

}