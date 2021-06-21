package ga.negyahu.music.fileupload.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ga.negyahu.music.artist.entity.Artist;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ArtistFileUpload extends BaseFileUpload {

    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Override
    public void createFileName() {
        String[] split = getOriginalName().split("\\.");
        String fileType = split[split.length - 1];
        setFileName(UUID.randomUUID().toString() + "." + fileType);
    }
}
