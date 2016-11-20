package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalLong;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValueThat;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class OptionalLongMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(OptionalLongMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsPresent() {
        OptionalLong optional = OptionalLong.of(5L);
        assertThat(optional, isPresent());
        assertThat(optional, not(isEmpty()));

        assertThat(isPresent(), description(is("OptionalLong value should be present")));
    }

    @Test
    public void testIsEmpty() {
        OptionalLong optional = OptionalLong.empty();
        assertThat(optional, isEmpty());
        assertThat(optional, not(isPresent()));

        assertThat(isEmpty(), description(is("OptionalLong value should be empty")));
    }

    @Test
    public void testHasValue() {
        OptionalLong optional = OptionalLong.of(42);
        assertThat(optional, hasValue(42));
        assertThat(optional, not(hasValue(13)));

        assertThat(hasValue(42L), description(is("OptionalLong value is <42L>")));
    }

    @Test
    public void testHasValueThat() {
        OptionalLong optional = OptionalLong.of(42L);
        assertThat(optional, hasValueThat(is(not(17L))));

        assertThat(hasValueThat(is(42L)), description(is("OptionalLong value is <42L>")));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueOnEmptyOptional() {
        assertThat(OptionalLong.empty(), hasValue(0));
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
        assertThat(null, hasValueThat(is(0L)));
    }
}
