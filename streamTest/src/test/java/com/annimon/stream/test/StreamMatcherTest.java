package com.annimon.stream.test;

import com.annimon.stream.Stream;
import static com.annimon.stream.test.CommonMatcher.description;
import static com.annimon.stream.test.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.StreamMatcher.elements;
import static com.annimon.stream.test.StreamMatcher.isEmpty;
import static com.annimon.stream.test.StreamMatcher.isNotEmpty;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import static org.junit.Assert.*;
import org.junit.Test;

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
