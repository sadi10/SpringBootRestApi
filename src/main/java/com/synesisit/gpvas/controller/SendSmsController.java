package com.synesisit.gpvas.controller;

import com.synesisit.gpvas.dto.GpsendsmsResponse.SendSmsResponse;
import com.synesisit.gpvas.dto.smsRequest.VasSmsRequest;
import com.synesisit.gpvas.service.SendSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("gpvas/api/v1/gp/")
public class SendSmsController {

    @Autowired
    private SendSmsService sendSmsService;

    @Value("${send-gp.url}")
    private String sendSmsUrl;

    @PostMapping("/sendsms")
    public SendSmsResponse smsRequest(@RequestBody VasSmsRequest vasSmsRequest) {

        return sendSmsService.sendSms(vasSmsRequest);
    }

}
