package ga.negyahu.music.artist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.artist.entity.ArtistType;
import ga.negyahu.music.fileupload.entity.ArtistUpload;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@Builder
//@AllArgsConstructor
@NoArgsConstructor
public class ArtistDto {

    private Long id;

    private String nameKR;

    private String nameEN;

    @JsonProperty("agency")
    @JsonIgnoreProperties(value = {"originalName", "filePath", "fullFilePath"})
    private AgencyDto agencyDto;

    private ArtistUpload profileImage;

    private ArtistType artistType;

    private LocalDateTime registerDateTime;

    private LocalDateTime updateDateTime;

    @QueryProjection
    public ArtistDto(String nameKR, String nameEN, AgencyDto agencyDto,
        ArtistUpload profileImage, ArtistType artistType, LocalDateTime registerDateTime,
        LocalDateTime updateDateTime) {
        this.nameKR = nameKR;
        this.nameEN = nameEN;
        this.agencyDto = agencyDto;
        this.profileImage = profileImage;
        this.artistType = artistType;
        this.registerDateTime = registerDateTime;
        this.updateDateTime = updateDateTime;
    }
}
