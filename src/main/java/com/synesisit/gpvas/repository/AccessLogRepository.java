package com.synesisit.gpvas.repository;

import com.synesisit.gpvas.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    @Query(value = "SELECT (COALESCE(MAX(id)+1, CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '00000000001'))) id " +
            " FROM `tbl_access_log` " +
            " where left(id, 8) = CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'))",nativeQuery = true)
    Long generateUniqueId();


}
