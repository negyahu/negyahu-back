package ga.negyahu.music.mapstruct.artist;

import ga.negyahu.music.artist.dto.ArtistDto;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.dto.ArtistCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ArtistMemberMapper.class})
public interface ArtistMapper {

    ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

    @Mapping(source = "imageId", target = "profileImage.id")
    Artist from(ArtistCreateDto createDto);

    @Named("artistToDto")
    @Mapping(source = "agency", target = "agencyDto")
//    @Mapping(target = "entity", ignore = true)
    ArtistDto toDto(Artist register);
}
