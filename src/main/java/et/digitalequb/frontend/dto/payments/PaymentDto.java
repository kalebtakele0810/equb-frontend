package et.digitalequb.frontend.dto.payments;

import com.fasterxml.jackson.annotation.JsonProperty;
import et.digitalequb.frontend.entity.payment.Payments;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Data
@RequiredArgsConstructor
public class PaymentDto {
    @JsonProperty("EqubId")
    public int equbId;
    @JsonProperty("EqubtegnaId")
    public int equbtegnaId;

    @JsonProperty("EqubType")
    private String equbType;
    @JsonProperty("EqubtegnaName")
    public String equbtegnaName;
    @JsonProperty("EqubtegnaNumber")
    public String equbtegnaNumber;

    public Payments payment;

    public int getEqubId() {
        return equbId;
    }

    public void setEqubId(int equbId) {
        this.equbId = equbId;
    }

    public int getEqubtegnaId() {
        return equbtegnaId;
    }

    public void setEqubtegnaId(int equbtegnaId) {
        this.equbtegnaId = equbtegnaId;
    }

    public String getEqubType() {
        return equbType;
    }

    public void setEqubType(String equbType) {
        this.equbType = equbType;
    }

    public String getEqubtegnaName() {
        return equbtegnaName;
    }

    public void setEqubtegnaName(String equbtegnaName) {
        this.equbtegnaName = equbtegnaName;
    }

    public String getEqubtegnaNumber() {
        return equbtegnaNumber;
    }

    public void setEqubtegnaNumber(String equbtegnaNumber) {
        this.equbtegnaNumber = equbtegnaNumber;
    }

    public Payments getPayment() {
        return payment;
    }

    public void setPayment(Payments payment) {
        this.payment = payment;
    }
}
