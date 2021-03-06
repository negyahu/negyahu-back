package ga.negyahu.music.account.controller;

import static ga.negyahu.music.account.controller.AccountController.ROOT_URI;
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
    public void ????????????_??????() throws Exception {
        // given
        AccountCreateDto createDto = TestUtils.createAccountCreateDto();
        // when
        ResultActions step1 = this.mockMvc.perform(post(ROOT_URI).contentType(APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(createDto))
        );
        // then : 201 ??????
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
    public void ????????????_??????????????????_?????????() throws Exception {

        // given : ?????? ????????? ???????????? ????????? ????????????.
        Account account = TestUtils.createDefaultAccount();
        Account save = testUtils.signUpAccount(account);

        AccountCreateDto createDto = TestUtils.createAccountCreateDto();

        // when : ????????? ???????????? ???????????? ??????
        ResultActions step1 = this.mockMvc.perform(post(ROOT_URI).contentType(APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(createDto))
        );

//        step1.andExpect(jsonPath("$", hasSize(1)))
//            .andExpect(jsonPath("$[0].field").exists())
        ;
    }

    @Test
    public void ??????_??????_??????() throws Exception {
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
    public void ??????_??????_??????() throws Exception {
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
    public void ??????????????????_??????_??????() throws Exception {
        ResultActions step1 = this.mockMvc.perform(get(ROOT_URI + "/{id}", 1L)
            .contentType(APPLICATION_JSON_VALUE)
        );

        step1.andExpect(status().isNotFound());
    }

    @Test
    public void ??????????????????_??????() throws Exception {
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
    public void ??????_????????????_??????_??????() throws Exception {
        // given : ????????? ??????
        Account account = TestUtils.createDefaultAccount();
        String jwt = testUtils.signSupAndLogin(account);

        Account account2 = TestUtils.createDefaultAccount();
        account2.setEmail("email@email.com");
        String jwt2 = testUtils.signSupAndLogin(account2);

        AccountUpdateDto updateDto = crateUpdateDto();

        // when : 1??? ????????? ????????? 2??? ????????? ??????
        ResultActions step1 = this.mockMvc.perform(patch(ROOT_URI + "/{id}", account.getId())
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, jwt2)
            .content(this.objectMapper.writeValueAsString(updateDto))
        );

        // then : Forbidden 403
        step1.andExpect(status().isForbidden());
    }

    @Test
    public void ???????????????_?????????() throws Exception {

        // given
        Account account = TestUtils.createDefaultAccount();
        Account save = this.accountService.signUp(account);

        // when
        this.mockMvc.perform(get(ROOT_URI + "/{id}/email", save.getId())
            .param("code", save.getCertifyCode())
            .contentType(APPLICATION_JSON_VALUE)
        )
            .andDo(print());

        // then : ???????????? ????????? ???????????? ???????????? ????????? ??????????????? null ??? ??????
        Account find = this.accountRepository.findById(save.getId()).get();
        assertEquals(true, find.isCertifiedEmail(), "true??? ??????????????? ??????.");
        assertEquals(null, find.getCertifyCode(), "????????? ????????? ????????????.");
    }

    @Description("??????????????? 24???????????? ????????????.")
    @Test
    public void ?????????_??????_?????????() throws Exception {

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

        // then : ???????????? ????????? ???????????? ???????????? ????????? ??????????????? null ??? ??????
        Account find = this.accountRepository.findById(save.getId()).get();
        assertEquals(false, find.isCertifiedEmail(), "????????? false ??? ????????????.");
        assertEquals(expiredCode, find.getCertifyCode(), "????????? ???????????? ?????????.");
    }

    @Test
    public void ?????????_??????_?????????() throws Exception {

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

        // then : ???????????? ????????? ???????????? ???????????? ????????? ??????????????? null ??? ??????
        Account find = this.accountRepository.findById(save.getId()).get();
        assertEquals(false, find.isCertifiedEmail(), "????????? false ??? ????????????.");
        assertEquals(expiredCode, find.getCertifyCode(), "????????? ???????????? ?????????.");
    }


    private AccountUpdateDto crateUpdateDto() {
        return AccountUpdateDto.builder()
            .password("dnwjd123@@@@")
            .mobile("01033334444")
            .build();
    }

}