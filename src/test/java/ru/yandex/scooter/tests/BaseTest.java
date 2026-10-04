package ru.yandex.scooter.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import ru.yandex.scooter.client.CourierClient;
import ru.yandex.scooter.data.TestData;
import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.CourierCredentials;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseTest {

    protected final CourierClient courierClient = new CourierClient();

    private final List<Integer> createdCourierIds = new ArrayList<>();

    /**
     * Создаёт уникального курьера, проверяет успешное создание
     * и сохраняет его id для удаления после теста.
     */
    protected Courier createCourierAndRemember() {
        Courier courier = TestData.createCourier();

        Response createResponse = courierClient.create(courier);

        assertEquals(
                201,
                createResponse.statusCode(),
                "Не удалось подготовить тестового курьера: "
                        + createResponse.asString()
        );

        rememberCourier(courier);

        return courier;
    }

    /**
     * Авторизуется под уже созданным курьером,
     * получает id из ответа и добавляет id в список на удаление.
     */
    protected void rememberCourier(Courier courier) {
        CourierCredentials credentials =
                CourierCredentials.from(courier);

        Response loginResponse = courierClient.login(credentials);

        assertEquals(
                200,
                loginResponse.statusCode(),
                "Не удалось авторизоваться под тестовым курьером: "
                        + loginResponse.asString()
        );

        Integer courierId = loginResponse.jsonPath().getInt("id");

        createdCourierIds.add(courierId);
    }

    /**
     * После каждого теста удаляет всех курьеров,
     * созданных и сохранённых в текущем тесте.
     */
    @AfterEach
    void deleteCreatedCouriers() {
        for (Integer courierId : createdCourierIds) {
            courierClient.delete(courierId);
        }
    }
}
