package com.synesisit.gpvas.dto.smsRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VasSmsRequest {

    @JsonProperty("serviceKey")
    private String serviceKey;

    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("accessChannel")
    private String accessChannel;

    @JsonProperty("transactionId")
    private String transactionId;// unique transaction code given by our system to send Gp

    @JsonProperty("language")
    private String language;

    @JsonProperty("senderId")
    private String senderId;

    @JsonProperty("message")
    private String message;









}
