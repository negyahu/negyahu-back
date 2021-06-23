package ga.negyahu.music.artist.repository;

import ga.negyahu.music.utils.DataJpaTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

}