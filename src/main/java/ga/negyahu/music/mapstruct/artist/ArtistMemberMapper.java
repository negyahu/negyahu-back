package ga.negyahu.music.mapstruct.artist;

import ga.negyahu.music.artist.dto.ArtistMemberDto;
import ga.negyahu.music.artist.dto.ArtistMemberUpdateDto;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.dto.ArtistMemberCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ArtistMemberMapper {

    ArtistMemberMapper INSTANCE = Mappers.getMapper(ArtistMemberMapper.class);

    @Mapping(source = "email", target = "account.email")
    @Mapping(source = "password", target = "account.password")
    @Mapping(source = "nameKR", target = "account.username")
    @Mapping(source = "nameKR", target = "nameKR")
    ArtistMember from(ArtistMemberCreateDto createDto);

    ArtistMember fetch(Long artistId, Long memberId);

    ArtistMemberDto toDto(ArtistMember member);

    ArtistMember from(ArtistMemberUpdateDto updateDto);
}
