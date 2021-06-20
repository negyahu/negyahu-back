package ga.negyahu.music.artist.dto;

import ga.negyahu.music.artist.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistMemberCreateDto {

    private String email;

    private String nameKR;

    private String password;

    private String nameEN;

    private Gender gender;

    private String instagram;

    private String etc;

    private Long imageId;

}
