package ga.negyahu.music;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrollPageable {

    private Long from;

    private Integer size = 50;

    private String type;

    private String keyword;

}
