package com.higor.poc1.domain.repository;

import com.higor.poc1.domain.model.Address;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class AddressRepository {

    @PersistenceContext
    private EntityManager manager;

    public List<Address> list() {
        return manager.createQuery("From Address", Address.class).getResultList();
    }

    public Address findById(Long id) {
        return manager.find(Address.class, id);
    }

    public Address save(Address address) {
        return manager.merge(address);
    }

    public void deleteById(Long id) {
        Address address = findById(id);
        manager.remove(address);
    }
}
