package ga.negyahu.music.message.repository;

import ga.negyahu.music.message.dto.MessageDto;
import ga.negyahu.music.message.dto.MessageSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MessageDao {

    Page<MessageDto> search(MessageSearch search, Pageable pageable);

}
