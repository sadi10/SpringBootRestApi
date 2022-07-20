package com.synesisit.gpvas.service;

import com.synesisit.gpvas.model.OutgoingContent;

public interface OutgoingContentService extends BaseService<OutgoingContent> {

    Long generateUniqueId();

}
