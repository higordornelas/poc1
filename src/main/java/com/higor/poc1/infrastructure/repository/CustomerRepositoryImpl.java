package com.higor.poc1.infrastructure.repository;

import com.higor.poc1.domain.model.Customer;
import com.higor.poc1.domain.repository.CustomerRepository;
import com.higor.poc1.domain.repository.CustomerRepositoryQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired @Lazy
    public CustomerRepository customerRepository;

    @Override
    public Page<Customer> find(String name, String email, String registerNumber, String type, String phoneNumber, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        Root<Customer> root = criteria.from(Customer.class);

        var predicates = new ArrayList<Predicate>();

        if (StringUtils.hasText(name)){
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }

        if (StringUtils.hasText(email)){
            predicates.add(builder.like(root.get("email"), "%" + email + "%"));
        }

        if (StringUtils.hasText(registerNumber)){
            predicates.add(builder.like(root.get("registerNumber"), "%" + registerNumber + "%"));
        }

        if (StringUtils.hasText(type)){
            predicates.add(builder.like(root.get("type").as(String.class), "%" + type + "%"));
        }

        if (StringUtils.hasText(phoneNumber)){
            predicates.add(builder.like(root.get("phoneNumber"), "%" + phoneNumber + "%"));
        }

        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Customer> query = manager.createQuery(criteria);

        List<Customer> pageList = query.getResultList().stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return new PageImpl<>(pageList, pageable, pageList.size());
    }
}