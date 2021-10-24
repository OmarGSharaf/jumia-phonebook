package com.jumia.exercise.persistence.dto;

import com.jumia.exercise.type.CountryName;
import com.jumia.exercise.type.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneDto {

    String countryCode;

    String number;

    CountryName country;

    State state;

}