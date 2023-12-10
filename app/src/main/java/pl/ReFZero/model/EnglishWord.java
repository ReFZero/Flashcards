package pl.ReFZero.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class EnglishWord {
    private Integer id;
    private String polish;
    private String english;
}
