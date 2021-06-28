package com.oreid.repository;

import com.oreid.domain.EntityType;
import com.oreid.model.AbstractEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.oreid.TestFixtures.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

@ExtendWith(MockitoExtension.class)
class DatastoreTest {

    private static final EntityType ENTITY_TYPE_1 = EntityType.ORGANIZATION;
    private static final EntityType ENTITY_TYPE_2 = EntityType.TICKET;
    private static final AbstractEntity ENTITY_1 = createAbstractEntity(ENTITY_TYPE_1, JSON_STRING);
    private static final AbstractEntity ENTITY_2 = createAbstractEntity(ENTITY_TYPE_1, JSON_STRING_2);
    private static final AbstractEntity ENTITY_3 = createAbstractEntity(ENTITY_TYPE_2, JSON_STRING);
    private static final AbstractEntity ENTITY_4 = createAbstractEntity(ENTITY_TYPE_1, JSON_STRING_BLANK_VALUE);
    private static final List<Object> ENTITY_VALUES_1 = List.of("Oliver", "true", "New Zealand", "Australia", "24");
    private static final List<Object> ENTITY_VALUES_2 = List.of("John", "false", "New Zealand", "USA", "24");
    private static final String SEARCH_KEY = "name";
    private static final String SEARCH_VALUE = "Oliver";
    private static final String MULTIPLE_SEARCH_KEY = "age";
    private static final String MULTIPLE_SEARCH_VALUE = "24";
    private static final String BLANK_VALUE = "";

    @InjectMocks
    private Datastore testee;

    @Test
    void test_constructor_success() {
        assertThat(testee.getData(), is(anEmptyMap()));
    }

