package et.digitalequb.frontend.dto.equbtegna;

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
public class ViewEqubtegnaDto implements Serializable {
    @JsonProperty("RequestRefID")
    public String requestRefID;
    @JsonProperty("CommandID")
    public String commandID;
    @JsonProperty("Remark")
    public String remark;
    @JsonProperty("Identifier")
    public String identifier;

    public String getRequestRefID() {
        return requestRefID;
    }

    public void setRequestRefID(String requestRefID) {
        this.requestRefID = requestRefID;
    }

    public String getCommandID() {
        return commandID;
    }

    public void setCommandID(String commandID) {
        this.commandID = commandID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
