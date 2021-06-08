package ga.negyahu.music.agency.dto;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.artist.Artist;
import ga.negyahu.music.artist.ArtistMember;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyDto {

    private Long id;

    private String name;

    private String nameEN;

    private String ceoName;

    private String mobile;

//    private Account account; // 대표계정의 상세정보 대신, 이메일만 노출
    private String adminEmail;

}
