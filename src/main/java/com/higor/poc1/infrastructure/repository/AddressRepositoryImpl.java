package com.higor.poc1.infrastructure.repository;

import com.higor.poc1.domain.model.Address;
import com.higor.poc1.domain.repository.AddressRepositoryQueries;
import com.higor.poc1.domain.repository.AddressRepository;
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
public class AddressRepositoryImpl implements AddressRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired @Lazy
    public AddressRepository addressRepository;

    @Override
    public Page<Address> find(String street, String number, String district, String zipCode, String state, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<Address> criteria = builder.createQuery(Address.class);
        Root<Address> root = criteria.from(Address.class);

        var predicates = new ArrayList<Predicate>();

        if (StringUtils.hasText(street)){
            predicates.add(builder.like(root.get("street"), "%" + street + "%"));
        }

        if (StringUtils.hasText(number)){
            predicates.add(builder.like(root.get("number"), "%" + number + "%"));
        }

        if (StringUtils.hasText(district)){
            predicates.add(builder.like(root.get("district"), "%" + district + "%"));
        }

        if (StringUtils.hasText(zipCode)){
            predicates.add(builder.like(root.get("zipCode"), "%" + zipCode + "%"));
        }

        if (StringUtils.hasText(state)){
            predicates.add(builder.like(root.get("state"), "%" + state + "%"));
        }

        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Address> query = manager.createQuery(criteria);

        List<Address> pageList = query.getResultList().stream()
                .skip(pageable.getPageSize() * pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        return new PageImpl<>(pageList, pageable, pageList.size());
    }
}