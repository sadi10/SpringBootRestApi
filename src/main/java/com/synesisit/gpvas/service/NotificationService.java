package com.synesisit.gpvas.service;

import com.synesisit.gpvas.dto.notify.ResponseDto;
import org.springframework.http.HttpEntity;

import javax.servlet.http.HttpServletRequest;

public interface NotificationService  {
    public ResponseDto processSubscription(HttpEntity<String> httpEntity, HttpServletRequest request);
    public ResponseDto processMoRequest(HttpEntity<String> httpEntity, HttpServletRequest request);


}
