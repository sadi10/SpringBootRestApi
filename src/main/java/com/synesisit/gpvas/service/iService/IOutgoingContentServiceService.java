package com.synesisit.gpvas.service.iService;

import com.synesisit.gpvas.model.OutgoingContent;
import com.synesisit.gpvas.repository.OutgoingContentRepo;
import com.synesisit.gpvas.service.OutgoingContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class IOutgoingContentServiceService implements OutgoingContentService {

    @Autowired
    private OutgoingContentRepo outgoingContentRepo;

    @Override
    @Transactional(readOnly = true)
    public OutgoingContent findById(Long id) {
        return outgoingContentRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutgoingContent> findAll() {
        return outgoingContentRepo.findAll();
    }

    @Override
    @Transactional
    public void save(OutgoingContent entity) {
        outgoingContentRepo.save(entity);
    }

    @Override
    @Transactional
    public void delete(OutgoingContent entity) {
        outgoingContentRepo.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Long generateUniqueId() {
        return outgoingContentRepo.generateUniqueId();
    }
}
