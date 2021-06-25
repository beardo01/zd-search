package com.oreid.model;

import com.google.gson.JsonObject;
import com.oreid.domain.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.oreid.TestFixtures.createJsonObject;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private static final JsonObject JSON_OBJECT = createJsonObject();
    private static final String EXPECTED_RELATED_DESCRIPTION_KEY = "name";
    private static final EntityType EXPECTED_ENTITY_TYPE = EntityType.USER;

    private User testee;

    @BeforeEach
    void setUp() {
        testee = new User(JSON_OBJECT);
    }

    @Test
    void test_constructor_success() {
        assertThat(testee.getData(), is(JSON_OBJECT));
        assertThat(testee.getEntityType(), is(EXPECTED_ENTITY_TYPE));
        assertThat(testee.getRelatedEntities(), is(Collections.emptyList()));
    }

    @Test
    void test_getRelatedDescriptionKey_success() {
        assertThat(testee.getRelatedDescriptionKey(), is(EXPECTED_RELATED_DESCRIPTION_KEY));
    }

    @Test
    void testee_addRelatedEntity_single_success() {
        assertThat(testee.getRelatedEntities(), is(Collections.emptyList()));

        final var relatedEntity = new User(JSON_OBJECT);
        testee.addRelatedEntity(relatedEntity);

        assertThat(testee.getRelatedEntities(), hasSize(1));
        assertThat(testee.getRelatedEntities(), contains(relatedEntity));
    }

    @Test
    void testee_addRelatedEntity_multiple_success() {
        assertThat(testee.getRelatedEntities(), is(Collections.emptyList()));

        final var relatedEntity1 = new User(JSON_OBJECT);
        final var relatedEntity2 = new User(JSON_OBJECT);
        testee.addRelatedEntity(relatedEntity1);
        testee.addRelatedEntity(relatedEntity2);

        assertThat(testee.getRelatedEntities(), hasSize(2));
        assertThat(testee.getRelatedEntities(), contains(relatedEntity1, relatedEntity2));
    }

}