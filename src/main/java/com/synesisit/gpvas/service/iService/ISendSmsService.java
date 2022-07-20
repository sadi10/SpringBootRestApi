package com.synesisit.gpvas.service.iService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synesisit.gpvas.dto.GpsendsmsResponse.ErrorInfo;
import com.synesisit.gpvas.dto.GpsendsmsResponse.SendSmsResponse;
import com.synesisit.gpvas.dto.GpsendsmsResponse.StatusInfo;
import com.synesisit.gpvas.dto.notify.AccessInfo;
import com.synesisit.gpvas.dto.notify.ResponseDto;
import com.synesisit.gpvas.dto.sendSms.SendSmsDto;
import com.synesisit.gpvas.dto.sendSms.SmsInfo;
import com.synesisit.gpvas.dto.smsRequest.VasSmsRequest;
import com.synesisit.gpvas.model.AccessLog;
import com.synesisit.gpvas.model.OutgoingContent;
import com.synesisit.gpvas.repository.AccessLogRepository;
import com.synesisit.gpvas.repository.OutgoingContentRepo;
import com.synesisit.gpvas.service.SendSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
@Slf4j
public class ISendSmsService implements SendSmsService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private OutgoingContentRepo outgoingContentRepo;

    @Value("${send-gp.url}")
    private String sendSmsUrl;


    @Transactional
    public SendSmsResponse sendSms(VasSmsRequest vasSmsRequest) {
        SendSmsDto sendSmsDto = new SendSmsDto();
        AccessInfo accessInfo = new AccessInfo();
        SmsInfo smsInfo = new SmsInfo();
        Long accessLongId = accessLogRepository.generateUniqueId();


        ResponseDto responseDto = new ResponseDto();

        log.info("vas sms incoming request: " + vasSmsRequest.toString());

        AccessLog accessLog = new AccessLog();
        accessLog.setId(accessLongId);
       // accessLog.setTransactionId(vasSmsRequest.getTransactionId());//unique transaction code given by our system to send Gp
        accessLog.setTransactionId(String.valueOf(accessLongId));
        accessLog.setMsisdn(vasSmsRequest.getMsisdn());
        accessLog.setAccessChannel(vasSmsRequest.getAccessChannel());
//        access.setResponseBody(vasSmsRequest.toString());

        accessLog.setMessage(vasSmsRequest.getMessage());
        accessLog.setRequestBody(vasSmsRequest.toString());
        accessLog.setRequestFrom("VAS");
        accessLog.setInsertDate(new Date());


        //gp requestBody
        accessInfo.setEndUserId(vasSmsRequest.getMsisdn());
        accessInfo.setServiceKey(vasSmsRequest.getServiceKey());
        accessInfo.setAccessChannel(vasSmsRequest.getAccessChannel());
        accessInfo.setReferenceCode(vasSmsRequest.getTransactionId());

       // smsInfo.setMsgTransactionId(vasSmsRequest.getTransactionId());
        smsInfo.setMsgTransactionId(String.valueOf(accessLongId));
        smsInfo.setLanguage(vasSmsRequest.getLanguage());
        smsInfo.setSenderId(vasSmsRequest.getSenderId());
        smsInfo.setMessage(vasSmsRequest.getMessage());
        smsInfo.setMsgType("Text");
        smsInfo.setValidity("1");
        smsInfo.setDeliveryReport("1");

        sendSmsDto.setAccessInfo(accessInfo);
        sendSmsDto.setSmsInfo(smsInfo);


        //save to outgoing content table
        OutgoingContent outgoingContent = new OutgoingContent();
        outgoingContent.setId(accessLongId);
        outgoingContent.setMsisdn(vasSmsRequest.getMsisdn().trim());
        outgoingContent.setTransactionId(vasSmsRequest.getTransactionId().trim());
        outgoingContent.setReferenceCode(vasSmsRequest.getTransactionId());
        outgoingContent.setContent(vasSmsRequest.getMessage());
        outgoingContent.setAccessChannel(vasSmsRequest.getAccessChannel());
        outgoingContent.setSenderId(vasSmsRequest.getSenderId());
        outgoingContent.setLanguage(vasSmsRequest.getLanguage());
        outgoingContent.setServiceKey(vasSmsRequest.getServiceKey());
        outgoingContent.setReceivedDate(new Date());
        log.info("saving into database: " + outgoingContent);

        SendSmsResponse sendSmsResponse = new SendSmsResponse();
        StatusInfo statusInfo = new StatusInfo();
        ErrorInfo errorInfo = new ErrorInfo();

        try {
            log.info("called from yml file:  " + sendSmsUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            log.info("GPDPDP SMS RequestUrl: " + sendSmsUrl);
            log.info("GPDPDP SMS RequestBody: " + sendSmsDto.toString());
            outgoingContent.setSendDate(new Date());
            HttpEntity<SendSmsDto> entity = new HttpEntity<SendSmsDto>(sendSmsDto, headers);
            ResponseEntity<String> responseVas = restTemplate.exchange(String.format(sendSmsUrl), HttpMethod.POST, entity, String.class);
            log.info("GPDPDP SMS Response: " + responseVas.getBody());

            accessLog.setRequestBody(sendSmsDto.toString());
            accessLog.setResponseBody(responseVas.getBody());
            accessLogRepository.save(accessLog);


            //TODO process gp incoming response and send to vas
            String gpResponse = responseVas.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            sendSmsResponse = objectMapper.readValue(gpResponse, SendSmsResponse.class);
            log.info("statusInfo :" + sendSmsResponse.getStatusInfo().getStatusCode());
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
            outgoingContent.setDeliveryDate(new Date());
            outgoingContent.setStatus(sendSmsResponse.getStatusInfo().getStatusCode());

            outgoingContentRepo.save(outgoingContent);
            return sendSmsResponse;
        } catch (Exception exception) {

            //setting response for vas
            statusInfo.setStatusCode("500");
            statusInfo.setReferenceCode("");
            statusInfo.setServerReferenceCode("");

            errorInfo.setErrorCode("500");
            errorInfo.setErrorDescription("Could not send request From Middleware To Gp");

            statusInfo.setErrorInfo(errorInfo);
            sendSmsResponse.setStatusInfo(statusInfo);

            outgoingContent.setId(accessLongId);
            outgoingContent.setStatusCode("500");
            outgoingContent.setStatusDescription(exception.getMessage());
            outgoingContent.setStatus("Failed");
            log.info(exception.getMessage());
            outgoingContentRepo.save(outgoingContent);

            accessLog.setId(accessLongId);
            accessLog.setResponseBody(sendSmsResponse.toString());
            accessLogRepository.save(accessLog);

            return sendSmsResponse;

        }

    }
}
