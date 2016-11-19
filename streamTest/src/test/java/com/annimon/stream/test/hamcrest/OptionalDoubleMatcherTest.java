package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalDouble;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValueThat;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isPresent;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class OptionalDoubleMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(OptionalDoubleMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsPresent() {
        OptionalDouble optional = OptionalDouble.of(12.34);
        assertThat(optional, isPresent());
        assertThat(optional, not(isEmpty()));

        assertThat(isPresent(), description(is("OptionalDouble value should be present")));
    }

    @Test
    public void testIsEmpty() {
        OptionalDouble optional = OptionalDouble.empty();
        assertThat(optional, isEmpty());
        assertThat(optional, not(isPresent()));

        assertThat(isEmpty(), description(is("OptionalDouble value should be empty")));
    }

    @Test
    public void testHasValue() {
        OptionalDouble optional = OptionalDouble.of(12.34);
        assertThat(optional, hasValue(12.34));
        assertThat(optional, not(hasValue(13)));

        assertThat(hasValue(12.34), description(is("OptionalDouble value is <12.34>")));
    }

    @Test
    public void testHasValueThat() {
        OptionalDouble optional = OptionalDouble.of(42.0);
        assertThat(optional, hasValueThat(is(not(Math.PI))));

        assertThat(hasValueThat(closeTo(42.0, 0.00001)), description(containsString("<42.0>")));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueOnEmptyOptional() {
        assertThat(OptionalDouble.empty(), hasValue(0));
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
        assertThat(null, hasValue(0d));
    }

    @Test(expected = AssertionError.class)
    public void testHasValueThatOnNullValue() {
        assertThat(null, hasValueThat(is(0d)));
    }
}
