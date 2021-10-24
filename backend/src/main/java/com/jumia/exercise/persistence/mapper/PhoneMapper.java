package com.jumia.exercise.persistence.mapper;

import com.jumia.exercise.persistence.dto.PhoneDto;
import com.jumia.exercise.persistence.model.Customer;
import com.jumia.exercise.type.CountryName;
import com.jumia.exercise.type.State;
import com.jumia.exercise.utils.PhoneUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper implements Mapper<PhoneDto, String> {

    @Override
    public PhoneDto from(String s) {
        String countryCode = getCountryCode(s);
        String number = getNumber(s);
        CountryName country = PhoneUtils.getCountry(countryCode);
        boolean isValid = PhoneUtils.validate(country, s);

        return PhoneDto.builder()
                .countryCode(countryCode)
                .number(number)
                .country(country)
                .state(isValid ? State.VALID : State.INVALID)
                .build();
    }

    public Page<PhoneDto> from(Page<Customer> customers) {
        return customers.map(this::from);
    }

    public PhoneDto from(Customer customer) {
        return from(customer.getPhone());
    }

    @Override
    public String to(PhoneDto phoneDto) {
        return String.format("(%s) %s", phoneDto.getCountryCode(), phoneDto.getNumber());
    }

    private String getCountryCode(String s) {
        return s.substring(s.indexOf("(") + 1, s.indexOf(")"));
    }

    private String getNumber(String s) {
        String[] tokens = s.trim().split("\\s+");
        return tokens.length > 1 ? tokens[1] : s;
    }
}
