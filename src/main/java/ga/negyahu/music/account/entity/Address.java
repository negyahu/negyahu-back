package ga.negyahu.music.account.entity;

import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Address {

    private String zipcode;

    private String roadAddress;

    private String detailAddress;

    @Override
    public String toString() {
        return String.format("%s, %s, (%s)",roadAddress,detailAddress,zipcode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(zipcode, address.zipcode) && Objects
            .equals(roadAddress, address.roadAddress) && Objects
            .equals(detailAddress, address.detailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipcode, roadAddress, detailAddress);
    }
}
