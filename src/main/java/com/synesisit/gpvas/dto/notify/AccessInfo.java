package com.synesisit.gpvas.dto.notify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessInfo {

    @JsonProperty("endUserId")
    private String endUserId;
    @JsonProperty("servicekey")
    private String serviceKey;
    @JsonProperty("language")
    private String language;
    @JsonProperty("serverReferenceCode")
    private String serverReferenceCode;
    @JsonProperty("referenceCode")
    private String referenceCode;
    @JsonProperty("accesschannel")
    private String accessChannel;

}
