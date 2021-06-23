package ga.negyahu.music.artist.repository;

import ga.negyahu.music.artist.entity.ArtistMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ArtistMemberRepository extends JpaRepository<ArtistMember, Long> {

    @Query("select am from ArtistMember  am join fetch am.account")
    Optional<ArtistMember> findFirstById(Long id);

}
