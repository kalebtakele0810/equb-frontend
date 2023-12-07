
package et.digitalequb.frontend.entity.equbtegna;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@SuperBuilder
@RequiredArgsConstructor
public class Account {
    private Integer id;

    private String password;

    private String status;


    //////////////Balance
    private String balance;

    private Date createdAt;

    private Date updatedAt;

}

