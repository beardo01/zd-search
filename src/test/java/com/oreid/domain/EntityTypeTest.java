package com.oreid.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntityTypeTest {

    private static final String EXPECTED_TICKET_TO_STRING = "ticket";
    private static final String EXPECTED_ORGANIZATION_TO_STRING = "organization";
    private static final String EXPECTED_USER_TO_STRING = "user";

    @Test
    void test_toString() {
        assertThat(EntityType.TICKET.toString(), is(EXPECTED_TICKET_TO_STRING));
        assertThat(EntityType.ORGANIZATION.toString(), is(EXPECTED_ORGANIZATION_TO_STRING));
        assertThat(EntityType.USER.toString(), is(EXPECTED_USER_TO_STRING));
    }

}