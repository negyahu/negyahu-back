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
    public void ?????????_????????????_?????????() throws Exception {

        // given : ????????? ?????? Dto
        AgencyCreateDto createDto = AgencyTestUtils.agencyCreateDto();

        String content = objectMapper.writeValueAsString(createDto);

        // when : ????????? ?????? ??????
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

        // then : ????????? ?????????, ???????????? ????????? ??????, ????????? AGENCY
        assertEquals(createDto.getBossName(), boss.getUsername());
        assertEquals(createDto.getMobile(), boss.getMobile());
        assertEquals(Role.AGENCY, boss.getRole());
        assertEquals(State.WAIT, boss.getState());

        Agency agency = this.agencyRepository.findById(Long.valueOf(id)).get();

        // then : Agency ????????????, ????????? DTO ??? ???????????????. ????????? ???????????? ????????? State ??? ?????? WAIT
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
    public void ?????????_??????_???????????????() throws Exception {

        // CEO ??? Agency ??????
        Account ceo = TestUtils.createDefaultAccount();
        Agency agency = AgencyTestUtils.createDefaultAgency(ceo);
        Agency register = agencyService.register(agency);
        String jwt = this.testUtils.loginAccount(ceo);

        // ????????? ?????? ?????? ?????? -> ?????? ?????? DTO ??????
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

        // when : ?????? ?????? ??????
        ResultActions perform = mockMvc
            .perform(post("/api/agencies/{agencyId}/manager", agency.getId())
                .content(this.objectMapper.writeValueAsString(managerDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, jwt)
            );

        perform.andDo(print());
        // then : 200, ????????? ???????????? ??????
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.count", is(accounts.size())));

        // then : 2?????? ???????????? ???????????????.
        List<AgencyMember> members = agencyMemberRepository.findAllByAgencyId(agency.getId(),
            PageRequest.of(0,10));
        assertEquals(addCount, members.size());
    }

}