package com.synesisit.gpvas.dto.notify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsInfo {

    @JsonProperty("totalAmountCharged")
    private String totalAmountCharged;
    @JsonProperty("msgType")
    private String msgType;
    @JsonProperty("productIdentifier")
    private String productIdentifier;
    @JsonProperty("msgTransactionId")
    private String msgTransactionId;
    @JsonProperty("servcieIdentifier")
    private String servcieIdentifier;
    @JsonProperty("shortcode")
    private String shortcode;
    @JsonProperty("message")
    private String message;



}
