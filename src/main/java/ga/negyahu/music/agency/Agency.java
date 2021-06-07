package ga.negyahu.music.agency;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.artist.Artist;
import ga.negyahu.music.artist.ArtistMember;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Agency {

    @Id @GeneratedValue
    @Column(name = "agency_id")
    private Long id;

    private String name;

    private String businessNumber;

    private String url;

    @Enumerated(EnumType.STRING)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CEO")
    private Account account;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    private List<AgencyMember> agencyMembers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    private List<Artist> artist = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "agency")
    private List<ArtistMember> artistMembers = new ArrayList<>();

}
