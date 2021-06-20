package ga.negyahu.music.mapstruct.artist;

import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.dto.ArtistMemberCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArtistMemberMapper {

    ArtistMemberMapper INSTANCE = Mappers.getMapper(ArtistMemberMapper.class);

    @Mapping(source = "email", target = "account.email")
    ArtistMember from(ArtistMemberCreateDto createDto);

}
