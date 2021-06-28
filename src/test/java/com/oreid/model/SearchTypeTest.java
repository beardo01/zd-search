package com.oreid.model;

import com.oreid.domain.EntityType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.oreid.TestFixtures.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

@ExtendWith(MockitoExtension.class)
class SearchTypeTest {

    private static final EntityType ENTITY_TYPE = EntityType.ORGANIZATION;
    private static final AbstractEntity ENTITY = createAbstractEntity(ENTITY_TYPE, JSON_STRING);
    private static final String VALID_FIELD = ENTITY_KEYS.get(0);
    private static final String INVALID_FIELD = "InvalidField";

    private SearchType testee;

    @BeforeEach
    void setUp() {
        testee = new SearchType(ENTITY);
    }

    @Test
    void test_constructor_success() {
        assertThat(testee.getName(), is(ENTITY_TYPE.toString()));
        assertThat(testee.getFields(), hasSize(4));
        assertThat(testee.getFields(), is(ENTITY_KEYS));
    }

    @Test
    void test_isValidField_valid() {
        final var result = testee.isValidField(VALID_FIELD);

        assertThat(result, is(true));
    }

    @Test
    void test_isValidField_invalid() {
        final var result = testee.isValidField(INVALID_FIELD);

        assertThat(result, is(false));
    }
}