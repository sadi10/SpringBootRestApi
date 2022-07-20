package com.synesisit.gpvas.dto.RequestFromGp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.synesisit.gpvas.dto.notify.NotifyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionDto {

    @JsonProperty("notify")
    private NotifyDto notify;
}
