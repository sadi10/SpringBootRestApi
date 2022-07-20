package com.synesisit.gpvas.helper;

import com.synesisit.gpvas.model.IncomingRequest;
import com.synesisit.gpvas.repository.IncomingRequestRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class SchedulerHelper {

    @Autowired
    private IncomingRequestRepo incomingRequestRepo;

    @Autowired
    private VasRequestHelper vasRequestHelper;


    public void processIncomingSMS() {
        IncomingRequest incomingRequest = incomingRequestRepo.findTopByIsProcessedAndIsSentAndMessageIsNotNullOrderById(0, 0);
        if (Objects.isNull(incomingRequest)) {
            log.info("No Incoming Request Found");
            return;
        }
        try {
            log.info("Incoming request Processing....");
            incomingRequest.setIsProcessed(1);
            incomingRequestRepo.save(incomingRequest);
            if (incomingRequest.getMessage().trim().equalsIgnoreCase("Y")) {
                log.info("consent request Processing....");
                incomingRequest.setStatus("VALID");
                vasRequestHelper.consentRequest(incomingRequest);
            } else {
                log.info("subscription request Processing....");
                vasRequestHelper.subscriptionRequest(incomingRequest);

            }
            incomingRequest.setIsProcessed(1);
            incomingRequest.setIsSent(1);
            incomingRequestRepo.save(incomingRequest);
        } catch (Exception e) {
            log.error("Incoming request Processing error: " + e.getMessage());
            incomingRequest.setStatus("Error");
            incomingRequestRepo.save(incomingRequest);
            e.printStackTrace();
        }

    }

}
