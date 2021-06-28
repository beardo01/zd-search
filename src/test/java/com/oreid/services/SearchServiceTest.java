package com.oreid.services;

import com.oreid.domain.EntityType;
import com.oreid.model.AbstractEntity;
import com.oreid.model.SearchType;
import com.oreid.repository.Datastore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static com.oreid.TestFixtures.*;
import static com.oreid.domain.SearchConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    private static AbstractEntity ENTITY_1;
    private static final EntityType ENTITY_TYPE = EntityType.ORGANIZATION;
    private static final SearchType SEARCH_TYPE = createSearchType(ENTITY_TYPE);
    private static final AbstractEntity ENTITY_2 = createAbstractEntity(ENTITY_TYPE, JSON_STRING_2);
    private static final String VALID_FIELD = "name";
    private static final String INVALID_FIELD = "INVALID";
    private static final String KEY = "KEY";
    private static final String VALUE = "VALUE";

    private static final String ENTITY_1_NAME = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(0), ENTITY_1_VALUES.get(0));
    private static final String ENTITY_1_PERSON = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(1), ENTITY_1_VALUES.get(1));
    private static final String ENTITY_1_COUNTRIES = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(2), ENTITY_1_VALUES.get(2));
    private static final String ENTITY_1_AGE = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(3), ENTITY_1_VALUES.get(3));

    private static final String ENTITY_2_NAME = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(0), ENTITY_2_VALUES.get(0));
    private static final String ENTITY_2_PERSON = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(1), ENTITY_2_VALUES.get(1));
    private static final String ENTITY_2_COUNTRIES = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(2), ENTITY_2_VALUES.get(2));
    private static final String ENTITY_2_AGE = String.format(ENTITY_DESCRIPTION_FORMAT, ENTITY_KEYS.get(3), ENTITY_2_VALUES.get(3));
    private static final String ENTITY_2_RELATED_DESCRIPTION = String.format(ENTITY_DESCRIPTION_FORMAT,
            String.format(RELATED_ENTITY_DESCRIPTION_FORMAT, ENTITY_TYPE, 1), ENTITY_2_VALUES.get(0));

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        ENTITY_1 = createAbstractEntity(ENTITY_TYPE, JSON_STRING);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Mock
    private Datastore datastore;

    @InjectMocks
    private SearchService testee;

    @Test
    void test_addSearchType() {
        assertThat(testee.getSearchTypes(), is(Collections.emptyMap()));

        testee.addSearchType(SEARCH_TYPE);

        assertThat(testee.getSearchTypes(), hasEntry(ENTITY_TYPE.toString(), SEARCH_TYPE));
        assertThat(testee.getSearchTypes().size(), is(1));
    }

    @Test
    void test_isValidField_valid() {
        testee.addSearchType(SEARCH_TYPE);

        final var result = testee.isValidField(ENTITY_TYPE, VALID_FIELD);

        assertThat(result, is(true));
    }

    @Test
    void test_isValidField_doesNotExist() {
        final var result = testee.isValidField(ENTITY_TYPE, VALID_FIELD);

        assertThat(result, is(false));
    }

    @Test
    void test_isValidField_invalid() {
        testee.addSearchType(SEARCH_TYPE);

        final var result = testee.isValidField(ENTITY_TYPE, INVALID_FIELD);

        assertThat(result, is(false));
    }

    @Test
    void test_addEntity() {
        testee.addEntity(ENTITY_1);

        verify(datastore, times(1)).addEntity(ENTITY_1);
    }

    @Test
    void test_printSearchResults_single() {
        when(datastore.search(ENTITY_TYPE, KEY, VALUE)).thenReturn(List.of(ENTITY_1));

        testee.printSearchResults(ENTITY_TYPE, KEY, VALUE);

        String output = outContent.toString();
        assertThat(output.contains(ENTITY_1_NAME), is(true));
        assertThat(output.contains(ENTITY_1_PERSON), is(true));
        assertThat(output.contains(ENTITY_1_COUNTRIES), is(true));
        assertThat(output.contains(ENTITY_1_AGE), is(true));
        assertThat(output.contains(PRINT_SEPARATOR), is(true));
        assertThat(output.length(), is(sumStringLengths(
                ENTITY_1_NAME,
                ENTITY_1_PERSON,
                ENTITY_1_COUNTRIES,
                ENTITY_1_AGE,
                PRINT_SEPARATOR
        )));
    }

    @Test
    void test_printSearchResults_singleRelatedEntity() {
        ENTITY_1.addRelatedEntity(ENTITY_2);
        when(datastore.search(ENTITY_TYPE, KEY, VALUE)).thenReturn(List.of(ENTITY_1));

        testee.printSearchResults(ENTITY_TYPE, KEY, VALUE);

        String output = outContent.toString();
        assertThat(output.contains(ENTITY_1_NAME), is(true));
        assertThat(output.contains(ENTITY_1_PERSON), is(true));
        assertThat(output.contains(ENTITY_1_COUNTRIES), is(true));
        assertThat(output.contains(ENTITY_1_AGE), is(true));
        assertThat(output.contains(ENTITY_2_RELATED_DESCRIPTION), is(true));
        assertThat(output.contains(PRINT_SEPARATOR), is(true));
        assertThat(output.length(), is(sumStringLengths(
                ENTITY_1_NAME,
                ENTITY_1_PERSON,
                ENTITY_1_COUNTRIES,
                ENTITY_1_AGE,
                ENTITY_2_RELATED_DESCRIPTION,
                PRINT_SEPARATOR
        )));
    }

    @Test
    void test_printSearchResults_multiple() {
        when(datastore.search(ENTITY_TYPE, KEY, VALUE)).thenReturn(List.of(ENTITY_1, ENTITY_2));

        testee.printSearchResults(ENTITY_TYPE, KEY, VALUE);

        String output = outContent.toString();
        assertThat(output.contains(ENTITY_1_NAME), is(true));
        assertThat(output.contains(ENTITY_1_PERSON), is(true));
        assertThat(output.contains(ENTITY_1_COUNTRIES), is(true));
        assertThat(output.contains(ENTITY_1_AGE), is(true));
        assertThat(output.contains(ENTITY_2_NAME), is(true));
        assertThat(output.contains(ENTITY_2_PERSON), is(true));
        assertThat(output.contains(ENTITY_2_COUNTRIES), is(true));
        assertThat(output.contains(ENTITY_2_AGE), is(true));
        assertThat(output.contains(PRINT_SEPARATOR), is(true));
        assertThat(output.length(), is(sumStringLengths(
                ENTITY_1_NAME,
                ENTITY_1_PERSON,
                ENTITY_1_COUNTRIES,
                ENTITY_1_AGE,
                PRINT_SEPARATOR,
                ENTITY_2_NAME,
                ENTITY_2_PERSON,
                ENTITY_2_COUNTRIES,
                ENTITY_2_AGE,
                PRINT_SEPARATOR
        )));
    }

    @Test
    void test_printSearchResults_noResult() {
        when(datastore.search(ENTITY_TYPE, KEY, VALUE)).thenReturn(Collections.emptyList());

        testee.printSearchResults(ENTITY_TYPE, KEY, VALUE);

        String output = outContent.toString();
        assertThat(output.contains(NO_RESULTS), is(true));
        assertThat(output.length(), is(sumStringLengths(NO_RESULTS)));
    }

}