package ga.negyahu.music.fileupload.account;

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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class AccountFileUpLoad {

    @Id @GeneratedValue
    @Column(name = "file_upload_id")
    private Long id;

    @Column(length = 100)   // 사용자가 업로드 당시 설정한 파일이름
    private String originalName;

    @Column(length = 100)   // 실제 저장되는 파일이름 (UUID + 원명)
    private String fileName;

    @Column(length = 100)   // 실제 파일이 저장되는 위치
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private boolean isDeleted;

    private String getFullPath() {
        return this.filePath+fileName;
    }

}
