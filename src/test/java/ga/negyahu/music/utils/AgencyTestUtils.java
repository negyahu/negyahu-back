package ga.negyahu.music.utils;

import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import org.springframework.stereotype.Component;

@Component
public class AgencyTestUtils {

    public static AgencyCreateDto agencyCreateDto(){
        return AgencyCreateDto.builder()
                .agentName("빅히트")
                .agentNameEN("BIGHIT")
                .adminEmail("yangfriendship.dev@gmail.com")
                .ceoName("김덕배")
                .businessNumber("본적이없어예시도못들겠네")
                .mobile("01011112222")
                .build();
    }

}
