package ga.negyahu.music.account;

import ga.negyahu.music.account.entity.Address;
import ga.negyahu.music.area.Area;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.fileupload.account.AccountFileUpLoad;
import ga.negyahu.music.message.Message;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode( of = "id")
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(length = 40)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 30)
    private String username;

    @Column(length = 30)
    private String nickname;

    private String country;

    private boolean isMemberShip;

    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @Embedded
    private Address address;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate signUpDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Area area;

    private boolean areaCertify;

    /* 연관관계 맵핑*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "account")
    @Builder.Default
    private List<AccountFileUpLoad> fileUpLoads = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    @PrePersist
    public void init(){
        if(Objects.isNull(this.role)){
            this.role = Role.USER;
        }
    }

}
