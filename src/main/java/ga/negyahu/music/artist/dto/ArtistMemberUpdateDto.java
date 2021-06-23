package ga.negyahu.music.artist.dto;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.entity.Gender;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistMemberUpdateDto {

    private String nameKR;

    private String nameEN;

    private Gender gender;

    private Long agencyId;

    private Long artistId;

    private String instagram;

    private String etc;
}
