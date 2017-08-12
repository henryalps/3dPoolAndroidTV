package com.intel.tvpresent.test.common;

import java.util.UUID;

public class TestDataFactory {

    public static String generateRandomString() {
        return UUID.randomUUID().toString().substring(0, 5);
    }

}
