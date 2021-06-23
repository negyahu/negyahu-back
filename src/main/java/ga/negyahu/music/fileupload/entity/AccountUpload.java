package ga.negyahu.music.fileupload.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ga.negyahu.music.account.Account;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AccountUpload extends BaseFileUpload {

    public AccountUpload(MultipartFile file, String filePath, String type) {
        super(file, filePath, type);
    }

    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

}
