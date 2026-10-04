package ru.yandex.scooter.client;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.scooter.config.ApiConfig;
import ru.yandex.scooter.model.Courier;
import ru.yandex.scooter.model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создать курьера")
    public Response create(Courier courier) {
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(ApiConfig.COURIERS_PATH);
    }

    @Step("Авторизоваться под курьером")
    public Response login(CourierCredentials credentials) {
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post(ApiConfig.COURIER_LOGIN_PATH);
    }

    @Step("Удалить курьера с id: {courierId}")
    public Response delete(int courierId) {
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .when()
                .delete(ApiConfig.COURIERS_PATH + "/" + courierId);
    }
}