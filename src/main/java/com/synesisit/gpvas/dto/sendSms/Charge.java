package com.synesisit.gpvas.dto.sendSms;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Charge {

    @JsonProperty("serviceIdentifier")
    private String serviceIdentifier;
    @JsonProperty("code")
    private String code;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("taxAmount")
    private String taxAmount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("currency")
    private String currency;





}
