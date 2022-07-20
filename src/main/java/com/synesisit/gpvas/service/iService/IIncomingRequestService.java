package com.synesisit.gpvas.service.iService;

import com.synesisit.gpvas.model.IncomingRequest;
import com.synesisit.gpvas.repository.IncomingRequestRepo;
import com.synesisit.gpvas.service.IncomingRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class IIncomingRequestService implements IncomingRequestService {

    @Autowired
    private IncomingRequestRepo incomingRequestRepo;

    @Override
    @Transactional(readOnly = true)
    public IncomingRequest findById(Long id) {
        return incomingRequestRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomingRequest> findAll() {
        return incomingRequestRepo.findAll();
    }

    @Override
    @Transactional
    public void save(IncomingRequest entity) {
        incomingRequestRepo.save(entity);
    }

    @Override
    @Transactional
    public void delete(IncomingRequest entity) {
        incomingRequestRepo.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Long generateUniqueId() {
        return incomingRequestRepo.generateUniqueId();
    }

    @Override
    @Transactional(readOnly = true)
    public IncomingRequest findByServerReferenceCode(String code) {
        return incomingRequestRepo.findByServerReferenceCode(code);
    }
}
