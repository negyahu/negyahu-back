package ga.negyahu.music.artist.entity;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.fileupload.entity.FileUpload;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class ArtistMember implements FileUpload<ArtistMember> {

    @Id
    @GeneratedValue
    @Column(name = "artist_member_id")
    private Long id;

    private String nameKR;

    private String nameEN;

    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_account_id")
    private Account account;

    private String instagram;

    private MemberRole memberRole;

    private State state;

    private String etc;

    public void setArtist(Artist artist) {
        this.artist = artist;
        this.agency = artist.getAgency();
    }

    @Override
    public ArtistMember getEntity() {
        return this;
    }

    @Override
    public Long getFK() {
        return this.id;
    }

    public static ArtistMember getOnlyIdEntity(Long id) {
        return ArtistMember.builder()
            .id(id)
            .build();
    }
}
