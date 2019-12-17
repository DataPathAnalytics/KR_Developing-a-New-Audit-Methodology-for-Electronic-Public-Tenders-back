package com.datapath.kg.risks.loader.dao.service;

import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import com.datapath.kg.risks.loader.dao.repository.TenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TenderDAOService {

    @Autowired
    private TenderRepository repository;

    public Optional<TenderEntity> findById(Integer id) {
        return repository.findById(id);
    }

    public List<TenderEntity> getTenders() {
        return repository.findAll();
    }

    public TenderEntity getTender(Integer id) {
        return repository.getOne(id);
    }

    public List<Integer> findAllIds() {
        return repository.findAllIds();
    }

    public List<Integer> getKRAI5AcceptableTenders() {
        return repository.getKRAI5AcceptableTenders();
    }

    public List<Integer> getKRAI8AcceptableTenders() {
        return repository.getKRAI8AcceptableTenders();
    }

    public List<Integer> getKRAI12AcceptableTenders() {
        return repository.getKRAI12AcceptableTenders();
    }

    public List<Integer> getKRAI13AcceptableTenders() {
        return repository.getKRAI13AcceptableTenders();
    }

    public List<Integer> getKRAI19AcceptableTenders() {
        return repository.getKRAI19AcceptableTenders();
    }

    public List<Integer> getKRAI21AcceptableTenders() {
        return repository.getKRAI21AcceptableTenders();
    }

    public List<Integer> getKRAI22AcceptableTenders() {
        return repository.getKRAI22AcceptableTenders();
    }

    public List<Integer> getKRAI29AcceptableTenders() {
        return repository.getKRAI29AcceptableTenders();
    }

    public List<Integer> getKRAI30AcceptableTenders() {
        return repository.getKRAI30AcceptableTenders();
    }

    public List<Integer> getKRAI31AcceptableTenders() {
        return repository.getKRAI31AcceptableTenders();
    }

    public List<Integer> getKRAI32AcceptableTenders() {
        return repository.getKRAI32AcceptableTenders();
    }
}
