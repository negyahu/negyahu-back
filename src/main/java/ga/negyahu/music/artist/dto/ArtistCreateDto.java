package ga.negyahu.music.artist.dto;

import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.artist.ArtistMember;
import ga.negyahu.music.subscribe.Subscribe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCreateDto {

    private String name;

    private String nameEN;

    private State state;

}
