package ga.negyahu.music.agency.dto;

import com.querydsl.core.annotations.QueryProjection;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.artist.Artist;
import ga.negyahu.music.artist.ArtistMember;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@NoArgsConstructor
@Builder
public class AgencyDto {

    private Long id;

    private String name;

    private String nameEN;

    private String businessNumber;

    private String ceoName;

    private String mobile;

    private State state;

    private LocalDate signUpDate;

    //    private Account account;
    private String email;

    @QueryProjection
    public AgencyDto(Long id, String name, String nameEN, String businessNumber,
        String ceoName, String mobile, State state, LocalDate signUpDate, String email) {
        this.id = id;
        this.name = name;
        this.nameEN = nameEN;
        this.businessNumber = businessNumber;
        this.ceoName = ceoName;
        this.mobile = mobile;
        this.state = state;
        this.signUpDate = signUpDate;
        this.email = email;
    }
}
