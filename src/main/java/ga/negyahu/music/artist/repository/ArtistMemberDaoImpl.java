package ga.negyahu.music.artist.repository;

import static ga.negyahu.music.artist.entity.QArtistMember.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.entity.QArtistMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
public class ArtistMemberDaoImpl {

    private final JPAQueryFactory query;

    public ArtistMember fetchMember(Long memberId){

//        query.select(artistMember)
//            .from(artistMember)
//            .join(artistMember.artist).fetchJoin()
//            .join(artistMember.account).fetchJoin()
//            .join(artistMember.account.eq(accountFileUpload.account)).fetch()
        return null;
    }

}
