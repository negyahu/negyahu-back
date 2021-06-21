package ga.negyahu.music.artist.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.entity.ArtistType;
import ga.negyahu.music.fileupload.entity.ArtistFileUpload;
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

    private ArtistFileUpload profileImage;

    private ArtistType artistType;

    private LocalDateTime registerDateTime;

    private LocalDateTime updateDateTime;

    @QueryProjection
    public ArtistDto(String nameKR, String nameEN, AgencyDto agencyDto,
        ArtistFileUpload profileImage, ArtistType artistType, LocalDateTime registerDateTime,
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
