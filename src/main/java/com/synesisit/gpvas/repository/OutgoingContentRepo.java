package com.synesisit.gpvas.repository;

import com.synesisit.gpvas.model.OutgoingContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OutgoingContentRepo extends JpaRepository<OutgoingContent, Long> {

    @Query(value = "SELECT (COALESCE(MAX(id)+1, CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '00000000001'))) id " +
            " FROM `outgoing_content` " +
            " where left(id, 8) = CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'))",nativeQuery = true)
    Long generateUniqueId();

}
