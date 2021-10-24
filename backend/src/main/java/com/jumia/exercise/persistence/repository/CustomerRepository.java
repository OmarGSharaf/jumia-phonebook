package com.jumia.exercise.persistence.repository;

import com.jumia.exercise.persistence.Filter;
import com.jumia.exercise.persistence.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Repository
public class CustomerRepository extends DynamicRepository<Customer> {

    private final SimpleJpaRepository<Customer, String> jpaRepository;

    public CustomerRepository(EntityManager entityManager) {
        jpaRepository = new SimpleJpaRepository<>(Customer.class, entityManager);
    }

    @Override
    public List<Customer> getQueryResult() {
        return jpaRepository.findAll();
    }

    @Override
    public Page<Customer> getQueryResult(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> getQueryResult(Filter filters, Pageable pageable) {
        return filters != null
                ? jpaRepository.findAll(getSpecificationFromFilters(Collections.singletonList(filters)), pageable)
                : jpaRepository.findAll(pageable);
    }

    @Override
    public Page<Customer> getQueryResult(List<Filter> filters, Pageable pageable) {
        return filters.size() > 0
                ? jpaRepository.findAll(getSpecificationFromFilters(filters), pageable)
                : jpaRepository.findAll(pageable);
    }

    @Transactional
    public SimpleJpaRepository<Customer, String> getJpaOperations() {
        return jpaRepository;
    }
}