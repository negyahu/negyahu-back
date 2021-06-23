package ga.negyahu.music.fileupload.entity;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.entity.ArtistMember;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class ArtistMemberUpload extends BaseFileUpload {

    public ArtistMemberUpload(MultipartFile file,String filePath,String type) {
        super(file,filePath,type);
    }

    @Id
    @GeneratedValue
    @Column(name = "member_file_id")
    private Long id;

    @OneToOne
    private ArtistMember member;

    public ArtistMemberUpload() {

    }
}
