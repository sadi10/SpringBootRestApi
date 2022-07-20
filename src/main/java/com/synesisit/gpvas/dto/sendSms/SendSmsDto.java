package com.synesisit.gpvas.dto.sendSms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.synesisit.gpvas.dto.notify.AccessInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class SendSmsDto {

   @JsonProperty("accessInfo")
   private AccessInfo accessInfo;

   @JsonProperty("smsInfo")
   private SmsInfo smsInfo;



}
