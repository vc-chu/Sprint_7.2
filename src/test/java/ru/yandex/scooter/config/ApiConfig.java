package ru.yandex.scooter.config;

public final class ApiConfig {

    public static final String BASE_URL = System.getProperty(
            "base.url",
            "https://qa-scooter.education-services.ru"
    );

    public static final String COURIERS_PATH = "/api/v1/courier";
    public static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";
    public static final String ORDERS_PATH = "/api/v1/orders";

    private ApiConfig() {
    }
}
