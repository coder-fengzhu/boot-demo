package com.tutorial.bootdemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum Gender {
    BOY("boy", "1"), GIRL("girl", "2");

    private String detail;
    private String dbValue;

    public static Optional<Gender> getGenderByValue(String dbValue) {
        return Arrays.stream(Gender.values()).filter(g -> g.dbValue.equals(dbValue)).findFirst();
    }

}
