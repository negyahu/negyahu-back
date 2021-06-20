package ga.negyahu.music.artist.dto;

import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.entity.ArtistType;
import ga.negyahu.music.subscribe.Subscribe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ArtistDto {

    private String name;

    private String nameEN;

    private Long agencyId;

    private ArtistType artistType;

    private LocalDateTime registerDateTime;

    private LocalDateTime updateDateTime;

}
