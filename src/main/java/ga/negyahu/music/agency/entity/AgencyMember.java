package ga.negyahu.music.agency.entity;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class AgencyMember {

    @Id
    @GeneratedValue
    @Column(name = "agency_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private AgencyRole agencyRole;

    private State state;

    @PrePersist
    private void init() {
        if (agencyRole == null) {
            this.agencyRole = AgencyRole.MEMBER;
        }
        this.state = State.WAIT;
    }

}
