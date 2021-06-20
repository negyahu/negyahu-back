package ga.negyahu.music.artist.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.entity.Artist;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArtistService {

    Artist register(Artist artist, Long agencyId, Account user);

}
