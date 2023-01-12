package com.example.orderservice.order.web;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRequestValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldValidationSucceedsWhenAllFieldsCorrect() {
        var orderRequest = new OrderRequest("1234567890", 1);
        var violations = validator.validate(orderRequest);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldValidationFailsWhenIsbnNotDefined() {
        var orderRequest = new OrderRequest("", 1);
        var violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book ISBN must be defined.");
    }

    @Test
    void shouldValidationFailsWhenQuantityIsNotDefined() {
        var orderRequest = new OrderRequest("1234567890", null);
        var violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book quantity must be defined.");
    }

    @Test
    void shouldValidationFailsWhenQuantityIsLowerThanMin() {
        var orderRequest = new OrderRequest("1234567890", 0);
        var violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("You must order at least 1 item.");
    }

    @Test
    void shouldValidationFailsWhenQuantityIsGreaterThanMax() {
        var orderRequest = new OrderRequest("1234567890", 7);
        var violations = validator.validate(orderRequest);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("You cannot order more than 5 items.");
    }

}
