package et.digitalequb.frontend.dto.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@SuperBuilder
@Data
@RequiredArgsConstructor
public class ListPaymentsDto implements Serializable {
    @JsonProperty("RequestRefID")
    public String requestRefID;
    @JsonProperty("Remark")
    public String remark;
    public List<PaymentDto> payments;
}
