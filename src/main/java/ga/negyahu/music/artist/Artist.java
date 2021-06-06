package ga.negyahu.music.artist;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.Agency;
import ga.negyahu.music.subscribe.Subscribe;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Artist {

    @Id
    @GeneratedValue
    @Column(name = "artist_id")
    private Long id;

    @Column(name = "artist_name")
    private String name;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    private List<ArtistMember> members = new ArrayList<>();

    // 구독자 명단
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    private List<Subscribe> subscribes = new ArrayList<>();

}
