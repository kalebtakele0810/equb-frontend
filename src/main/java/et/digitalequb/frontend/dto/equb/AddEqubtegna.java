package et.digitalequb.frontend.dto.equb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Setter
@Getter
@SuperBuilder
@Data
@RequiredArgsConstructor
public class AddEqubtegna implements Serializable {
    @JsonProperty("Identifier")
    public String Identifier;
    @JsonProperty("EqubId")
    public String EqubId;

    @JsonProperty("NumberOfTime")
    public int numberOfTime;

    @JsonProperty("Status")
    public String status;
    @JsonProperty("SubGroup")
    public int subGroup;

    @JsonProperty("Channel")
    public String channel;
    @JsonProperty("Amount")
    public int amount;
    @JsonProperty("TransactionId")
    public String transactionId;

    public String getIdentifier() {
        return Identifier;
    }

    public void setIdentifier(String identifier) {
        Identifier = identifier;
    }

    public String getEqubId() {
        return EqubId;
    }

    public void setEqubId(String equbId) {
        EqubId = equbId;
    }

    public int getNumberOfTime() {
        return numberOfTime;
    }

    public void setNumberOfTime(int numberOfTime) {
        this.numberOfTime = numberOfTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(int subGroup) {
        this.subGroup = subGroup;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}