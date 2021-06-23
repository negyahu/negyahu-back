package ga.negyahu.music.artist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.artist.entity.Gender;
import ga.negyahu.music.fileupload.entity.AccountUpload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistMemberDto {

    private Long id;

    private String nameKR;

    private String nameEN;

    private Gender gender;

    @JsonProperty("imageFile")
    private AccountUpload fileUpload;

    @JsonProperty("agency")
    private AgencyDto agencyDto;

    @JsonProperty("artistDto")
    private ArtistDto artistDto;

    private String instagram;

    private String etc;
}
