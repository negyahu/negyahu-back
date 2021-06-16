package ga.negyahu.music.agency.entity;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "agency_member",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"agency_id", "account_id"}
        )
    }
)
public class AgencyMember {

    @Id
    @GeneratedValue
    @Column(name = "agency_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @Column(unique = true)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private AgencyRole agencyRole;

    private State state;

    @CreatedDate
    private LocalDate registerDate;

    @PrePersist
    private void init() {
        if (agencyRole == null) {
            this.agencyRole = AgencyRole.MEMBER;
        }
        this.state = State.WAIT;
    }

}
