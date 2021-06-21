package ga.negyahu.music.artist.repository;

import ga.negyahu.music.ScrollPageable;
import ga.negyahu.music.artist.entity.Artist;
import java.util.List;

public interface ArtistDao {

    List<Artist> findAllByPageable(ScrollPageable pageable);

}
