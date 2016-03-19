package com.annimon.stream.test;

import com.annimon.stream.Optional;
import static com.annimon.stream.test.CommonMatcher.description;
import static com.annimon.stream.test.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.OptionalMatcher.isEmpty;
import static com.annimon.stream.test.OptionalMatcher.isPresent;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import org.junit.Test;

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
    }
}
