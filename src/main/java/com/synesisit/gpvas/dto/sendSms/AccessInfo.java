package com.synesisit.gpvas.dto.sendSms;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessInfo {

    @JsonProperty("accessKey")
    private String accessKey;

    @JsonProperty("serviceKey")
    private String serviceKey;

    @JsonProperty("endUserId")
    private String endUserId;

    @JsonProperty("accessChannel")
    private String accessChannel;

    @JsonProperty("referenceCode")
    private String referenceCode;

}
