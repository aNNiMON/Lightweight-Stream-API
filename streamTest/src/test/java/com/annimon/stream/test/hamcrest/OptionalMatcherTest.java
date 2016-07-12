package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Optional;

import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isPresent;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.hasValueThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.startsWith;
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

    @Test
    public void testHasValue() {
        Optional<String> optional = Optional.of("text");
        assertThat(optional, hasValue("text"));
        assertThat(optional, not(hasValue("test")));

        assertThat(hasValue(42), description(is("Optional value is <42>")));
    }

    @Test
    public void testHasValueThat() {
        Optional<String> optional = Optional.of("text");
        assertThat(optional, hasValueThat(startsWith("te")));

        assertThat(hasValueThat(is(42)), description(is("Optional value is <42>")));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueOnEmptyOptional() {
        assertThat(Optional.<String>empty(), hasValue(""));
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
        assertThat(null, hasValue(""));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueThatOnNullValue() {
        assertThat(null, hasValueThat(is("")));
    }
}
