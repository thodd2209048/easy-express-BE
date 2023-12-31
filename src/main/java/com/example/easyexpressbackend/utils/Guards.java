package com.example.easyexpressbackend.utils;

public class Guards {
    public static void mustPositive(String argumentName, int value) {
        if (value <= 0)
            throw new IllegalArgumentException(argumentName + " should be a positive number");
    }
}
