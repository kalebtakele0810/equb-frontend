package et.digitalequb.frontend.dto;

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
public class ResponseDto implements Serializable {
    @JsonProperty("RequestRefID")
    public String requestRefID;
    @JsonProperty("Remark")
    public String remark;
    public Object payload;

    public String getRequestRefID() {
        return requestRefID;
    }

    public void setRequestRefID(String requestRefID) {
        this.requestRefID = requestRefID;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }
}
