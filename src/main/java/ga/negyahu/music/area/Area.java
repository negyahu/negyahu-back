package ga.negyahu.music.area;

import ga.negyahu.music.account.Account;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "area")
public class Area {

    @Id @GeneratedValue
    @Column(name = "code")
    private Integer code;

    @Column(length = 60, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "area")
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();
}
