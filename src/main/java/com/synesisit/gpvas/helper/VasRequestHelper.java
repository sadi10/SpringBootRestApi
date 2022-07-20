package com.synesisit.gpvas.helper;


import com.synesisit.gpvas.dto.notify.SubscriptionDto;
import com.synesisit.gpvas.dto.smsRequest.VasSmsRequest;
import com.synesisit.gpvas.model.AccessLog;
import com.synesisit.gpvas.model.IncomingRequest;
import com.synesisit.gpvas.repository.AccessLogRepository;
import com.synesisit.gpvas.repository.IncomingRequestRepo;
import com.synesisit.gpvas.service.iService.ISendSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VasRequestHelper {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private SendSMSHelper sendSMSHelper;

    @Value("${vas-gp.url}")
    private String urlF;

    @Value("${vas-gp.path.subscription}")
    private String subscriptionPath;

    @Value("${message.keyword.invalid}")
    private String msgInvalidKeyword;

    private String parameterAddPortion = "?%s";

    public void subscriptionRequest(IncomingRequest incomingRequest) throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();

        if (keywordValidation(incomingRequest, params)){
            incomingRequest.setStatus("VALID");
            String url = urlF + subscriptionPath+parameterAddPortion;
            String parametrizedArgs = params.keySet().stream().map(k ->
                    String.format("%s={%s}", k, k)
            ).collect(Collectors.joining("&"));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            log.info("Weekly Parameterized Argument: "+parametrizedArgs);
            ResponseEntity<String> responseVas = restTemplate.exchange(String.format(url, parametrizedArgs), HttpMethod.POST, entity, String.class, params);
            if (responseVas.getStatusCode().value() == 200) {
                AccessLog accessLog = new AccessLog();
                accessLog.setId(accessLogRepository.generateUniqueId());
                accessLog.setAccessChannel("mosms");
                accessLog.setMsisdn(incomingRequest.getMsisdn());
                Date date = new Date();
                accessLog.setInsertDate(date);
                accessLog.setProcessed(false);
                accessLog.setRequestBody(parametrizedArgs);
                accessLog.setRequestFrom("Middleware");
                accessLog.setRequestType("Subscription");
                accessLog.setServerReferenceCode(incomingRequest.getServerReferenceCode());
                accessLog.setServiceIdentifier(incomingRequest.getProductIdentifier());
                accessLog.setMessage(incomingRequest.getMessage());
                accessLog.setRequestBody(String.format(url, parametrizedArgs));
                accessLog.setResponseBody(responseVas.getBody());
                accessLog.setTransactionId("" + incomingRequest.getId());
                accessLog.setResponseFrom("vas");
                accessLog.setEndpoint(url);
                accessLog.setClientIp("urlF");
                accessLogRepository.save(accessLog);
            } else {
                log.info("vas Response Error");
                throw new Exception("vas not response");
            }
        }else{
            log.info("Invalid keyword SMS process.....");
            incomingRequest.setStatus("INVALID");
            sendInvalidKeywordSms(incomingRequest);
        }
    }

    public void consentRequest(IncomingRequest incomingRequest) throws Exception{

        log.info("Consent Subscription Request: " + incomingRequest.toString());

        Map<String, Object> params = new HashMap<>();

        if(incomingRequest.getProductIdentifier().equalsIgnoreCase("SUB00058600001")){
            params.put("content", "EPP");
            params.put("service_type", "ondemand");
        }
        if(incomingRequest.getProductIdentifier().equalsIgnoreCase("SUB00058600002")){
            params.put("content", "EPPW");
            params.put("service_type", "weekly");
        }

        if(incomingRequest.getProductIdentifier().equalsIgnoreCase("SUB00058600003")){
            params.put("content", "EPPM");
            params.put("service_type", "monthly");
        }

        params.put("msisdn", incomingRequest.getMsisdn());
        params.put("action","consent");
        params.put("service_src", "sms");
        params.put("contentType", "sms");
        params.put("shortCode", "16445");
        params.put("ins", "Y");
        params.put("transaction_id", incomingRequest.getServerReferenceCode());


        String url = urlF+subscriptionPath+parameterAddPortion;
        String parametrizedArgs = params.keySet().stream().map(k ->
                String.format("%s={%s}", k, k)
        ).collect(Collectors.joining("&"));

        String requestUrl = String.format(url, parametrizedArgs);
        log.info("vas consent request: " + requestUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseVas = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, String.class, params);

        AccessLog accessLog = new AccessLog();
        accessLog.setId(accessLogRepository.generateUniqueId());
        accessLog.setAccessChannel("mosms");
        accessLog.setMsisdn(incomingRequest.getMsisdn());
        accessLog.setInsertDate(new Date());
        accessLog.setProcessed(false);
        accessLog.setRequestBody(parametrizedArgs);
        accessLog.setRequestFrom("Middleware");
        accessLog.setRequestType("consent");
        accessLog.setServerReferenceCode(incomingRequest.getServerReferenceCode());
        accessLog.setServiceIdentifier(incomingRequest.getProductIdentifier());
        accessLog.setMessage(incomingRequest.getMessage());
        accessLog.setRequestBody(requestUrl);
        accessLog.setResponseBody(responseVas.getBody());
        accessLog.setTransactionId("" + incomingRequest.getId());
        accessLog.setResponseFrom("vas");
        accessLog.setEndpoint(url);
        accessLog.setClientIp("urlF");
        accessLogRepository.save(accessLog);

        log.info("vas consent responseCode: " + responseVas.getStatusCode());
        log.info("vas consent response: " + responseVas.getBody());

    }


    private boolean keywordValidation(IncomingRequest incomingRequest, Map<String,Object> params) throws Exception{

        boolean response = false;

        String moMessage = incomingRequest.getMessage().trim();
        String[] keyword = moMessage.split(" ");
        log.info("moMessage Body: " + moMessage );
        log.info("keyword length: " + keyword.length );
        if (keyword.length == 3){
            if (keyword[0].equalsIgnoreCase("START") && keyword[1].equalsIgnoreCase("EPP"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "ondemand");
                params.put("pass_app_id", keyword[2]);
                params.put("ins", keyword[0]);
                response = true;
            }else if (keyword[0].equalsIgnoreCase("START") && keyword[1].equalsIgnoreCase("EPPW"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "weekly");
                params.put("pass_app_id", keyword[2]);
                params.put("ins", keyword[0]);
                response = true;
            }else if (keyword[0].equalsIgnoreCase("START") && keyword[1].equalsIgnoreCase("EPPM"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "monthly");
                params.put("pass_app_id", keyword[2]);
                params.put("ins", keyword[0]);
                response = true;
            }else if (keyword[0].equalsIgnoreCase("STOP") && keyword[1].equalsIgnoreCase("EPP"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "ondemand");
                params.put("pass_app_id", keyword[2]);
                params.put("ins", keyword[0]);
                response = true;
            }else if (keyword[0].equalsIgnoreCase("STOP") && keyword[1].equalsIgnoreCase("EPPW"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "weekly");
                params.put("pass_app_id", keyword[2]);
                params.put("ins", keyword[0]);
                response = true;
            }else if (keyword[0].equalsIgnoreCase("STOP") && keyword[1].equalsIgnoreCase("EPPM"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "monthly");
                params.put("pass_app_id", keyword[2]);
                params.put("ins", keyword[0]);
                response = true;
            }else{
                log.info("Invalid Keyword3: " + moMessage);
                response = false;
            }
        }else if (keyword.length == 2){

            if (keyword[0].equalsIgnoreCase("STOP") && keyword[1].equalsIgnoreCase("EPP"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "ondemand");
                params.put("pass_app_id", "");
                params.put("ins", keyword[0]);
                response = true;
            }else if (keyword[0].equalsIgnoreCase("STOP") && keyword[1].equalsIgnoreCase("EPPW"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "weekly");
                params.put("pass_app_id", "");
                params.put("ins", keyword[0]);
                response = true;
            }else if (keyword[0].equalsIgnoreCase("STOP") && keyword[1].equalsIgnoreCase("EPPM"))
            {
                params.put("service", keyword[1]);
                params.put("service_type", "monthly");
                params.put("pass_app_id", "");
                params.put("ins", keyword[0]);
                response = true;
            }else{
                log.info("Invalid Keyword2: " + moMessage);
                response = false;
            }

        }else if (keyword.length == 1 && moMessage.equalsIgnoreCase("Y")){
            log.info("Consent Keyword1: " + moMessage);
            response = false;
        }else {
            log.info("Invalid Keyword else: " + moMessage);
            response = false;
        }

        params.put("msisdn", incomingRequest.getMsisdn());
        params.put("contentType", "USSD");
        params.put("shortCode", "16445");
        params.put("action", "reg");
        params.put("service_src", "sms");
        params.put("transaction_id", incomingRequest.getServerReferenceCode());

        return response;
    }

    private void sendInvalidKeywordSms(IncomingRequest incomingRequest) throws Exception{

        log.info("sendInvalidKeywordSms method call");
        VasSmsRequest vasSmsRequest = new VasSmsRequest();
        vasSmsRequest.setAccessChannel("mosms");
        vasSmsRequest.setMessage(msgInvalidKeyword);
        vasSmsRequest.setServiceKey("f12f34158141443fbc318e378a6fab24");
        vasSmsRequest.setLanguage("EN");
        vasSmsRequest.setTransactionId("" + incomingRequest.getId());
        vasSmsRequest.setSenderId("16445");
        vasSmsRequest.setMsisdn(incomingRequest.getMsisdn());

        log.info("Invalid Keyword SMS Send RequestBody: " + vasSmsRequest);
        sendSMSHelper.sendSMSToGP(vasSmsRequest, incomingRequest.getId());

    }

}
