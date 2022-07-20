package com.synesisit.gpvas.dto.RequestFromGp;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.synesisit.gpvas.dto.notify.AccessInfo;
import com.synesisit.gpvas.dto.notify.SmsInfo;
import com.synesisit.gpvas.dto.notify.SubscriptionInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotifyDto {

    @JsonProperty("accessInfo")
    //@JsonAlias("accessInfo")
    private AccessInfo accessInfo;


    @JsonProperty("subscriptionInfo")
    private SubscriptionInfo subscriptionInfo ;

}
