package ga.negyahu.music.agency;

import static ga.negyahu.music.agency.AgencyController.ROOT_URL;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.dto.ManagerDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.agency.repository.AgencyMemberRepository;
import ga.negyahu.music.agency.repository.AgencyRepository;
import ga.negyahu.music.agency.service.AgencyService;
import ga.negyahu.music.utils.AgencyTestUtils;
import ga.negyahu.music.utils.TestUtils;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@CustomSpringBootTest
@AutoConfigureMockMvc
public class AgencyControllerTest {

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private AgencyService agencyService;
    @Autowired
    private AgencyMemberRepository agencyMemberRepository;

    @Test
    public void 소속사_등록신청_테스트() throws Exception {

        // given : 소속사 등록 Dto
        AgencyCreateDto createDto = AgencyTestUtils.agencyCreateDto();

        String content = objectMapper.writeValueAsString(createDto);

        // when : 소속사 등록 요청
        ResultActions perform = this.mockMvc.perform(post(ROOT_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
        );

        perform.andDo(print());
        // then : 201,
        perform.andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
        ;

        MvcResult result = perform.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Map map = new ObjectMapper().readValue(contentAsString, Map.class);
        Integer id = (Integer) map.get("id");
        Account boss = accountRepository.findFirstByEmail(createDto.getEmail()).get();

        // then : 입력한 이메일, 연락처로 생성된 계정, 역할은 AGENCY
        assertEquals(createDto.getBossName(), boss.getUsername());
        assertEquals(createDto.getMobile(), boss.getMobile());
        assertEquals(Role.AGENCY, boss.getRole());
        assertEquals(State.WAIT, boss.getState());

        Agency agency = this.agencyRepository.findById(Long.valueOf(id)).get();

        // then : Agency 생성확인, 입력한 DTO 와 같아야한다. 승인이 필요하기 때문에 State 의 값은 WAIT
        assertEquals(createDto.getBusinessNumber(), agency.getBusinessNumber());
        assertEquals(createDto.getMobile(), agency.getMobile());
        assertEquals(createDto.getBossName(), agency.getBossName());
        assertEquals(createDto.getNameEN(), agency.getNameEN());
        assertEquals(createDto.getNameKR(), agency.getNameKR());
        assertEquals(State.WAIT, agency.getState());
        assertNotNull(agency.getSignUpDate());
        assertNotNull(agency.getUpdateDateTime());
    }

    @Test
    public void 소속사_직원_등록테스트() throws Exception {

        // CEO 와 Agency 생성
        Account ceo = TestUtils.createDefaultAccount();
        Agency agency = AgencyTestUtils.createDefaultAgency(ceo);
        Agency register = agencyService.register(agency);
        String jwt = this.testUtils.loginAccount(ceo);

        // 등록할 직원 계정 생성 -> 등록 요청 DTO 생성
        int addCount = 3;
        List<Account> accounts = TestUtils.createAccounts(addCount);
        for (Account account : accounts) {
            this.testUtils.signUpAccount(account);
        }
        String[] emails = accounts.stream()
            .map(a -> a.getEmail())
            .toArray(String[]::new);
        ManagerDto managerDto = new ManagerDto();
        managerDto.setEmails(emails);

        // when : 계정 등록 요청
        ResultActions perform = mockMvc
            .perform(post("/api/agencies/{agencyId}/manager", agency.getId())
                .content(this.objectMapper.writeValueAsString(managerDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, jwt)
            );

        perform.andDo(print());
        // then : 200, 등록된 계정개수 반환
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.count", is(accounts.size())));

        // then : 2명의 매니저가 등록되있다.
        List<AgencyMember> members = agencyMemberRepository.findAllByAgencyId(agency.getId(),
            PageRequest.of(0,10));
        assertEquals(addCount, members.size());
    }

}