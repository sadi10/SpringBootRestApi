package com.synesisit.gpvas.dto.sendSms;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class S2fInfo {


    @JsonProperty("msisdn")
    private String msisdn;
    @JsonProperty("language")
    private String language;
    @JsonProperty("senderId")
    private String senderId;
    @JsonProperty("message")
    private String message;
    @JsonProperty("msgType")
    private String msgType;
    @JsonProperty("validity")
    private String validity;
    @JsonProperty("deliveryReport")
    private String deliveryReport;






}
