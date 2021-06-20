package ga.negyahu.music.artist.repository;

import ga.negyahu.music.artist.entity.ArtistMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ArtistMemberRepository extends JpaRepository<ArtistMember, Long> {

}
