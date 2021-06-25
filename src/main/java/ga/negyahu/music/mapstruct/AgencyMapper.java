package ga.negyahu.music.mapstruct;

import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencyMeDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.artist.dto.ArtistDto;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.mapstruct.artist.ArtistMapper;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {ArtistMapper.class})
public interface AgencyMapper {

    AgencyMapper INSTANCE = Mappers.getMapper(AgencyMapper.class);

    @Mapping(source = "email", target = "account.email")
    @Mapping(target = "state", constant = "WAIT")
    Agency from(AgencyCreateDto createDto);

//    @Mapping(source = "account.email", target = "email")
    AgencyDto toDto(Agency agency);

//    @Mapping(source = "email", target = "account.email")
    @Mapping(target = "state", constant = "WAIT")
    @Mapping(source = "artists", target = "artists")
    AgencyMeDto toMeDto(Agency agency);

}
