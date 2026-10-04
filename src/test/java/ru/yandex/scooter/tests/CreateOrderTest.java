package ru.yandex.scooter.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.scooter.client.OrderClient;
import ru.yandex.scooter.model.Order;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Заказы")
@Feature("Создание заказа")
class CreateOrderTest {

    private final OrderClient orderClient = new OrderClient();

    static Stream<Arguments> orderColors() {
        return Stream.of(
                Arguments.of("Один цвет BLACK", List.of("BLACK")),
                Arguments.of("Один цвет GREY", List.of("GREY")),
                Arguments.of(
                        "Два цвета BLACK и GREY",
                        List.of("BLACK", "GREY")
                ),
                Arguments.of("Цвет не указан", null)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("orderColors")
    @Story("Выбор цвета самоката")
    @DisplayName("Заказ создаётся с разными вариантами цвета")
    void shouldCreateOrderWithDifferentColors(
            String scenarioName,
            List<String> colors
    ) {
        Order order = new Order(colors);

        Response response = orderClient.create(order);

        assertAll(
                () -> assertEquals(
                        201,
                        response.statusCode(),
                        response.asString()
                ),
                () -> assertNotNull(
                        response.jsonPath().get("track"),
                        "В ответе отсутствует track: "
                                + response.asString()
                )
        );
    }
}
