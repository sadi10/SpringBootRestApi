package com.synesisit.gpvas.repository;

import com.synesisit.gpvas.model.IncomingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IncomingRequestRepo extends JpaRepository<IncomingRequest,Long> {

    IncomingRequest findByServerReferenceCode(String code);
//    IncomingRequest findTopByMsisdnOrderByIdDesc(String msisdn);

    @Query(value = "SELECT (COALESCE(MAX(id)+1, CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '00000000001'))) id " +
            " FROM `incoming_request` " +
            " where left(id, 8) = CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'))",nativeQuery = true)
    Long generateUniqueId();

    IncomingRequest findTopByIsProcessedAndIsSentAndMessageIsNotNullOrderById(Integer isProcessed, Integer isSent);
}
