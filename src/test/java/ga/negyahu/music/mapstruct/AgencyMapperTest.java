package ga.negyahu.music.mapstruct;

import static org.junit.jupiter.api.Assertions.*;

import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.utils.AgencyTestUtils;
import org.junit.jupiter.api.Test;

public class AgencyMapperTest {

    private AgencyMapper mapper = AgencyMapper.INSTANCE;

    @Test
    public void createDtoToEntity(){
        AgencyCreateDto dto = AgencyTestUtils.agencyCreateDto();

        Agency entity = mapper.from(dto);

        assertEquals(dto.getAgentName(), entity.getName());
        assertEquals(dto.getAgentNameEN(), entity.getNameEN());
        assertEquals(dto.getBusinessNumber(), entity.getBusinessNumber());
        assertEquals(dto.getMobile(), entity.getMobile());
        assertEquals(dto.getCeoName(), entity.getCeoName());
        assertEquals(State.WAIT, entity.getState());
    }

}