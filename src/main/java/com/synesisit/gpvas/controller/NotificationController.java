package com.synesisit.gpvas.controller;


import com.synesisit.gpvas.dto.notify.ResponseDto;
import com.synesisit.gpvas.helper.LoggerHelper;
import com.synesisit.gpvas.repository.AccessLogRepository;
import com.synesisit.gpvas.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("gpvas/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private LoggerHelper loggerHelper;

    @Autowired
    private AccessLogRepository accessLogRepository;


    @PostMapping("subscribe/daily-weekly-monthly")
    public ResponseEntity<?> subscription(HttpEntity<String> httpEntity, HttpServletRequest request) {
        ResponseDto responseDto = notificationService.processSubscription(httpEntity, request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("push-pull/zero-charging")
    public ResponseEntity<?> moRequest(HttpEntity<String> httpEntity, HttpServletRequest request) {
            ResponseDto responseDto = notificationService.processMoRequest(httpEntity, request);
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("sms-delivery-status")
    public String deliveryStatus(HttpEntity<String> httpEntity, HttpServletRequest request) {
        String response = "synesis";
        try {
            response = httpEntity.getBody();
            log.info("zero-charging url:incoming request: {}", response);
            log.info("getRequestURI" + request.getRequestURI());
            log.info("getRemoteAddr" + request.getRemoteAddr());
            log.info("getRemoteHost" + request.getRemoteHost());

            return "api hit success";
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }
    }
}
