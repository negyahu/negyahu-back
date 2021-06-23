package ga.negyahu.music.fileupload.repository;

import static ga.negyahu.music.account.QAccount.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.account.QAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountUploadDaoImpl implements AccountUploadDao {

    private final JPAQueryFactory query;

    @Override
    public void updateFKOfAccount(Long imageId, Long accountId) {
        long execute = query.update(account)
            .set(account.profileImage.id, imageId)
            .where(account.id.eq(accountId))
            .execute();
    }
}
