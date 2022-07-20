package com.synesisit.gpvas.helper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.synesisit.gpvas.dto.GpsendsmsResponse.ErrorInfo;
import com.synesisit.gpvas.dto.GpsendsmsResponse.SendSmsResponse;
import com.synesisit.gpvas.dto.GpsendsmsResponse.StatusInfo;
import com.synesisit.gpvas.dto.notify.AccessInfo;
import com.synesisit.gpvas.dto.sendSms.SendSmsDto;
import com.synesisit.gpvas.dto.sendSms.SmsInfo;
import com.synesisit.gpvas.dto.smsRequest.VasSmsRequest;
import com.synesisit.gpvas.model.OutgoingContent;
import com.synesisit.gpvas.repository.AccessLogRepository;
import com.synesisit.gpvas.repository.OutgoingContentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Slf4j
@Component
public class SendSMSHelper {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private OutgoingContentRepo outgoingContentRepo;

    @Value("${send-gp.url}")
    private String sendSmsUrl;

    public void sendSMSToGP(VasSmsRequest vasSmsRequest, Long id) throws Exception {

        SendSmsDto sendSmsDto = new SendSmsDto();
        AccessInfo accessInfo = new AccessInfo();
        SmsInfo smsInfo = new SmsInfo();

        //gp requestBody
        OutgoingContent outgoingContent = new OutgoingContent();
        outgoingContent.setId(id);

        accessInfo.setEndUserId(vasSmsRequest.getMsisdn());
        accessInfo.setServiceKey(vasSmsRequest.getServiceKey());
        accessInfo.setAccessChannel(vasSmsRequest.getAccessChannel());
        accessInfo.setReferenceCode(vasSmsRequest.getTransactionId());//

        smsInfo.setMsgTransactionId(vasSmsRequest.getTransactionId());//
        smsInfo.setLanguage(vasSmsRequest.getLanguage());
        smsInfo.setSenderId(vasSmsRequest.getSenderId());
        smsInfo.setMessage(vasSmsRequest.getMessage());
        smsInfo.setMsgType("Text");
        smsInfo.setValidity("1");
        smsInfo.setDeliveryReport("1");

        sendSmsDto.setAccessInfo(accessInfo);
        sendSmsDto.setSmsInfo(smsInfo);


        //save to outgoing content table

        outgoingContent.setId(id);
        outgoingContent.setMsisdn(vasSmsRequest.getMsisdn().trim());
        outgoingContent.setTransactionId(vasSmsRequest.getTransactionId().trim());
        outgoingContent.setReferenceCode(vasSmsRequest.getTransactionId());
        outgoingContent.setContent(vasSmsRequest.getMessage());
        outgoingContent.setAccessChannel(vasSmsRequest.getAccessChannel());
        outgoingContent.setSenderId(vasSmsRequest.getSenderId());
        outgoingContent.setLanguage(vasSmsRequest.getLanguage());
        outgoingContent.setServiceKey(vasSmsRequest.getServiceKey());
        log.info("saving into database: " + outgoingContent);
        outgoingContent.setReceivedDate(new Date());
        outgoingContentRepo.save(outgoingContent);

        SendSmsResponse sendSmsResponse = new SendSmsResponse();
        StatusInfo statusInfo = new StatusInfo();
        ErrorInfo errorInfo = new ErrorInfo();


            // TO DO: url has to come from yml file.
            log.info("called from yml file:  " + sendSmsUrl);

            //String url = "https://10.21.11.16:9155/digital5/messaging/v5.2/sendsms";
            outgoingContent.setSendDate(new Date());
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            log.info("GPDPDP SMS RequestUrl: " + sendSmsUrl);
            log.info("GPDPDP SMS RequestBody: " + sendSmsDto.toString());
            HttpEntity<SendSmsDto> entity = new HttpEntity<SendSmsDto>(sendSmsDto, headers);
            ResponseEntity<String> responseVas = restTemplate.exchange(String.format(sendSmsUrl), HttpMethod.POST,
                    entity, String.class);
            log.info("GPDPDP SMS Response: " + responseVas.getBody());

            String gpResponse = responseVas.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            sendSmsResponse = objectMapper.readValue(gpResponse, SendSmsResponse.class);
            log.info("statusinfo :" + sendSmsResponse.getStatusInfo().getStatusCode());
            log.info("GP Response: " + sendSmsResponse.toString());


            if (sendSmsResponse.getStatusInfo().getStatusCode().equals("200")) {
                outgoingContent.setIsSent(1);
                outgoingContent.setIsSuccess(1);
                outgoingContent.setStatus("Success");
                outgoingContent.setStatusDescription("Successfully Sent SMS");
            } else {
                outgoingContent.setIsSuccess(0);
                outgoingContent.setIsSent(1);
                outgoingContent.setStatus("Failed");
                outgoingContent.setStatusCode(sendSmsResponse.getStatusInfo().getStatusCode());
                outgoingContent.setStatusDescription(sendSmsResponse.getStatusInfo().getErrorInfo().getErrorCode() + " |"
                        + sendSmsResponse.getStatusInfo().getErrorInfo().getErrorDescription());
            }
            outgoingContent.setStatusCode(sendSmsResponse.getStatusInfo().getStatusCode());
            outgoingContent.setDeliveryDate(new Date());
            outgoingContentRepo.save(outgoingContent);
    }
}
