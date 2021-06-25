package ga.negyahu.music.subscribe.dto;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.dto.ArtistDto;
import ga.negyahu.music.artist.entity.Artist;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscribeDto {

    private Long id;

    private ArtistDto artist;

    private String nickname;

    private LocalDate subscribeDate;

}