package com.jumia.exercise.service;

import com.jumia.exercise.persistence.Filter;
import com.jumia.exercise.persistence.QueryOperator;
import com.jumia.exercise.persistence.dto.PhoneDto;
import com.jumia.exercise.persistence.mapper.PhoneMapper;
import com.jumia.exercise.persistence.repository.CustomerRepository;
import com.jumia.exercise.type.CountryName;
import com.jumia.exercise.type.State;
import com.jumia.exercise.utils.PhoneUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PhoneService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PhoneMapper phoneMapper;

    public Page<PhoneDto> find(Pageable pageable) {
        return find(null, null, pageable);
    }

    public Page<PhoneDto> find(CountryName country, State state, Pageable pageable) {
        List<Filter> filters = new ArrayList<>();

        if (country != null) {
            filters.add(Filter.builder()
                    .field("phone")
                    .value(String.format("(%s)", PhoneUtils.getCode(country)))
                    .operator(QueryOperator.STARTS_WITH)
                    .build());
        }

        if (state != null) {
            filters.add(Filter.builder()
                    .field("phone")
                    .values(PhoneUtils.getRegex())
                    .operator(state.equals(State.VALID) ? QueryOperator.REGEX_IN : QueryOperator.NOT_REGEX_IN)
                    .build());
        }

        return phoneMapper.from(customerRepository.getQueryResult(filters, pageable));
    }
}
