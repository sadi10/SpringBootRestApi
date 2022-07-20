package com.synesisit.gpvas.service;

import com.synesisit.gpvas.model.AccessLog;

import java.util.List;

public interface BaseService<T> {

    T findById(Long id);
    List<T> findAll();
    void save(T entity);
    void delete(T entity);
}
