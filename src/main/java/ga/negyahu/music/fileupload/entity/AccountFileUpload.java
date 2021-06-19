package ga.negyahu.music.fileupload.entity;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import ga.negyahu.music.account.Account;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AccountFileUpload extends BaseFileUpload {

    public AccountFileUpload(MultipartFile file, String filePath, Account account) {
        super(file, filePath);
        this.account = account;
        createFileName();
    }

    @Id
    @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    public void createFileName() {
//        String[] split = getOriginalName().split("\\.");
//        String fileType = split[split.length - 1];
        super.setFileName(account.getId() + "." + "png");
    }
}
