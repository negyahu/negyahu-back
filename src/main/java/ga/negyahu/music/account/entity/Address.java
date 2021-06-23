package ga.negyahu.music.account.entity;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Address {

    private String zipcode;

    private String roadAddress;

    private String detailAddress;

    @Override
    public String toString() {
        return String.format("%s, %s, (%s)", roadAddress, detailAddress, zipcode);
    }

}