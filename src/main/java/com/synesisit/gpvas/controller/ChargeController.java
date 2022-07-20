package com.synesisit.gpvas.controller;


import com.synesisit.gpvas.helper.LoggerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("gpvas/api/v1")

public class ChargeController {
    @Autowired
    private LoggerHelper service;

    @PostMapping("/charge")
    public String charge(HttpEntity<String> httpEntity, HttpServletRequest request) {
        String response = "synesis";
        try {
            response = httpEntity.getBody();
            //service.fileService(response,request);
            log.info("Notify url:incoming request: {}", response);
            log.info("getRequestURI" + request.getRequestURI());
            log.info("getRemoteAddr" + request.getRemoteAddr());
            log.info("getRemoteHost" + request.getRemoteHost());

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }
    }
}
