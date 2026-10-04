package ru.yandex.scooter.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Order {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final int metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    public Order(List<String> color) {
        this.firstName = "Иван";
        this.lastName = "Иванов";
        this.address = "Москва, Красная площадь, 1";
        this.metroStation = 4;
        this.phone = "+79990000000";
        this.rentTime = 3;
        this.deliveryDate = "2026-10-10";
        this.comment = "Автотест создания заказа";
        this.color = color;
    }

    public Order(
            String firstName,
            String lastName,
            String address,
            int metroStation,
            String phone,
            int rentTime,
            String deliveryDate,
            String comment,
            List<String> color
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public Map<String, Object> toRequestBody() {
        Map<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("firstName", firstName);
        requestBody.put("lastName", lastName);
        requestBody.put("address", address);
        requestBody.put("metroStation", metroStation);
        requestBody.put("phone", phone);
        requestBody.put("rentTime", rentTime);
        requestBody.put("deliveryDate", deliveryDate);
        requestBody.put("comment", comment);

        // Если цвет не указан, поле color не попадёт в JSON-запрос.
        if (color != null) {
            requestBody.put("color", color);
        }

        return requestBody;
    }

    public List<String> getColor() {
        return color;
    }
}