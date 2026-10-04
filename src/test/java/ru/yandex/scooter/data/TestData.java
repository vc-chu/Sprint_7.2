package ru.yandex.scooter.data;

import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.CourierCredentials;

import java.util.UUID;

public final class TestData {

    private TestData() {
        // Утилитарный класс: экземпляры не создаются
    }

    public static Courier createCourier() {
        String uniqueId = UUID.randomUUID()
                .toString()
                .replace("-", "");

        String login = "api_test_" + uniqueId;
        String password = "Pwd" + uniqueId.substring(0, 12);
        String firstName = "API test";

        return new Courier(login, password, firstName);
    }

    public static CourierCredentials createNonExistingCourierCredentials() {
        String uniqueId = UUID.randomUUID()
                .toString()
                .replace("-", "");

        String login = "non_existing_" + uniqueId;
        String password = "Pwd" + uniqueId.substring(0, 12);

        return new CourierCredentials(login, password);
    }

    public static CourierCredentials createCredentials(Courier courier) {
        return new CourierCredentials(
                courier.getLogin(),
                courier.getPassword()
        );
    }
}