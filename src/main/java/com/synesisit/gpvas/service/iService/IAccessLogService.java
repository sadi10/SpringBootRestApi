package com.synesisit.gpvas.service.iService;

import com.synesisit.gpvas.model.AccessLog;
import com.synesisit.gpvas.repository.AccessLogRepository;
import com.synesisit.gpvas.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class IAccessLogService implements AccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    @Transactional(readOnly = true)
    public AccessLog findById(Long id) {
        return accessLogRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessLog> findAll() {
        return accessLogRepository.findAll();
    }

    @Override
    @Transactional
    public void save(AccessLog entity) {
        accessLogRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(AccessLog entity) {
        accessLogRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTransactionId() {
        return accessLogRepository.generateUniqueId();
    }
}
