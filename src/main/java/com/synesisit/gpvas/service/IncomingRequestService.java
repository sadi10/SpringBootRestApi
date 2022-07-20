package com.synesisit.gpvas.service;

import com.synesisit.gpvas.model.IncomingRequest;

public interface IncomingRequestService extends BaseService<IncomingRequest>{

    Long generateUniqueId();
    IncomingRequest findByServerReferenceCode(String code);
}
