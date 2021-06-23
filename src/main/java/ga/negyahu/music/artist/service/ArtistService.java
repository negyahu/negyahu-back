package ga.negyahu.music.artist.service;

import ga.negyahu.music.ScrollPageable;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.ArtistMember;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArtistService {

    Artist register(Artist artist, Long agencyId, Account user);

    List<Artist> fetchList(ScrollPageable pageable);

    List<ArtistMember> createMembers(Long artistId,
        List<ArtistMember> artistMembers);

    void checkIsAdmin(Account user, Long agencyId);
}
