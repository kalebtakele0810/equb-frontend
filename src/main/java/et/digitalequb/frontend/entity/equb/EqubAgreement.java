
package et.digitalequb.frontend.entity.equb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class EqubAgreement {
    private Integer id;
    private String title;
    private String body;
    private Date createdAt;
    private Date updatedAt;
}

