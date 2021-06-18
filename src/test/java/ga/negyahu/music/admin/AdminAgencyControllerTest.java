package ga.negyahu.music.admin;

import static ga.negyahu.music.admin.AdminAgencyController.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.utils.AgencyTestUtils;
import ga.negyahu.music.utils.TestUtils;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@CustomSpringBootTest
public class AdminAgencyControllerTest {

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    int activeCnt = 10;
    int waitCnt = 5;
    int total = activeCnt + waitCnt;
    private Account account;
    private List<Agency> agencies;
    private String token;

    @BeforeEach
    public void init() {
        account = TestUtils.createDefaultAccount();
        account.setRole(Role.ADMIN);
        token = this.testUtils.signSupAndLogin(account);
        agencies = AgencyTestUtils
            .createAgencies(account, activeCnt, State.ACTIVE, waitCnt, State.WAIT);
        List<Agency> saveAll = agencyRepository.saveAll(agencies);
    }

    @Test
    public void 소속사_검색_테스트() throws Exception {

        // when : 조건없이 검색
        ResultActions perform = this.mockMvc.perform(get(ROOT_URL)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
        );
        // then : 15개 모두 출력 default pageable : s = 20, p = 1
        perform.andDo(print());
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.content", Matchers.hasSize(total)));

        // when : State = WAIT
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "state");
        params.add("keyword", "WAIT");
        ResultActions perform2 = this.mockMvc.perform(get(ROOT_URL)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
            .queryParams(params)
        );

        // then : State = WAIT 5개
        perform2.andExpect(status().isOk())
            .andExpect(jsonPath("$.content", Matchers.hasSize(waitCnt)));
    }

    @Test
    public void 소속사_승인_테스트() throws Exception {

        Agency targetAgency = this.agencies.stream()
            .filter(a -> a.getState() == State.WAIT)
            .findFirst()
            .get();

        // when : permit 요청
        ResultActions perform = this.mockMvc.perform(patch(PERMIT_URL, targetAgency.getId())
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
        );
        perform.andExpect(status().isNoContent());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("type", "state");
        params.add("keyword", "WAIT");
        ResultActions perform2 = this.mockMvc.perform(get(ROOT_URL)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
            .queryParams(params)
        );

        perform2.andExpect(status().isOk())
            .andExpect(jsonPath("$.content", Matchers.hasSize(waitCnt - 1)));
        //
        MultiValueMap<String, String> params2 = new LinkedMultiValueMap<>();
        params2.add("type", "state");
        params2.add("keyword", "ACTIVE");

        ResultActions perform1 = this.mockMvc.perform(get(ROOT_URL)
            .header(HttpHeaders.AUTHORIZATION, token)
            .contentType(MediaType.APPLICATION_JSON)
            .queryParams(params2)
        );

        perform1.andExpect(status().isOk())
            .andExpect(jsonPath("$.content", Matchers.hasSize(activeCnt + 1)));

        // then : 해당 소속사와, 소속사 대표 계정의 상태가 Active 로 변경된다.
        assertEquals(State.ACTIVE, account.getState());
        assertEquals(State.ACTIVE, targetAgency.getState());

    }


}