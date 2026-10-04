package ru.yandex.scooter.tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.scooter.client.OrderClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Epic("Заказы")
@Feature("Список заказов")
class OrderListTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    @Story("Получение списка заказов")
    @DisplayName("В ответе возвращается список заказов")
    void shouldReturnOrderList() {
        Response response = orderClient.getList();

        List<?> orders = response.jsonPath().getList("orders");

        assertAll(
                () -> assertEquals(
                        200,
                        response.statusCode(),
                        response.asString()
                ),
                () -> assertNotNull(
                        orders,
                        "В ответе отсутствует поле orders: "
                                + response.asString()
                )
        );
    }
}
