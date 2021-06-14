package ga.negyahu.music.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.security.config.JwtSecurityConfig;
import ga.negyahu.music.security.dto.LoginRequest;
import ga.negyahu.music.utils.TestUtils;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@CustomSpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private Account account;
    private String password;

    @BeforeEach
    public void init(){
        Account account = TestUtils.createDefaultAccount();
        password = account.getPassword();
        this.account = testUtils.signUpAccount(account);
    }

    @Description("로그인 성공후, header,body에 token이 발급된다.")
    @Test
    public void 회원_로그인_테스트() throws Exception{

        LoginRequest request = LoginRequest.builder().email(account.getEmail()).password(password)
            .build();
        String content = objectMapper.writeValueAsString(request);

        // when : email, password 를 post 요청
        ResultActions step1 = mockMvc.perform(post(JwtSecurityConfig.LOGIN_URL)
        .content(content));

        // then : 로그인 성공, token 을 반환한다.
        step1.andExpect(jsonPath("token").exists())
            .andExpect(header().exists(HttpHeaders.AUTHORIZATION));

    }

    @Test
    public void 비밀번호_오류_실패() throws Exception {
        LoginRequest request = LoginRequest.builder().email(account.getEmail()).password("invalidPassword@@")
            .build();
        String content = objectMapper.writeValueAsString(request);

        // when : email, password 를 post 요청
        ResultActions step1 = mockMvc.perform(post(JwtSecurityConfig.LOGIN_URL)
            .content(content));

        // then : 로그인 성공, token 을 반환한다.
        step1.andExpect(status().isUnauthorized());
    }

    @Test
    public void 존재하지않는_계정_오류_실패() throws Exception {
        LoginRequest request = LoginRequest.builder().email("invaliduser@gmail.com").password("invalidPassword@@")
            .build();
        String content = objectMapper.writeValueAsString(request);

        // when : email, password 를 post 요청
        ResultActions step1 = mockMvc.perform(post(JwtSecurityConfig.LOGIN_URL)
            .content(content));
        // then : 로그인 성공, token 을 반환한다.

        step1.andExpect(status().isUnauthorized());
    }


}