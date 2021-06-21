package ga.negyahu.music.artist.repository;

import static ga.negyahu.music.agency.entity.QAgency.*;
import static ga.negyahu.music.artist.entity.QArtist.*;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.ScrollPageable;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.QAgencyDto;
import ga.negyahu.music.agency.entity.QAgency;
import ga.negyahu.music.artist.dto.QArtistDto;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.QArtist;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArtistDaoImpl implements ArtistDao {

    private final JPAQueryFactory query;

    @Override
    public List<Artist> findAllByPageable(ScrollPageable pageable) {
        int size = 50;
        if (pageable != null) {
            size = pageable.getSize();
        }
//
//        QAgencyDto agencyDto = new QAgencyDto(
//            artist.agency.id,
//            artist.agency.nameKR,
//            artist.agency.nameEN,
//            artist.agency.businessNumber,
//            artist.agency.bossName,
//            artist.agency.mobile,
//            artist.agency.state,
//            artist.agency.signUpDate
//        );
//
//        Projections.constructor(
//            AgencyDto.class
//            , artist.agency.id,
//            artist.agency.nameKR,
//            artist.agency.nameEN,
//            artist.agency.businessNumber,
//            artist.agency.bossName,
//            artist.agency.mobile,
//            artist.agency.state,
//            artist.agency.signUpDate
//        );
//        new QArtistDto(
//            artist.nameKR,
//            artist.nameEN
//            ,Projections.constructor(
//            AgencyDto.class
//            , artist.agency.id,
//            artist.agency.nameKR,
//            artist.agency.nameEN,
//            artist.agency.businessNumber,
//            artist.agency.bossName,
//            artist.agency.mobile,
//            artist.agency.state,
//            artist.agency.signUpDate
//        ) ,
//            artist.profileImage,
//            artist.artistType,
//            artist.registerDateTime,
//            artist.updateDateTime
//        )
        List<Artist> fetch = query.select(artist)
            .from(
                artist
            )
            .join(artist.profileImage).fetchJoin()
            .join(artist.agency).fetchJoin()
            .where(addScrollQuery(pageable))
            .fetch();

        return fetch;
    }

    private BooleanBuilder addScrollQuery(ScrollPageable pageable) {
        if (pageable == null || pageable.getFrom() == null || pageable.getFrom().equals(0)) {
            return null;
        }
        BooleanBuilder builder = new BooleanBuilder();
        return builder.and(artist.id.lt(pageable.getFrom()));
    }

}
