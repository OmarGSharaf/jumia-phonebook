package com.jumia.exercise.repository;

import com.jumia.exercise.persistence.Filter;
import com.jumia.exercise.persistence.QueryOperator;
import com.jumia.exercise.persistence.model.Customer;
import com.jumia.exercise.persistence.repository.CustomerRepository;
import com.jumia.exercise.type.CountryName;
import com.jumia.exercise.utils.PhoneUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.getJpaOperations().save(Customer.builder().phone("123").name("valid").build());
        customerRepository.getJpaOperations().save(Customer.builder().phone("123").name("valid").build());
        customerRepository.getJpaOperations().save(Customer.builder().phone("123").name("valid").build());
        customerRepository.getJpaOperations().save(Customer.builder().phone("123").name("invalid").build());
        customerRepository.getJpaOperations().save(Customer.builder().phone("123").name("valid").build());
        customerRepository.getJpaOperations().save(Customer.builder().phone("123").name("invalid").build());
    }

    @AfterEach
    public void onTearDown() {
        customerRepository.getJpaOperations().deleteAll();
    }

    @Test
    public void givenSixCustomers_whenFindAllUnpagedUnfiltered_thenReturnSixCustomers() {
        List<Customer> customers = customerRepository.getQueryResult();
        Assertions.assertEquals(6, customers.size());
    }

    @Test
    public void givenSixCustomers_whenFindAllSecondPagedUnfiltered_thenReturnPageOfOneCustomer() {
        Page<Customer> page = customerRepository.getQueryResult(PageRequest.of(1, 5));

        Assertions.assertEquals(6, page.getTotalElements());
        Assertions.assertEquals(2, page.getTotalPages());
        Assertions.assertEquals(1, page.getNumberOfElements());
    }

    @Test
    public void givenSixCustomers_whenFindCustomersByValidPhoneNumbersUnpaged_thenReturnPageOfThreeCustomers() {
        Filter filter = Filter.builder()
                .field("phone")
                .values(PhoneUtils.getRegex())
                .operator(QueryOperator.REGEX_IN)
                .build();

        Page<Customer> page = customerRepository.getQueryResult(filter, Pageable.unpaged());

        Assertions.assertEquals(2, page.getTotalElements());
        page.getContent().forEach(customer -> Assertions.assertEquals("valid", customer.getName()));
    }

    @Test
    public void givenSixCustomers_whenFindCustomersByCountryUnpaged_thenReturnPageOfFourCustomers() {
        Filter filter = Filter.builder()
                .field("phone")
                .value(String.format("(%s)", PhoneUtils.getCode(CountryName.MOROCCO)))
                .operator(QueryOperator.STARTS_WITH)
                .build();

        Page<Customer> page = customerRepository.getQueryResult(filter, Pageable.unpaged());

        Assertions.assertEquals(4, page.getTotalElements());
        page.getContent().forEach(customer -> Assertions.assertEquals(Boolean.TRUE, customer.getPhone().contains("(212)")));
    }

    @Test
    public void givenSixCustomers_whenFindCustomersByCountryAndValidPhoneNumberUnpaged_thenReturnPageOfThreeCustomers() {
        Filter filter = Filter.builder()
                .field("phone")
                .value(String.format("(%s)", PhoneUtils.getCode(CountryName.MOROCCO)))
                .operator(QueryOperator.STARTS_WITH)
                .build();

        Page<Customer> page = customerRepository.getQueryResult(filter, Pageable.unpaged());

        Assertions.assertEquals(3, page.getTotalElements());
        page.getContent().forEach(customer -> Assertions.assertEquals("valid", customer.getName()));
        page.getContent().forEach(customer -> Assertions.assertEquals(Boolean.TRUE, customer.getPhone().contains("(212)")));
    }
}
