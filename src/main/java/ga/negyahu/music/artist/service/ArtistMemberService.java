package ga.negyahu.music.artist.service;

import ga.negyahu.music.artist.entity.ArtistMember;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArtistMemberService {

    ArtistMember fetch(Long artistId);

    ArtistMember register(Long artistId, ArtistMember member);

    ArtistMember update(ArtistMember from);

}
