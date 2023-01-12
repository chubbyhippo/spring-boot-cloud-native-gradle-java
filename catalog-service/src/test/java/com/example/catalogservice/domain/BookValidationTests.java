package com.example.catalogservice.domain;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class BookValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void shouldNotShowErrorsWhenAllFieldsCorrect() {
        var book = Book.of("1234567890", "Title", "Author", 9.90, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailsWhenIsbnNotDefined() {
        var book = Book.of("", "Title", "Author", 9.90, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        var constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage).collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The book ISBN must be defined.")
                .contains("The ISBN format must be valid.");
    }

    @Test
    void shouldFailsWhenIsbnDefinedButIncorrect() {
        var book = Book.of("a234567890", "Title", "Author", 9.90, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    void shouldFailsWhenTitleIsNotDefined() {
        var book = Book.of("1234567890", "", "Author", 9.90, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book title must be defined.");
    }

    @Test
    void shouldFailsWhenAuthorIsNotDefined() {
        var book = Book.of("1234567890", "Title", "", 9.90, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book author must be defined.");
    }

    @Test
    void shouldFailsWhenPriceIsNotDefined() {
        var book = Book.of("1234567890", "Title", "Author", null, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be defined.");
    }

    @Test
    void shouldFailsWhenPriceDefinedButZero() {
        var book = Book.of("1234567890", "Title", "Author", 0.0, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

    @Test
    void shouldFailsWhenPriceDefinedButNegative() {
        var book = Book.of("1234567890", "Title", "Author", -9.90, "Chubby Hippo");
        var violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

}
