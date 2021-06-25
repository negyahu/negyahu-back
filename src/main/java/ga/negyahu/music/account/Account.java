package ga.negyahu.music.account;

import static java.util.Objects.isNull;

import ga.negyahu.music.account.entity.Address;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.area.Area;
import ga.negyahu.music.fileupload.entity.AccountUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.subscribe.entity.Subscribe;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Account implements FileUpload {
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(length = 40)
    private String email;

    @Column(length = 255)
    private String password;

    @Column(length = 30)
    private String mobile;

    @Column(length = 30)
    private String username;

    private boolean isMemberShip;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private Address address;

    @CreatedDate
    @Column(updatable = false)
    private LocalDate signUpDate;

    @Column
    private LocalDate suspendUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    private Area area;

    private boolean areaCertify;

    @Enumerated(EnumType.STRING)
    private State state;

    private boolean certifiedEmail;

    private String certifyCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_iamge_id")
    private AccountUpload profileImage;

    /* 연관관계 맵핑*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    @Builder.Default
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<AccountUpload> fileUpLoads = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Subscribe> subscribes = new ArrayList<>();

    private void addSubscribes(Iterable<Subscribe> subscribes) {
        for (Subscribe subscribe : subscribes) {
            this.subscribes.add(subscribe);
            subscribe.setAccount(this);
        }
    }

    public void permit(){
        this.state = State.ACTIVE;
    }

    public void ignore(){
        this.state = State.IGNORE;
    }

    @PrePersist
    public void init() {
        if (isNull(this.role)) {
            this.role = Role.USER;
        }
        if (isNull(this.state)) {
            this.state = State.WAIT;
        }
    }

    @Override
    public Object getEntity() {
        return this;
    }

    @Override
    public Long getFK() {
        return null;
    }
}
