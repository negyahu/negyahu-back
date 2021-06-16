package ga.negyahu.music.message.repository;

import static ga.negyahu.music.account.QAccount.account;
import static ga.negyahu.music.message.QMessage.message;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.message.dto.MessageDto;
import ga.negyahu.music.message.dto.MessageSearch;
import ga.negyahu.music.message.dto.MessageType;
import ga.negyahu.music.message.dto.QMessageDto;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageDaoImpl implements MessageDao {

    private final JPAQueryFactory query;

    @Override
    public Page<MessageDto> search(MessageSearch search, Pageable pageable) {

        QueryResults<MessageDto> queryResults = query
            .select(new QMessageDto(
                message.id,
                message.content,
                message.sendDateTime,
                message.sender.id,
                message.sender.nickname,
                message.receiver.id,
                message.receiver.nickname,
                message.isOpened))
            .from(message)
            .join(message.sender, account)
            .join(message.receiver, account)
            .where(
                addQuery(search)
            )
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetchResults();
        return new PageImpl<>(queryResults.getResults(), pageable,
            queryResults.getTotal());
    }

    private BooleanBuilder addQuery(MessageSearch search) {
        Long id = search.getAccountId();
        BooleanBuilder builder = new BooleanBuilder();
        // 발수신메세지 모두 검색
        if (Objects.isNull(search.getType()) || search.getType() == MessageType.ALL) {
            builder.and(
                message.sender.id.eq(id).and(message.isDeletedBySender.isFalse())
                    .or(message.receiver.id.eq(id)).and(message.isDeletedByReceiver.isFalse()));
            return builder;
        } else if (search.getType() == MessageType.SEND) {
            // 보낸메세지 검색
            builder.and(message.sender.id.eq(id).and(message.isDeletedBySender.isFalse()));
            return builder;
        }
        // 받은메세지 검색
        builder.and(message.receiver.id.eq(id).and(message.isDeletedByReceiver.isFalse()));
        return builder;
    }


}
