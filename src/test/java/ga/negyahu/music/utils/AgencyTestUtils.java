package ga.negyahu.music.utils;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.agency.entity.AgencyRole;
import ga.negyahu.music.agency.repository.AgencyRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgencyTestUtils {

    @Autowired
    private AgencyRepository agencyRepository;

    public static AgencyCreateDto agencyCreateDto() {
        return AgencyCreateDto.builder()
            .nameEN("빅히트")
            .nameKR("BIGHIT")
            .email("yangfriendship.dev@gmail.com")
            .bossName("김덕배")
            .businessNumber("본적이없어예시도못들겠네")
            .mobile("01011112222")
            .build();
    }

    public static Agency createDefaultAgency(Account account) {
        return Agency.builder()
            .nameKR("빅히트")
            .nameEN("BIGHIT")
            .account(account)
            .bossName("김덕배")
            .businessNumber("본적이없어예시도못들겠네")
            .mobile("01011112222")
            .build();
    }

    public static AgencyMember createAgencyMember(Agency agency, Account account){
        return AgencyMember.builder()
            .nickname(UUID.randomUUID().toString().substring(0,10))
            .agencyRole(AgencyRole.MANAGER)
            .agency(agency)
            .account(account)
            .state(State.ACTIVE)
            .build();
    }

    public Agency registerAgency(Agency agency) {
        return this.agencyRepository.save(agency);
    }
}
