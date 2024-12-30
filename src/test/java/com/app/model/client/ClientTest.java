package com.app.model.client;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.app.data.ClientData.*;

public class ClientTest {

    @Test
    @DisplayName("When converting a client to a Client entity.")
    public void test1() {
        Assertions.assertThat(CLIENT1.toClientEntity())
                .isEqualTo(CLIENT_ENTITY_READ_1);
    }
}
