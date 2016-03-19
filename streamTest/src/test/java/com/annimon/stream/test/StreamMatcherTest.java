package com.annimon.stream.test;

import com.annimon.stream.Stream;
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
import org.hamcrest.core.IsNot;
import static org.junit.Assert.*;
import org.junit.Test;

public class StreamMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        TestUtils.testPrivateConstructor(StreamMatcher.class);
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

        assertFalse(elements(is(expected)).matches(Stream.<Integer>empty()));

        StringDescription description = new StringDescription();
        matcher.describeTo(description);
        assertThat(description.toString(), allOf(
                containsString("Stream elements is"),
                containsString(expected.toString())
        ));
    }
}
