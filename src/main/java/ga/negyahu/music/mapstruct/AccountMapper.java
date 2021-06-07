package ga.negyahu.music.mapstruct;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.account.dto.AccountOwnerDto;
import ga.negyahu.music.account.dto.AccountUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account from(AccountCreateDto accountCreateDto);

    Account from(Account account);

    AccountDto toDto(Account account);

    AccountOwnerDto toOwnerDto(Account account);

    Account from(AccountUpdateDto accountUpdateDto);

    @Mapping(source = "fileUpLoads", target = "fileUpLoads", ignore = true)
    Account map(Account source, @MappingTarget Account account);

}
