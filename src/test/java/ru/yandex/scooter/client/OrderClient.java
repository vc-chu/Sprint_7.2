package ru.yandex.scooter.client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.scooter.config.ApiConfig;
import ru.yandex.scooter.model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создать заказ")
    public Response create(Order order) {
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .body(order.toRequestBody())
                .when()
                .post(ApiConfig.ORDERS_PATH);
    }

    @Step("Получить список заказов")
    public Response getList() {
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get(ApiConfig.ORDERS_PATH);
    }
}
