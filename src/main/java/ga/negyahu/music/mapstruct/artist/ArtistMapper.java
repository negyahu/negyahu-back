package ga.negyahu.music.mapstruct.artist;

import ga.negyahu.music.artist.dto.ArtistDto;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.dto.ArtistCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ArtistMemberMapper.class})
public interface ArtistMapper {

    ArtistMapper INSTANCE = Mappers.getMapper(ArtistMapper.class);

    Artist from(ArtistCreateDto createDto);

    ArtistDto toDto(Artist register);
}
