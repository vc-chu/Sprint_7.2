package ru.yandex.scooter.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.yandex.scooter.data.TestData;
import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Курьеры")
@Feature("Создание курьера")
class CreateCourierTest extends BaseTest {

    @Test
    @Story("Успешное создание")
    @DisplayName("Можно создать нового курьера")
    void shouldCreateCourier() {
        Courier courier = TestData.createCourier();

        Response response = courierClient.create(courier);

        assertAll(
                () -> assertEquals(
                        201,
                        response.statusCode(),
                        response.asString()
                ),
                () -> assertTrue(
                        response.jsonPath().getBoolean("ok"),
                        response.asString()
                )
        );

        // Сохраняем курьера для удаления в @AfterEach.
        rememberCourier(courier);
    }

    @Test
    @Story("Создание дубликата")
    @DisplayName("Нельзя создать двух курьеров с одинаковым логином")
    void shouldNotCreateDuplicateCourier() {
        Courier courier = TestData.createCourier();

        Response firstResponse = courierClient.create(courier);

        assertEquals(
                201,
                firstResponse.statusCode(),
                firstResponse.asString()
        );

        // Сохраняем созданного курьера для очистки данных после теста.
        rememberCourier(courier);

        Response secondResponse = courierClient.create(courier);

        ErrorResponse errorResponse =
                secondResponse.as(ErrorResponse.class);

        assertAll(
                () -> assertEquals(
                        409,
                        secondResponse.statusCode(),
                        secondResponse.asString()
                ),
                () -> assertEquals(
                        409,
                        errorResponse.getCode()
                ),
                () -> assertEquals(
                        "Этот логин уже используется. Попробуйте другой.",
                        errorResponse.getMessage()
                )
        );
    }

    @ParameterizedTest(
            name = "Без обязательного поля {0} курьер не создаётся"
    )
    @ValueSource(strings = {"login", "password"})
    @Story("Проверка обязательных полей")
    @DisplayName("Без login или password возвращается ошибка")
    void shouldNotCreateCourierWithoutRequiredField(String fieldName) {
        Courier courier = TestData.createCourier();

        if ("login".equals(fieldName)) {
            courier.setLogin(null);
        } else {
            courier.setPassword(null);
        }

        Response response = courierClient.create(courier);

        ErrorResponse errorResponse =
                response.as(ErrorResponse.class);

        assertAll(
                () -> assertEquals(
                        400,
                        response.statusCode(),
                        response.asString()
                ),
                () -> assertEquals(
                        400,
                        errorResponse.getCode()
                ),
                () -> assertEquals(
                        "Недостаточно данных для создания учетной записи",
                        errorResponse.getMessage()
                )
        );
    }
}
