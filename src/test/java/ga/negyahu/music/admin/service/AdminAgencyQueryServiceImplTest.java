package ga.negyahu.music.admin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.repository.AgencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AdminAgencyQueryServiceImplTest {

    AdminAgencyQueryService queryService;
    @Mock
    AgencyRepository agencyRepository;

    @BeforeEach
    public void init() {
        this.queryService = new AdminAgencyQueryServiceImpl(this.agencyRepository);
    }

    @Test
    public void 소속사_수락_테스트() {
        // given
        Account account = Account.builder().state(State.WAIT).build();
        Agency agency = Agency.builder()
            .id(1L)
            .state(State.WAIT)
            .account(account).build();
        when(this.agencyRepository.findById(any())).thenReturn(
            java.util.Optional.ofNullable(agency));
        State expect = State.IGNORE;

        // when
        this.queryService.changeState(agency.getId(),expect);

        // then
        assertEquals(expect, account.getState());
        assertEquals(expect, agency.getState());
    }

    @Test
    public void 소속사_정지_테스트() {
        // given
        Account account = Account.builder()
            .state(State.ACTIVE)
            .build();
        Agency agency = Agency.builder()
            .id(1L)
            .state(State.ACTIVE)
            .account(account).build();
        when(this.agencyRepository.findById(any())).thenReturn(
            java.util.Optional.ofNullable(agency));
        State expect = State.IGNORE;
        // when
        this.queryService.changeState(agency.getId(),expect);

        // then
        assertEquals(expect, account.getState());
        assertEquals(expect, agency.getState());
    }

}