    @Test
    void test_addEntity_single_success() {
        testee.addEntity(ENTITY_1);

        final var data = testee.getData();

        // Entity Type
        assertThat(data, hasKey(ENTITY_TYPE_1));

        // Keys
        final var entityTypeMap = data.get(ENTITY_TYPE_1);
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(0))); // name
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(1))); // person
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(2))); // countries
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(3))); // age
        assertThat(entityTypeMap.keySet(), hasSize(4));

        // Values
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).keySet(), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)), hasKey(ENTITY_VALUES_1.get(0))); // Oliver
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).keySet(), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)), hasKey(ENTITY_VALUES_1.get(1))); // true
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).keySet(), hasSize(2));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_1.get(2))); // New Zealand
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_1.get(3))); // Australia
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)).keySet(), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)), hasKey(ENTITY_VALUES_1.get(4))); // 24

        // Matching entities
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), contains(ENTITY_1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), contains(ENTITY_1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), contains(ENTITY_1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(3)), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(3)), contains(ENTITY_1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), contains(ENTITY_1));
    }

    @Test
    void test_addEntity_multiple_success() {
        testee.addEntity(ENTITY_1);
        testee.addEntity(ENTITY_2);

        final var data = testee.getData();

        // Entity Type
        assertThat(data, hasKey(ENTITY_TYPE_1));

        // Keys
        final var entityTypeMap = data.get(ENTITY_TYPE_1);
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(0))); // name
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(1))); // person
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(2))); // countries
        assertThat(entityTypeMap, hasKey(ENTITY_KEYS.get(3))); // age
        assertThat(entityTypeMap.keySet(), hasSize(4));

        // Values
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).keySet(), hasSize(2));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)), hasKey(ENTITY_VALUES_1.get(0))); // Oliver
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)), hasKey(ENTITY_VALUES_2.get(0))); // John
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).keySet(), hasSize(2));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)), hasKey(ENTITY_VALUES_1.get(1))); // true
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)), hasKey(ENTITY_VALUES_2.get(1))); // true
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).keySet(), hasSize(3));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_1.get(2))); // New Zealand
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_2.get(2))); // Australia
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_2.get(3))); // USA
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)).keySet(), hasSize(1));
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)), hasKey(ENTITY_VALUES_1.get(4))); // 24

        // Matching entities
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), hasSize(1)); // Oliver
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), contains(ENTITY_1)); // Oliver
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_2.get(0)), hasSize(1)); // John
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_2.get(0)), contains(ENTITY_2)); // John
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), hasSize(1)); // true
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), contains(ENTITY_1)); // true
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_2.get(1)), hasSize(1)); // false
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_2.get(1)), contains(ENTITY_2)); // false
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), hasSize(2)); // New Zealand
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), contains(ENTITY_1, ENTITY_2)); // New Zealand
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_2.get(3)), hasSize(1)); // USA
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_2.get(3)), contains(ENTITY_2)); // USA
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), hasSize(2)); // 24
        assertThat(entityTypeMap.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), contains(ENTITY_1, ENTITY_2)); // 24
    }

    @Test
    void test_addEntity_multiple_differentTypes_success() {
        testee.addEntity(ENTITY_1);
        testee.addEntity(ENTITY_3);

        final var data = testee.getData();

        // Entity Type
        assertThat(data, hasKey(ENTITY_TYPE_1));
        assertThat(data, hasKey(ENTITY_TYPE_2));

        // Keys
        final var entityType1Map = data.get(ENTITY_TYPE_1);
        assertThat(entityType1Map, hasKey(ENTITY_KEYS.get(0))); // name
        assertThat(entityType1Map, hasKey(ENTITY_KEYS.get(1))); // person
        assertThat(entityType1Map, hasKey(ENTITY_KEYS.get(2))); // countries
        assertThat(entityType1Map, hasKey(ENTITY_KEYS.get(3))); // age
        assertThat(entityType1Map.keySet(), hasSize(4));
        final var entityType2Map = data.get(ENTITY_TYPE_2);
        assertThat(entityType2Map, hasKey(ENTITY_KEYS.get(0))); // name
        assertThat(entityType2Map, hasKey(ENTITY_KEYS.get(1))); // person
        assertThat(entityType2Map, hasKey(ENTITY_KEYS.get(2))); // countries
        assertThat(entityType2Map, hasKey(ENTITY_KEYS.get(3))); // age
        assertThat(entityType2Map.keySet(), hasSize(4));

        // Values
        assertThat(entityType1Map.get(ENTITY_KEYS.get(0)).keySet(), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(0)), hasKey(ENTITY_VALUES_1.get(0))); // Oliver
        assertThat(entityType1Map.get(ENTITY_KEYS.get(1)).keySet(), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(1)), hasKey(ENTITY_VALUES_1.get(1))); // true
        assertThat(entityType1Map.get(ENTITY_KEYS.get(2)).keySet(), hasSize(2));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_1.get(2))); // New Zealand
        assertThat(entityType1Map.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_1.get(3))); // Australia
        assertThat(entityType1Map.get(ENTITY_KEYS.get(3)).keySet(), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(3)), hasKey(ENTITY_VALUES_1.get(4))); // 24
        assertThat(entityType2Map.get(ENTITY_KEYS.get(0)).keySet(), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(0)), hasKey(ENTITY_VALUES_1.get(0))); // Oliver
        assertThat(entityType2Map.get(ENTITY_KEYS.get(1)).keySet(), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(1)), hasKey(ENTITY_VALUES_1.get(1))); // true
        assertThat(entityType2Map.get(ENTITY_KEYS.get(2)).keySet(), hasSize(2));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_1.get(2))); // New Zealand
        assertThat(entityType2Map.get(ENTITY_KEYS.get(2)), hasKey(ENTITY_VALUES_1.get(3))); // Australia
        assertThat(entityType2Map.get(ENTITY_KEYS.get(3)).keySet(), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(3)), hasKey(ENTITY_VALUES_1.get(4))); // 24

        // Matching entities
        assertThat(entityType1Map.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), contains(ENTITY_1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), contains(ENTITY_1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), contains(ENTITY_1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(3)), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(3)), contains(ENTITY_1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), hasSize(1));
        assertThat(entityType1Map.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), contains(ENTITY_1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(0)).get(ENTITY_VALUES_1.get(0)), contains(ENTITY_3));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(1)).get(ENTITY_VALUES_1.get(1)), contains(ENTITY_3));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(2)), contains(ENTITY_3));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(3)), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(2)).get(ENTITY_VALUES_1.get(3)), contains(ENTITY_3));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), hasSize(1));
        assertThat(entityType2Map.get(ENTITY_KEYS.get(3)).get(ENTITY_VALUES_1.get(4)), contains(ENTITY_3));
    }

    @Test
    void test_search_found_single() {
        testee.addEntity(ENTITY_1);

        final var result = testee.search(ENTITY_TYPE_1, SEARCH_KEY, SEARCH_VALUE);

        assertThat(result, hasSize(1));
        assertThat(result, contains(ENTITY_1));
    }

    @Test
    void test_search_found_multiple() {
        testee.addEntity(ENTITY_1);
        testee.addEntity(ENTITY_2);

        final var result = testee.search(ENTITY_TYPE_1, MULTIPLE_SEARCH_KEY, MULTIPLE_SEARCH_VALUE);

        assertThat(result, hasSize(2));
        assertThat(result, contains(ENTITY_1, ENTITY_2));
    }

    @Test
    void test_search_notFound() {
        final var result = testee.search(ENTITY_TYPE_1, SEARCH_KEY, SEARCH_VALUE);

        assertThat(result, is(Collections.emptyList()));
    }

    @Test
    void test_search_found_blankValue() {
        testee.addEntity(ENTITY_4);

        final var result = testee.search(ENTITY_TYPE_1, SEARCH_KEY, BLANK_VALUE);

        assertThat(result, hasSize(1));
        assertThat(result, contains(ENTITY_4));
    }
}