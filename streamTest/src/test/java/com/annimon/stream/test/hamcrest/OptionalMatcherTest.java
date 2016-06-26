package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Optional;

import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OptionalMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(OptionalMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsPresent() {
        Optional<Integer> optional = Optional.of(5);
        assertThat(optional, isPresent());
        assertThat(optional, not(isEmpty()));

        assertThat(isPresent(), description(is("Optional value should be present")));
    }

    @Test
    public void testIsEmpty() {
        Optional<Integer> optional = Optional.empty();
        assertThat(optional, isEmpty());
        assertThat(optional, not(isPresent()));

        assertThat(isEmpty(), description(is("Optional value should be empty")));
    }

    @Test(expected = AssertionError.class)
    public void testIsEmptyOnNullValue() {
        assertThat(null, isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testIsPresentOnNullValue() {
        assertThat(null, isPresent());
    }
}
