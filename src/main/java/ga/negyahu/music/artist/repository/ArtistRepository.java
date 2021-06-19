package ga.negyahu.music.artist.repository;

import ga.negyahu.music.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArtistRepository extends JpaRepository<Artist, Long> {


}
