package ga.negyahu.music.agency.entity;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.artist.Artist;
import ga.negyahu.music.artist.ArtistMember;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Table(name = "agency")
@EntityListeners(AuditingEntityListener.class)
public class Agency {

    @Id
    @GeneratedValue
    @Column(name = "agency_id")
    private Long id;

    private String name;

    private String nameEN;

    private String businessNumber;

    private String ceoName;

    private String mobile;

    @Enumerated(EnumType.STRING)
    private State state;

    @CreatedDate
    private LocalDate signUpDate;

    @LastModifiedDate
    private LocalDate updateDateTime;

    // 연관관계 맵핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Builder.Default
    private Set<AgencyMember> agencyMembers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Builder.Default
    private List<Artist> artist = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    @Builder.Default
    private List<ArtistMember> artistMembers = new ArrayList<>();

    public void addMembers(Iterable<AgencyMember> agencyMembers) {
        for (AgencyMember member : agencyMembers) {
            this.agencyMembers.add(member);
            member.setAgency(this);
        }
    }

    public boolean isOwner(Account account){
        return this.account.getId().equals(account.getId());
    }
}
