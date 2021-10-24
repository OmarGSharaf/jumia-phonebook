package com.jumia.exercise.utils;

import com.jumia.exercise.type.CountryName;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.jumia.exercise.type.CountryName.*;

public class PhoneUtils {

    public static boolean validate(CountryName country, String s) {
        switch (country) {
            case CAMEROON:
                return Pattern.compile("\\(237\\) ?[2368]\\d{7,8}$").matcher(s).matches();
            case ETHIOPIA:
                return Pattern.compile("\\(251\\) ?[1-59]\\d{8}$").matcher(s).matches();
            case MOROCCO:
                return Pattern.compile("\\(212\\) ?[5-9]\\d{8}$").matcher(s).matches();
            case MOZAMBIQUE:
                return Pattern.compile("\\(258\\) ?[28]\\d{7,8}$").matcher(s).matches();
            case UGANDA:
                return Pattern.compile("\\(25P6\\) ?\\d{9}$").matcher(s).matches();
            default:
                return Boolean.FALSE;
        }
    }

    public static CountryName getCountry(String countryCode) {
        switch (countryCode) {
            case "237":
                return CAMEROON;
            case "251":
                return ETHIOPIA;
            case "212":
                return MOROCCO;
            case "258":
                return MOZAMBIQUE;
            case "256":
                return UGANDA;
            default:
                return UNKNOWN;
        }
    }

    public static String getCode(CountryName country) {
        switch (country) {
            case CAMEROON:
                return "237";
            case ETHIOPIA:
                return "251";
            case MOROCCO:
                return "212";
            case MOZAMBIQUE:
                return "258";
            case UGANDA:
                return "256";
            default:
                return "-1";
        }
    }

    public static List<String> getRegex() {
        return Arrays.asList(
                "\\(237\\)\\ ?[2368]\\d{7,8}$",
                "\\(251\\)\\ ?[1-59]\\d{8}$",
                "\\(212\\)\\ ?[5-9]\\d{8}$",
                "\\(258\\)\\ ?[28]\\d{7,8}$",
                "\\(256\\)\\ ?\\d{9}$");
    }
}
