package com.annimon.stream.test;

import com.annimon.stream.Stream;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.annimon.stream.test.CommonMatcher.description;
import static com.annimon.stream.test.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.StreamMatcher.elements;
import static com.annimon.stream.test.StreamMatcher.hasElements;
import static com.annimon.stream.test.StreamMatcher.isEmpty;
import static com.annimon.stream.test.StreamMatcher.isNotEmpty;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class StreamMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(StreamMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsEmpty() {
        assertThat(Stream.empty(), isEmpty());
        assertThat(Stream.of(1, 2), isNotEmpty());

        StringDescription description = new StringDescription();
        isEmpty().describeTo(description);
        assertThat(description.toString(), is("an empty stream"));
    }

    @Test
    public void testHasElements() {
        assertThat(Stream.of(1, 2), hasElements());
        assertThat(Stream.empty(), not(hasElements()));

        StringDescription description = new StringDescription();
        hasElements().describeTo(description);
        assertThat(description.toString(), is("a non-empty stream"));
    }

    @Test(expected = AssertionError.class)
    public void testIsEmptyOnNullValue() {
        assertThat(null, isEmpty());
    }

    @Test
    public void testIsNotEmptyOnNullValue() {
        assertThat(null, isNotEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testHasElementsOnNullValue() {
        assertThat(null, hasElements());
    }

    @Test
    public void testElements() {
        final Stream<Integer> stream = Stream.range(0, 5);
        final List<Integer> expected = Arrays.asList(0, 1, 2, 3, 4);
        final Matcher<Stream<Integer>> matcher = elements(is(expected));
        assertThat(stream, matcher);
        assertTrue(matcher.matches(stream));

        assertFalse(elements(is(expected)).matches(Stream.<Integer>empty()));

        assertThat(matcher, description(allOf(
                containsString("Stream elements is "),
                containsString(expected.toString())
        )));
    }
}
