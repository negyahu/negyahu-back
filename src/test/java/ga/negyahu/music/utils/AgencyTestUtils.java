package ga.negyahu.music.utils;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import org.springframework.stereotype.Component;

@Component
public class AgencyTestUtils {

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

}
