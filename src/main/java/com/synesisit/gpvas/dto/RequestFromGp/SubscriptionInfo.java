package com.synesisit.gpvas.dto.RequestFromGp;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionInfo {

    @JsonProperty("productIdentifier")
    private String productIdentifier ;
    @JsonProperty("statusChangedDate")
    private String statusChangedDate ;
    @JsonProperty("subscriptionStatus")
    private String subscriptionStatus ;
    @JsonProperty("registrationDate")
    private String registrationDate;
    @JsonProperty("nextRenewalDate")
    private String nextRenewalDate;
    @JsonProperty("validity")
    private String validity;
    @JsonProperty("subscriptionId")
    private String subscriptionId;
    @JsonProperty("activationDate")
    private String activationDate;
    @JsonProperty("lastRenewalDate")
    private String lastRenewalDate;
    @JsonProperty("lastChangedAmount")
    private String lastChangedAmount;
    @JsonProperty("serviceIdentifier")
    private String serviceIdentifier;

}
