package com.oreid.services;

import com.oreid.domain.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.security.SystemExit;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.oreid.TestFixtures.sumStringLengths;
import static com.oreid.domain.MenuConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static uk.org.webcompere.systemstubs.SystemStubs.withTextFromSystemIn;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private static final String INVALID_INPUT = "INVALID";
    private static final int EXIT_CODE = 0;
    private static final String SEARCH_TERM = "SEARCH_TERM";
    private static final String SEARCH_VALUE = "SEARCH_VALUE";
    private static final EntityType ENTITY_TYPE = EntityType.ORGANIZATION;

    @SystemStub
    private final SystemExit systemExit = new SystemExit();

    @Mock
    private SearchService searchService;

    @InjectMocks
    private MenuService testee;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void test_printMenu_success() {
        testee.printMenu();

        assertThat(outContent.toString(), is(MAIN_MENU));
    }

    @Test
    void test_processMenuInput_searchOption_validOptions() throws Exception {
        withTextFromSystemIn(ORGANIZATION_OPTION, SEARCH_TERM, SEARCH_VALUE).execute(() -> {
            // Reconstruct testee so that Scanner is mocked
            testee = new MenuService(searchService);

            when(searchService.isValidField(ENTITY_TYPE, SEARCH_TERM)).thenReturn(true);

            testee.processMenuInput(SEARCH_OPTION);

            assertThat(outContent.toString().contains(SELECT_ENTITY_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SEARCH_TERM_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SEARCH_VALUE_MESSAGE), is(true));
            assertThat(outContent.toString().contains(MAIN_MENU), is(true));
            assertThat(outContent.toString().length(), is(sumStringLengths(
                            SELECT_ENTITY_MESSAGE,
                            SEARCH_TERM_MESSAGE,
                            SEARCH_VALUE_MESSAGE,
                            MAIN_MENU
                    )));
            verify(searchService, times(1)).isValidField(ENTITY_TYPE, SEARCH_TERM);
            verify(searchService, times(1)).printSearchResults(
                    ENTITY_TYPE,
                    SEARCH_TERM,
                    SEARCH_VALUE
            );
        });
    }

    @Test
    void test_processMenuInput_searchOption_invalidThenValidType() throws Exception {
        withTextFromSystemIn(INVALID_INPUT, ORGANIZATION_OPTION, SEARCH_TERM, SEARCH_VALUE).execute(() -> {
            // Reconstruct testee so that Scanner is mocked
            testee = new MenuService(searchService);

            when(searchService.isValidField(ENTITY_TYPE, SEARCH_TERM)).thenReturn(true);

            testee.processMenuInput(SEARCH_OPTION);

            assertThat(outContent.toString().contains(INVALID_INPUT_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SELECT_ENTITY_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SEARCH_TERM_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SEARCH_VALUE_MESSAGE), is(true));
            assertThat(outContent.toString().contains(MAIN_MENU), is(true));
            assertThat(outContent.toString().length(), is(sumStringLengths(
                    SELECT_ENTITY_MESSAGE,
                    INVALID_INPUT_MESSAGE,
                    SELECT_ENTITY_MESSAGE,
                    SEARCH_TERM_MESSAGE,
                    SEARCH_VALUE_MESSAGE,
                    MAIN_MENU
            )));
            verify(searchService, times(1)).isValidField(ENTITY_TYPE, SEARCH_TERM);
            verify(searchService, times(1)).printSearchResults(
                    ENTITY_TYPE,
                    SEARCH_TERM,
                    SEARCH_VALUE
            );
        });
    }

    @Test
    void test_processMenuInput_searchOption_invalidThenValidTerm() throws Exception {
        withTextFromSystemIn(ORGANIZATION_OPTION, INVALID_INPUT, SEARCH_TERM, SEARCH_VALUE).execute(() -> {
            // Reconstruct testee so that Scanner is mocked
            testee = new MenuService(searchService);

            when(searchService.isValidField(ENTITY_TYPE, INVALID_INPUT)).thenReturn(false);
            when(searchService.isValidField(ENTITY_TYPE, SEARCH_TERM)).thenReturn(true);

            testee.processMenuInput(SEARCH_OPTION);

            assertThat(outContent.toString().contains(INVALID_INPUT_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SELECT_ENTITY_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SEARCH_TERM_MESSAGE), is(true));
            assertThat(outContent.toString().contains(SEARCH_VALUE_MESSAGE), is(true));
            assertThat(outContent.toString().contains(MAIN_MENU), is(true));
            assertThat(outContent.toString().length(), is(sumStringLengths(
                    SELECT_ENTITY_MESSAGE,
                    SEARCH_TERM_MESSAGE,
                    INVALID_INPUT_MESSAGE,
                    SEARCH_TERM_MESSAGE,
                    SEARCH_VALUE_MESSAGE,
                    MAIN_MENU
            )));
            verify(searchService, times(1)).isValidField(ENTITY_TYPE, INVALID_INPUT);
            verify(searchService, times(1)).isValidField(ENTITY_TYPE, SEARCH_TERM);
            verify(searchService, times(1)).printSearchResults(
                    ENTITY_TYPE,
                    SEARCH_TERM,
                    SEARCH_VALUE
            );
        });
    }

    @Test
    void test_processMenuInput_fieldsOption() {
        testee.processMenuInput(FIELDS_OPTION);

        verify(searchService, times(1)).printSearchTypes();
        verifyNoMoreInteractions(searchService);
    }

    @Test
    void test_processMenuInput_quitOption() throws Exception {
        systemExit.execute(() -> testee.processMenuInput(QUIT_OPTION));

        assertThat(systemExit.getExitCode(), is(EXIT_CODE));
        assertThat(outContent.toString(), is(QUIT_MESSAGE));
        verifyNoInteractions(searchService);
    }

    @Test
    void test_processMenuInput_invalidOption() {
        testee.processMenuInput(INVALID_INPUT);

        assertThat(outContent.toString(), is(INVALID_INPUT_MESSAGE));
        verifyNoInteractions(searchService);
    }

    @Test
    void test_getEntityType_user() throws Exception {
        withTextFromSystemIn(USER_OPTION).execute(() -> {
            // Reconstruct testee so that Scanner is mocked
            testee = new MenuService(searchService);

            final var result = testee.getEntityType();

            assertThat(result, is(EntityType.USER));
            assertThat(outContent.toString().contains(SELECT_ENTITY_MESSAGE), is(true));
            assertThat(outContent.toString().length(), is(sumStringLengths(SELECT_ENTITY_MESSAGE)));
        });
    }

    @Test
    void test_getEntityType_ticket() throws Exception {
        withTextFromSystemIn(TICKET_OPTION).execute(() -> {
            // Reconstruct testee so that Scanner is mocked
            testee = new MenuService(searchService);

            final var result = testee.getEntityType();

            assertThat(result, is(EntityType.TICKET));
            assertThat(outContent.toString().contains(SELECT_ENTITY_MESSAGE), is(true));
            assertThat(outContent.toString().length(), is(sumStringLengths(SELECT_ENTITY_MESSAGE)));
        });
    }

    @Test
    void test_getEntityType_organization() throws Exception {
        withTextFromSystemIn(ORGANIZATION_OPTION).execute(() -> {
            // Reconstruct testee so that Scanner is mocked
            testee = new MenuService(searchService);

            final var result = testee.getEntityType();

            assertThat(result, is(EntityType.ORGANIZATION));
            assertThat(outContent.toString().contains(SELECT_ENTITY_MESSAGE), is(true));
            assertThat(outContent.toString().length(), is(sumStringLengths(SELECT_ENTITY_MESSAGE)));
        });
    }

}