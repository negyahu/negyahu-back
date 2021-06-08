package ga.negyahu.music.artist;

import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.subscribe.Subscribe;
import java.util.ArrayList;
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

    @Column(name = "artist_name_kr",nullable = false,length = 30)
    private String nameKR;

    @Column(name = "artist_name_en",nullable = false,length = 20)
    private String nameEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    private ArtistType artistType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    @Builder.Default
    private List<ArtistMember> members = new ArrayList<>();

    // 구독자 명단
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    @Builder.Default
    private List<Subscribe> subscribes = new ArrayList<>();

}
