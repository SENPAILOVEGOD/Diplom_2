package helpers;

import java.util.UUID;

public final class TestDataGenerator {

    private TestDataGenerator() {}

    public static String uniqueEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@yandex.ru";
    }
}