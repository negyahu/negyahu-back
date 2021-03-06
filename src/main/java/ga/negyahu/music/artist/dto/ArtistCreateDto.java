package ga.negyahu.music.artist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ga.negyahu.music.account.entity.State;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCreateDto {

    private String nameKR;

    private String nameEN;

    @JsonProperty("isBlind")
    private boolean isBlind;

    private Long imageId;

}
