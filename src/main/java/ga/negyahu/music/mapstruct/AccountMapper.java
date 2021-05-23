package ga.negyahu.music.mapstruct;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.account.dto.AccountOwnerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "zipcode", target = "address.zipcode")
    @Mapping(source = "roadAddress", target = "address.roadAddress")
    @Mapping(source = "detailAddress", target = "address.detailAddress")
    Account from(AccountCreateDto accountCreateDto);

    Account from(Account account);


    AccountDto toDto(Account account);

    AccountOwnerDto toOwnerDto(Account account);

}
