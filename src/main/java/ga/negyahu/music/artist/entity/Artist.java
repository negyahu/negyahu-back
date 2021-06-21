package ga.negyahu.music.artist.entity;

import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.fileupload.entity.ArtistFileUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.subscribe.Subscribe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Artist implements FileUpload {

    @Id
    @GeneratedValue
    @Column(name = "artist_id")
    private Long id;

    @Column(name = "artist_name_kr", nullable = false, length = 30)
    private String nameKR;

    @Column(name = "artist_name_en", nullable = false, length = 20)
    private String nameEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;

    private ArtistType artistType;

    @CreatedDate
    private LocalDateTime registerDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    @OneToOne(fetch = FetchType.LAZY)
    private ArtistFileUpload profileImage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    @Builder.Default
    private List<ArtistMember> members = new ArrayList<>();

    // 구독자 명단
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    @Builder.Default
    private List<Subscribe> subscribes = new ArrayList<>();

    public void setProfileImage(ArtistFileUpload profileImage) {
        this.profileImage = profileImage;
        profileImage.setArtist(this);
    }

    @Override
    public Object getEntity() {
        return this;
    }

    @Override
    public Long getFK() {
        return this.getId();
    }
}
