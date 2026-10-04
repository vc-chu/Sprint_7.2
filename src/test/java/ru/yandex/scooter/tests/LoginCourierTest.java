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
import ru.yandex.scooter.model.CourierCredentials;
import ru.yandex.scooter.model.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Курьеры")
@Feature("Логин курьера")
class LoginCourierTest extends BaseTest {

    @Test
    @Story("Успешная авторизация")
    @DisplayName("Курьер может авторизоваться и получить id")
    void shouldLoginCourier() {
        Courier courier = createCourierAndRemember();

        CourierCredentials credentials =
                CourierCredentials.from(courier);

        Response response = courierClient.login(credentials);

        assertAll(
                () -> assertEquals(
                        200,
                        response.statusCode(),
                        response.asString()
                ),
                () -> assertNotNull(
                        response.jsonPath().get("id"),
                        "В ответе отсутствует поле id: "
                                + response.asString()
                )
        );
    }

    @ParameterizedTest(
            name = "Без обязательного поля {0} логин возвращает ошибку"
    )
    @ValueSource(strings = {"login", "password"})
    @Story("Обязательные поля для авторизации")
    @DisplayName("Без login или password нельзя авторизоваться")
    void shouldNotLoginWithoutRequiredField(String fieldName) {
        CourierCredentials credentials =
                TestData.createNonExistingCourierCredentials();

        if ("login".equals(fieldName)) {
            credentials.setLogin(null);
        } else {
            credentials.setPassword(null);
        }

        Response response = courierClient.login(credentials);

        assertEquals(
                400,
                response.statusCode(),
                "Неожиданный статус. Content-Type: "
                        + response.contentType()
                        + ". Тело ответа: "
                        + response.asString()
        );

        assertTrue(
                response.contentType().contains("application/json"),
                "Сервер вернул не JSON. Content-Type: "
                        + response.contentType()
                        + ". Тело ответа: "
                        + response.asString()
        );

        ErrorResponse errorResponse =
                response.as(ErrorResponse.class);

        assertAll(
                () -> assertEquals(
                        400,
                        errorResponse.getCode()
                ),
                () -> assertEquals(
                        "Недостаточно данных для входа",
                        errorResponse.getMessage()
                )
        );
    }

    @ParameterizedTest(
            name = "Неверный {0} возвращает ошибку"
    )
    @ValueSource(strings = {"login", "password"})
    @Story("Неверные данные авторизации")
    @DisplayName("Неверный логин или пароль возвращает ошибку")
    void shouldNotLoginWithWrongCredentials(String incorrectField) {
        Courier courier = createCourierAndRemember();

        CourierCredentials credentials =
                CourierCredentials.from(courier);

        if ("login".equals(incorrectField)) {
            credentials.setLogin(
                    "wrong_login_" + courier.getLogin()
            );
        } else {
            credentials.setPassword(
                    "wrong_password_" + courier.getPassword()
            );
        }

        Response response = courierClient.login(credentials);

        assertJsonErrorResponse(
                response,
                404,
                "Учетная запись не найдена"
        );
    }

    @Test
    @Story("Авторизация несуществующего курьера")
    @DisplayName("Несуществующий курьер не может авторизоваться")
    void shouldNotLoginNonExistingCourier() {
        CourierCredentials credentials =
                TestData.createNonExistingCourierCredentials();

        Response response = courierClient.login(credentials);

        assertJsonErrorResponse(
                response,
                404,
                "Учетная запись не найдена"
        );
    }

    private void assertJsonErrorResponse(
            Response response,
            int expectedStatusCode,
            String expectedMessage
    ) {
        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "Неожиданный статус. Content-Type: "
                        + response.contentType()
                        + ". Тело ответа: "
                        + response.asString()
        );

        assertTrue(
                response.contentType().contains("application/json"),
                "Сервер вернул не JSON. Content-Type: "
                        + response.contentType()
                        + ". Тело ответа: "
                        + response.asString()
        );

        ErrorResponse errorResponse =
                response.as(ErrorResponse.class);

        assertAll(
                () -> assertEquals(
                        expectedStatusCode,
                        errorResponse.getCode()
                ),
                () -> assertEquals(
                        expectedMessage,
                        errorResponse.getMessage()
                )
        );
    }
}
