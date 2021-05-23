package ga.negyahu.music.account.controller;

import static ga.negyahu.music.account.controller.AccountController.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.utils.TestUtils;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
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
        Account account = TestUtils.createAccount();
        Account save = testUtils.signUpAccount(account);

        AccountCreateDto createDto = TestUtils.createAccountCreateDto();

        // when : 동일한 이메일로 회원가입 요청
        ResultActions step1 = this.mockMvc.perform(post(ROOT_URI).contentType(APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(createDto))
        );
        step1.andDo(print());

        step1.andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].field").exists())
            ;
    }

    @Test
    public void 본인_계정_조회() throws Exception {
        Account account = TestUtils.createAccount();
        String token = testUtils.signSupAndLogin(account);
        ResultActions step1 = this.mockMvc.perform(get(ROOT_URI + "/{id}", account.getId())
            .contentType(APPLICATION_JSON_VALUE)
            .header(HttpHeaders.AUTHORIZATION, token)
        );
        step1.andDo(print());

        step1.andExpect(jsonPath("password").doesNotExist())
            .andExpect(jsonPath("memberShip").exists());
    }

    @Test
    public void 다른_회원_조회() throws Exception {
        Account account = TestUtils.createAccount();
        Account save = testUtils.signUpAccount(account);
        ResultActions step1 = this.mockMvc.perform(get(ROOT_URI + "/{id}", account.getId())
            .contentType(APPLICATION_JSON_VALUE)
        );
        step1.andDo(print());

        step1.andExpect(jsonPath("password").doesNotExist())
                .andExpect(jsonPath("memberShip").doesNotExist());
    }

    @Test
    public void 존재하지않는_회원_조회() throws Exception{
        ResultActions step1 = this.mockMvc.perform(get(ROOT_URI + "/{id}", 1L)
            .contentType(APPLICATION_JSON_VALUE)
        );
        step1.andDo(print());

        step1.andExpect(status().isBadRequest());
    }

}