package com.app.validator.impl;

import com.app.controller.dto.ClientDto;
import com.app.validator.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ClientDtoValidator.class)
public class ClientDtoValidatorTest {
    @Autowired
    ClientDtoValidator clientDtoValidator;

    @Value("${validate.client.name.min.length}")
    private int minNameLength;

    @Value("${validate.client.surname.min.length}")
    private int minSurnameLength;

    @Value("${validate.client.min.age}")
    private int minAge;

    @Test
    @DisplayName("When the client's name is null, throw an IllegalArgumentException")
    public void test1(){
        var clientDto = new ClientDto(1L, null, "AA", 20, BigDecimal.valueOf(500));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client name cannot be null");
    }

    @Test
    @DisplayName("When the client's name is too short, throw an IllegalArgumentException")
    public void test2(){
        var clientDto = new ClientDto(1L, "A", "AA", 20, BigDecimal.valueOf(500));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client name must have at least " + minNameLength + " characters");
    }

    @Test
    @DisplayName("When the client's name contain invalid characters, throw an IllegalArgumentException")
    public void test3(){
        var clientDto = new ClientDto(1L, "Alonek!", "AA", 20, BigDecimal.valueOf(500));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client name contains invalid characters");
    }

    @Test
    @DisplayName("When the client's surname is null, throw an IllegalArgumentException")
    public void test4(){
        var clientDto = new ClientDto(1L, "Bartek", null, 20, BigDecimal.valueOf(500));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client surname cannot be null");
    }

    @Test
    @DisplayName("When the client's surname is too short, throw an IllegalArgumentException")
    public void test5(){
        var clientDto = new ClientDto(1L, "Bartek", "AA", 20, BigDecimal.valueOf(500));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client surname must have at least " + minSurnameLength
                        + " characters");
    }

    @Test
    @DisplayName("When the client's surname contains illegal characters, throw an IllegalArgumentException")
    public void test6(){
        var clientDto = new ClientDto(1L, "Bartek", "Alek!", 20, BigDecimal.valueOf(500));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client surname contains invalid characters");
    }

    @Test
    @DisplayName("When the client's age is too low, throw an IllegalArgumentException")
    public void test7(){
        var clientDto = new ClientDto(1L, "Bartek", "Aldonek", 8, BigDecimal.valueOf(500));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client is too young, min age is: " + minAge);
    }

    @Test
    @DisplayName("When the client's balance is invalid, throw an IllegalArgumentException")
    public void test8(){
        var clientDto = new ClientDto(1L, "Bartek", "Aldonek", 18, BigDecimal.valueOf(0));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cash is too low");
    }

    @Test
    @DisplayName("When the client's balance is too low, throw an IllegalArgumentException.")
    public void test9(){
        var clientDto = new ClientDto(1L, "Bartek", "Aldonek", 18, BigDecimal.valueOf(0));

        Assertions.assertThatThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cash is too low");
    }

    @Test
    @DisplayName("When the client's data are correct, do not throw any exception.")
    public void test10(){
        var clientDto = new ClientDto(1L, "Bartek", "Aldonek", 18, BigDecimal.valueOf(5));

        Assertions.assertThatNoException().isThrownBy(()
                        -> Validator.validate(clientDto, clientDtoValidator));
    }

}
