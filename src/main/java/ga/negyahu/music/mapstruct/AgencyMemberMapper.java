package ga.negyahu.music.mapstruct;

import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.agency.dto.AgencyMemberDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.entity.AgencyMember;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    uses = {AccountDto.class}
)
public interface AgencyMemberMapper {

    AgencyMemberMapper INSTANCE = Mappers.getMapper(AgencyMemberMapper.class);

    AgencyMember from(AgencyMember agencyMember);

    AgencyMember from(AgencyMemberDto agencyMemberDto);

    AgencyMemberDto toDto(AgencyMember agencyMember);



}
