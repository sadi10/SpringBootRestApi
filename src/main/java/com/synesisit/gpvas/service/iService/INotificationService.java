package com.synesisit.gpvas.service.iService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synesisit.gpvas.dto.notify.ResponseDto;
import com.synesisit.gpvas.dto.notify.StatusInfo;
import com.synesisit.gpvas.dto.notify.SubscriptionDto;
import com.synesisit.gpvas.model.AccessLog;
import com.synesisit.gpvas.model.IncomingRequest;
import com.synesisit.gpvas.repository.AccessLogRepository;
import com.synesisit.gpvas.repository.IncomingRequestRepo;
import com.synesisit.gpvas.service.NotificationService;
import com.synesisit.gpvas.utility.DateUtils;
import com.synesisit.gpvas.utility.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Service
@Configurable
@Slf4j
public class INotificationService implements NotificationService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Autowired
    private IncomingRequestRepo incomingRequestRepo;


    public ResponseDto processMoRequest(HttpEntity<String> httpEntity, HttpServletRequest request) {
        String serverReferenceCode = "";
        Long accessLogId = 0L;
        AccessLog accessLog = new AccessLog();
        StatusInfo statusInfo = new StatusInfo();
        accessLogId = accessLogRepository.generateUniqueId();
        try {
            log.info("MO RequestBody: " + httpEntity.getBody());
            String moRequestBody = URLDecoder.decode(httpEntity.getBody(), StandardCharsets.UTF_8.name());
            log.info("MO Decode RequestBody: " + moRequestBody);
            SubscriptionDto subscriptionDto = new ObjectMapper().readValue(moRequestBody, SubscriptionDto.class);

            serverReferenceCode = subscriptionDto.getNotify().getAccessInfo().getServerReferenceCode();

            log.info("accessLog: " + accessLogId);
            accessLog.setId(accessLogId);
            accessLog.setAccessChannel(subscriptionDto.getNotify().getAccessInfo().getAccessChannel());
            accessLog.setMsisdn(subscriptionDto.getNotify().getAccessInfo().getEndUserId());
            accessLog.setProcessed(false);
            accessLog.setRequestBody(moRequestBody);
            accessLog.setEndpoint(request.getRequestURI());
            accessLog.setClientIp(NetworkUtils.getClientIpAddress(request));
            accessLog.setMessage(subscriptionDto.getNotify().getSmsInfo().getMessage());
            accessLog.setServiceIdentifier(subscriptionDto.getNotify().getSmsInfo().getServcieIdentifier());
            accessLog.setTransactionId(subscriptionDto.getNotify().getSmsInfo().getMsgTransactionId());
            accessLog.setServerReferenceCode(subscriptionDto.getNotify().getAccessInfo().getServerReferenceCode());
            accessLog.setRequestFrom("GP_DPDP");
            accessLog.setRequestType("MOSMS");
            accessLog.setResponseFrom("Middleware");
            accessLog.setInsertDate(new Date());
            accessLog.setUpdatedDate(new Date());
            accessLogRepository.save(accessLog);

            log.info("Request save to incoming table ....");
            IncomingRequest incomingRequest = incomingRequestRepo.findByServerReferenceCode(subscriptionDto.getNotify().getAccessInfo().getServerReferenceCode());

            if (Objects.isNull(incomingRequest)) {
                incomingRequest = new IncomingRequest();
                incomingRequest.setId(accessLogId);
                incomingRequest.setInsertDate(new Date());
                incomingRequest.setUpdatedDate(new Date());
            }

            if (!subscriptionDto.getNotify().getSmsInfo().getMessage().equalsIgnoreCase("y")){
                incomingRequest.setMsisdn(subscriptionDto.getNotify().getAccessInfo().getEndUserId());
            }
            incomingRequest.setServerReferenceCode(subscriptionDto.getNotify().getAccessInfo().getServerReferenceCode());
            incomingRequest.setLanguage(subscriptionDto.getNotify().getAccessInfo().getLanguage());
            incomingRequest.setMessage(subscriptionDto.getNotify().getSmsInfo().getMessage());
            incomingRequest.setProductIdentifier(subscriptionDto.getNotify().getSmsInfo().getServcieIdentifier());
            log.info("insert to incoming request.. " + incomingRequest.toString());
            incomingRequest.setIsProcessed(0);
            incomingRequest.setIsSent(0);
            incomingRequestRepo.save(incomingRequest);



            statusInfo.setServerReferenceCode(subscriptionDto.getNotify().getAccessInfo().getServerReferenceCode());
            statusInfo.setStatusCode("200");
            statusInfo.setReferenceCode("" + accessLogId);

            accessLog.setResponseBody(new ObjectMapper().writeValueAsString(statusInfo));
            accessLog.setUpdatedDate(new Date());
            accessLogRepository.save(accessLog);
            //log.info("response sent to gp.. " + new ObjectMapper().writeValueAsString(statusInfo));

            return new ResponseDto(statusInfo);
        } catch (Exception e) {
            log.error("MO Request Error: " + e.getMessage());
            statusInfo.setServerReferenceCode(serverReferenceCode);
            statusInfo.setStatusCode("500");
            statusInfo.setReferenceCode("" + accessLogId);
            accessLog.setId(accessLogId);
            accessLog.setResponseBody(statusInfo + " error: " +  e.getMessage());
            accessLog.setUpdatedDate(new Date());
            accessLogRepository.save(accessLog);
            log.info("response sent to GP.. " + statusInfo);
            e.printStackTrace();
            return new ResponseDto(statusInfo);
        }

    }


    public ResponseDto processSubscription(HttpEntity<String> httpEntity, HttpServletRequest request) {

        String serverReferenceCode = "";
        Long accessLogId = 0L;
        StatusInfo statusInfo = new StatusInfo();
        AccessLog accessLog = new AccessLog();
        accessLogId = accessLogRepository.generateUniqueId();
        log.info("accessLogId: " + accessLogId);
        try {
            String subscriptionRequestBody = httpEntity.getBody();
            String fromApi = request.getRemoteHost();
            log.info("Subscription Request: " + subscriptionRequestBody);
            log.info("Request From: " + fromApi);

            SubscriptionDto subscriptionDto = new ObjectMapper().readValue(subscriptionRequestBody, SubscriptionDto.class);

            serverReferenceCode = subscriptionDto.getNotify().getAccessInfo().getReferenceCode();

            log.info("Request save to access log table .....");
            accessLog.setId(accessLogId);
            accessLog.setAccessChannel(subscriptionDto.getNotify().getAccessInfo().getAccessChannel());
            accessLog.setMsisdn(subscriptionDto.getNotify().getAccessInfo().getEndUserId());
            accessLog.setRequestBody(subscriptionRequestBody);
            accessLog.setServerReferenceCode(serverReferenceCode);
            accessLog.setServiceIdentifier(subscriptionDto.getNotify().getSubscriptionInfo().getServiceIdentifier());
            accessLog.setEndpoint(request.getRequestURI());
            accessLog.setClientIp(NetworkUtils.getClientIpAddress(request));
            accessLog.setSubscriptionId(subscriptionDto.getNotify().getSubscriptionInfo().getSubscriptionId());
            accessLog.setInsertDate(new Date());
            accessLog.setProcessed(false);
            accessLog.setRequestFrom("GP_DPDP");
            accessLog.setResponseFrom("Middleware");
            if (subscriptionDto.getNotify().getSubscriptionInfo().getSubscriptionStatus().equals("0")) {
                accessLog.setRequestType("subscription");
            }
            if (subscriptionDto.getNotify().getSubscriptionInfo().getSubscriptionStatus().equals("1")) {
                accessLog.setRequestType("consent");
            }
            if (subscriptionDto.getNotify().getSubscriptionInfo().getSubscriptionStatus().equals("9")) {
                accessLog.setRequestType("unsubscribe");
            }
            accessLog.setInsertDate(new Date());
            accessLogRepository.save(accessLog);

            log.info("Request save to incoming Request Table.....");
            IncomingRequest incomingRequest = incomingRequestRepo.findByServerReferenceCode(subscriptionDto.getNotify().getAccessInfo().getReferenceCode());

            if (Objects.isNull(incomingRequest)) {
                log.info("subscription id not found: ");
                incomingRequest = new IncomingRequest();
                incomingRequest.setId(accessLogId);
                incomingRequest.setInsertDate(new Date());
                incomingRequest.setUpdatedDate(new Date());
            }
            incomingRequest.setMsisdn(subscriptionDto.getNotify().getAccessInfo().getEndUserId());
            incomingRequest.setSubscriptionId(subscriptionDto.getNotify().getSubscriptionInfo().getSubscriptionId());
            incomingRequest.setRegistrationDate(DateUtils.parse(subscriptionDto.getNotify().getSubscriptionInfo().getRegistrationDate(), DateUtils.ISO));
            incomingRequest.setSubscriptionStatus(subscriptionDto.getNotify().getSubscriptionInfo().getSubscriptionStatus());
            incomingRequest.setProductIdentifier(subscriptionDto.getNotify().getSubscriptionInfo().getServiceIdentifier());
            incomingRequest.setServerReferenceCode(serverReferenceCode);
            /*if (Objects.nonNull(subscriptionDto.getNotify().getSubscriptionInfo().getNextRenewalDate())) {
                incomingRequest.setNextRenewalDate(DateUtils.parse(subscriptionDto.getNotify().getSubscriptionInfo().getNextRenewalDate(), DateUtils.ISO));
            }*/
            if (Objects.nonNull(subscriptionDto.getNotify().getSubscriptionInfo().getStatusChangedDate())) {
                incomingRequest.setStatusChangedDate(DateUtils.parse(subscriptionDto.getNotify().getSubscriptionInfo().getStatusChangedDate(), DateUtils.ISO));
            }
            if (Objects.nonNull(subscriptionDto.getNotify().getSubscriptionInfo().getActivationDate())) {
                incomingRequest.setActivationDate(DateUtils.parse(subscriptionDto.getNotify().getSubscriptionInfo().getActivationDate(), DateUtils.ISO));
            }
            incomingRequest.setIsProcessed(0);
            incomingRequest.setIsSent(0);
            log.info("insert to incoming request.. " + incomingRequest.toString());
            incomingRequestRepo.save(incomingRequest);

            statusInfo.setServerReferenceCode(serverReferenceCode);
            statusInfo.setStatusCode("200");
            statusInfo.setReferenceCode(String.valueOf(accessLog.getId()));

            accessLog.setResponseBody(new ObjectMapper().writeValueAsString(statusInfo));
            accessLog.setProcessed(true);
            accessLog.setUpdatedDate(new Date());
            accessLogRepository.save(accessLog);
            log.info("response sent to GP.. " + new ObjectMapper().writeValueAsString(statusInfo));

            return new ResponseDto(statusInfo);
        } catch (Exception e) {
            log.error("Subscription Request Error: " + e.getMessage());
            statusInfo.setServerReferenceCode(serverReferenceCode);
            statusInfo.setStatusCode("500");
            statusInfo.setReferenceCode("" + accessLogId);

            accessLog.setId(accessLogId);
            accessLog.setResponseBody(statusInfo + " error: " +  e.getMessage());
            accessLog.setUpdatedDate(new Date());
            accessLogRepository.save(accessLog);
            log.info("response sent to GP.. " + statusInfo);
            e.printStackTrace();
            return new ResponseDto(statusInfo);
        }

    }


}
