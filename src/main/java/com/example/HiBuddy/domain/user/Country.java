package com.example.HiBuddy.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Country {
    AFGHANISTAN,
    AZERBAIJAN,
    AUSTRALIA,
    AUSTRIA,
    BANGLADESH,
    BELGIUM,
    BRAZIL,
    BRUNEI,
    MYANMAR,
    BELARUS,
    CAMBODIA,
    CANADA,
    CHAD,
    CHINA,
    TAIWAN,
    CZECH,
    DENMARK,
    EQUATORIAL_GUINEA,
    FINLAND,
    FRANCE,
    PALESTINE,
    GERMANY,
    HONG_KONG,
    HUNGARY,
    INDIA,
    INDONESIA,
    IRAN,
    ITALY,
    JAPAN,
    KAZAKHSTAN,
    JORDAN,
    KENYA,
    KYRGYZSTAN,
    LIBYA,
    MALAYSIA,
    MEXICO,
    MONGOLIA,
    MOROCCO,
    NEPAL,
    NETHERLANDS,
    PHILIPPINES,
    POLAND,
    PORTUGAL,
    RUSSIA,
    SAUDI_ARABIA,
    SINGAPORE,
    VIETNAM,
    SPAIN,
    SUDAN,
    SWEDEN,
    TAJIKISTAN,
    THAILAND,
    UAE, // United Arab Emirates
    TURKEY,
    TURKMENISTAN,
    UKRAINE,
    EGYPT,
    UK,
    USA,
    UZBEKISTAN;

    @JsonCreator
    public static Country fromValue(String value) {
        for (Country country : values()) {
            if (country.name().equalsIgnoreCase(value)) {
                return country;
            }
        }
        throw new IllegalArgumentException("Invalid country: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
