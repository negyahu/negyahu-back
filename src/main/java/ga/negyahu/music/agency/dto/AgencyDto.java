package ga.negyahu.music.agency.dto;

import com.querydsl.core.annotations.QueryProjection;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.artist.Artist;
import ga.negyahu.music.artist.ArtistMember;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@Builder
@Schema(description = "사용자", name = "AgencyDto")
public class AgencyDto {

    @Schema(defaultValue = "4", description = "소속사 고유번호", example = "1")
    private Long id;

    @Schema(defaultValue = "우정엔터테이먼트", description = "소속사 이름(한)", example = "우정엔터테이먼트")
    private String name;

    @Schema(defaultValue = "YouzhentENT", description = "소속사 이름(영)", example = "YouzhentENT")
    private String nameEN;

    @Schema(defaultValue = "000-00-00000", description = "사업자등록번호", example = "000-00-00000")
    private String businessNumber;

    @Schema(defaultValue = "양우정", description = "소속사대표 이름", example = "양우정")
    private String bossName;

    @Schema(defaultValue = "010-1234-5678", description = "연락처", example = "010-1234-5678")
    private String mobile;

    @Schema(defaultValue = "ACTIVE", description = "소속사 상태 ex)WAIT:승인대기,ACTIVE:활성화", example = "ACTIVE")
    private State state;

    @Schema(defaultValue = "2021-01-01", description = "소속사 등록일", example = "2021-01-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate signUpDate;

    @Schema(defaultValue = "youzheng.ent@gmail.com", description = "대표 이메일, 로그인시 사용", example = "youzheng.ent@gmail.com")
    private String email;

    @QueryProjection
    public AgencyDto(Long id, String name, String nameEN, String businessNumber,
        String bossName, String mobile, State state, LocalDate signUpDate, String email) {
        this.id = id;
        this.name = name;
        this.nameEN = nameEN;
        this.businessNumber = businessNumber;
        this.bossName = bossName;
        this.mobile = mobile;
        this.state = state;
        this.signUpDate = signUpDate;
        this.email = email;
    }
}
