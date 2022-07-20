package com.synesisit.gpvas.service;

import com.synesisit.gpvas.dto.GpsendsmsResponse.SendSmsResponse;
import com.synesisit.gpvas.dto.smsRequest.VasSmsRequest;

public interface SendSmsService{

    public SendSmsResponse sendSms(VasSmsRequest vasSmsRequest);

}
