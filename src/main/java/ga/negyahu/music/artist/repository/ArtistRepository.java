package ga.negyahu.music.artist.repository;

import ga.negyahu.music.artist.entity.Artist;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArtistRepository extends JpaRepository<Artist, Long>, ArtistDao {

    @EntityGraph(attributePaths = {"agency"})
    Optional<Artist> findWithAgencyById(Long id);

}
