package ga.negyahu.music.mapstruct;

import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.entity.Agency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AgencyMapper {

    AgencyMapper INSTANCE = Mappers.getMapper(AgencyMapper.class);

    @Mapping(source = "agentName", target = "name")
    @Mapping(source = "agentNameEN", target = "nameEN")
    @Mapping(source = "adminEmail", target = "account.email")
    @Mapping(target = "state",constant = "WAIT")
    Agency from(AgencyCreateDto createDto);



}
