package com.oreid.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.core.Is.is;

@ExtendWith(MockitoExtension.class)
class DatastoreTest {

    @InjectMocks
    private Datastore testee;

    @Test
    void test_constructor_success() {
        assertThat(testee.getData(), is(anEmptyMap()));
        assertThat(testee.getEntities(), is(Collections.emptyList()));
    }
}