package com.app.controller.dto.order;

import com.app.controller.dto.ClientDto;

import java.math.BigDecimal;

/**
 * A DTO that represents a client and their associated debit amount.
 * <p>
 * This class contains the client information and the corresponding debit amount that the client owes.
 * </p>
 */
public record ClientAndDebitDto(
        /**
         * The client information, encapsulated in a {@link ClientDto} object.
         */
        ClientDto clientDto,

        /**
         * The debit amount that the client owes, represented as a {@link BigDecimal}.
         */
        BigDecimal debitAmount) {
}
