package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalBoolean;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.OptionalBooleanMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalBooleanMatcher.hasValueThat;
import static com.annimon.stream.test.hamcrest.OptionalBooleanMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalBooleanMatcher.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OptionalBooleanMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(OptionalBooleanMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsPresent() {
        OptionalBoolean optional = OptionalBoolean.of(true);
        assertThat(optional, isPresent());
        assertThat(optional, not(isEmpty()));

        assertThat(isPresent(), description(is("OptionalBoolean value should be present")));
    }

    @Test
    public void testIsEmpty() {
        OptionalBoolean optional = OptionalBoolean.empty();
        assertThat(optional, isEmpty());
        assertThat(optional, not(isPresent()));

        assertThat(isEmpty(), description(is("OptionalBoolean value should be empty")));
    }

    @Test
    public void testHasValue() {
        OptionalBoolean optional = OptionalBoolean.of(false);
        assertThat(optional, hasValue(false));
        assertThat(optional, not(hasValue(true)));

        assertThat(hasValue(false), description(is("OptionalBoolean value is <false>")));
    }

    @Test
    public void testHasValueThat() {
        OptionalBoolean optional = OptionalBoolean.of(true);
        assertThat(optional, hasValueThat(is(not(false))));

        assertThat(hasValueThat(is(true)), description(is("OptionalBoolean value is <true>")));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueOnEmptyOptional() {
        assertThat(OptionalBoolean.empty(), hasValue(true));
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
        assertThat(null, hasValue(true));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueThatOnNullValue() {
        assertThat(null, hasValueThat(is(false)));
    }
}
