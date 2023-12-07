package et.digitalequb.frontend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@SuperBuilder
@Data
@RequiredArgsConstructor
public class RegisterDto implements Serializable {
    @JsonProperty("RequestRefID")
    public String requestRefID;
    @JsonProperty("CommandID")
    public String commandID;
    @JsonProperty("Remark")
    public String remark;
    @JsonProperty("SourceSystem")
    public String sourceSystem;
    @JsonProperty("Version")
    public String version;
    @JsonProperty("Timestamp")
    public Date timestamp;
    public Object payload;

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

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
