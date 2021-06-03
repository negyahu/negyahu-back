package ga.negyahu.music.mapstruct;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountCreateDto;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.account.dto.AccountOwnerDto;
import ga.negyahu.music.account.dto.AccountUpdateDto;
import ga.negyahu.music.message.Message;
import ga.negyahu.music.message.dto.MessageDto;
import ga.negyahu.music.message.dto.MessageSendDto;
import ga.negyahu.music.message.dto.MessageUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mappings({
        @Mapping(source = "receiverId", target = "receiver.id"),
        @Mapping(source = "content", target = "content")
    })
    Message from(MessageSendDto messageSendDto);

    Message from(MessageUpdateDto updateDto);

    @Mappings(
        {@Mapping(source = "receiver.id", target = "receiver.id"),
            @Mapping(source = "receiver.nickname", target = "receiver.nickname"),
            @Mapping(source = "sender.id", target = "sender.id"),
            @Mapping(source = "sender.nickname", target = "sender.nickname")}
    )
    MessageDto  toDto(Message message);



}
