package ga.negyahu.music.artist.dto;

import ga.negyahu.music.artist.ArtistMember;
import ga.negyahu.music.subscribe.Subscribe;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistCreateDto {

    @Column(name = "artist_name_kr", nullable = false, length = 30)
    private String name;

    @Column(name = "artist_name_en", nullable = false, length = 20)
    private String nameEN;

    @CreatedDate
    private LocalDateTime registerDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    @Builder.Default
    private List<ArtistMember> members = new ArrayList<>();

    // 구독자 명단
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "artist")
    @Builder.Default
    private List<Subscribe> subscribes = new ArrayList<>();

}
