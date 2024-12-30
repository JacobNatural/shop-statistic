package com.app.converter.impl.single;

import com.app.converter.single.Converter;
import com.app.converter.single.impl.ClientConverterImpl;
import com.app.model.Client;
import com.app.persistence.entity.ClientEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.app.data.ClientData.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ClientConverterImpl.class)
public class ClientConverterImplTest {

    @Autowired
    private Converter<ClientEntity, Client> converter;

    @Test
    @DisplayName("When convert client entity to client.")
    public void test1(){
        Assertions.assertThat(converter.toModel(CLIENT_ENTITY_READ_1))
                .isEqualTo(CLIENT1);
    }

    @Test
    @DisplayName("When convert client to entity client.")
    public void test2(){
        Assertions.assertThat(converter.toEntity(CLIENT1))
                .isEqualTo(CLIENT_ENTITY_READ_1);

    }
}

