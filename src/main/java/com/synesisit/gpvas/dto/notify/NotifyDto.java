package com.synesisit.gpvas.dto.notify;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotifyDto {

    @JsonProperty("accesInfo")
    @JsonAlias("accessInfo")
    private AccessInfo accessInfo;

    @JsonProperty("smsInfo")
    private SmsInfo smsInfo;

    @JsonProperty("subscriptionInfo")
    private SubscriptionInfo subscriptionInfo ;

}
