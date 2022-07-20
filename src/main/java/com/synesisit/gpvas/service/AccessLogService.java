package com.synesisit.gpvas.service;

import com.synesisit.gpvas.model.AccessLog;

public interface AccessLogService extends BaseService<AccessLog> {

    Long getTransactionId();

}
