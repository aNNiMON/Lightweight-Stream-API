package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalInt;

import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.isPresent;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.hasValueThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OptionalIntMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(OptionalIntMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsPresent() {
        OptionalInt optional = OptionalInt.of(5);
        assertThat(optional, isPresent());
        assertThat(optional, not(isEmpty()));

        assertThat(isPresent(), description(is("OptionalInt value should be present")));
    }

    @Test
    public void testIsEmpty() {
        OptionalInt optional = OptionalInt.empty();
        assertThat(optional, isEmpty());
        assertThat(optional, not(isPresent()));

        assertThat(isEmpty(), description(is("OptionalInt value should be empty")));
    }

    @Test
    public void testHasValue() {
        OptionalInt optional = OptionalInt.of(42);
        assertThat(optional, hasValue(42));
        assertThat(optional, not(hasValue(13)));

        assertThat(hasValue(42), description(is("OptionalInt value is <42>")));
    }

    @Test
    public void testHasValueThat() {
        OptionalInt optional = OptionalInt.of(42);
        assertThat(optional, hasValueThat(is(not(17))));

        assertThat(hasValueThat(is(42)), description(is("OptionalInt value is <42>")));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueOnEmptyOptional() {
        assertThat(OptionalInt.empty(), hasValue(0));
    }

    @Test(expected = AssertionError.class)
    public void testIsEmptyOnNullValue() {
        assertThat(null, isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testIsPresentOnNullValue() {
        assertThat(null, isPresent());
    }

    @Test(expected = AssertionError.class)
    public void testHasValueOnNullValue() {
        assertThat(null, hasValue(0));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueThatOnNullValue() {
        assertThat(null, hasValueThat(is(0)));
    }
}
