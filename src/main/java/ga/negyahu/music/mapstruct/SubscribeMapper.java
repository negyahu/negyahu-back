package ga.negyahu.music.mapstruct;

import ga.negyahu.music.subscribe.dto.SubscribeDto;
import ga.negyahu.music.subscribe.entity.Subscribe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubscribeMapper {

    SubscribeMapper INSTANCE = Mappers.getMapper(SubscribeMapper.class);

    SubscribeDto toDto(Subscribe subscribe);

}
