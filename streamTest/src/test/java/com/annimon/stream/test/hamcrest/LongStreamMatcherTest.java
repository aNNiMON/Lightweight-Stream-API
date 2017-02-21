package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Collectors;
import com.annimon.stream.LongStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.hasElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LongStreamMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(LongStreamMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsEmpty() {
        assertThat(LongStream.empty(), isEmpty());

        StringDescription description = new StringDescription();
        isEmpty().describeTo(description);
        assertThat(description.toString(), is("an empty stream"));
    }

    @Test
    public void testHasElements() {
        assertThat(LongStream.of(1, 2), hasElements());
        assertThat(LongStream.empty(), not(hasElements()));

        StringDescription description = new StringDescription();
        hasElements().describeTo(description);
        assertThat(description.toString(), is("a non-empty stream"));
    }

    @Test(expected = AssertionError.class)
    public void testIsEmptyOnNullValue() {
        assertThat(null, isEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testHasElementsOnNullValue() {
        assertThat(null, hasElements());
    }

    @Test
    public void testElements() {
        final LongStream stream = LongStream.of(-813, 123456, Integer.MAX_VALUE);
        final Long[] expected = new Long[] {-813L, 123456L, (long) Integer.MAX_VALUE};
        final Matcher<LongStream> matcher = elements(arrayContaining(expected));
        assertThat(stream, matcher);
        assertTrue(matcher.matches(stream));

        assertFalse(elements(arrayContaining(expected)).matches(LongStream.empty()));

        assertThat(matcher, description(allOf(
                containsString("LongStream elements"),
                containsString(Stream.of(expected)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long t) {
                                return String.format("<%sL>", t.toString());
                            }
                        })
                        .collect(Collectors.joining(", ")))
        )));
    }

    @Test
    public void testAssertIsEmptyOperator() {
        LongStream.empty()
                .custom(LongStreamMatcher.assertIsEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testAssertIsEmptyOperatorOnEmptyStream() {
        LongStream.of(1, 2)
                .custom(LongStreamMatcher.assertIsEmpty());
    }

    @Test
    public void testAssertHasElementsOperator() {
        LongStream.of(1, 2)
                .custom(LongStreamMatcher.assertHasElements());
    }

    @Test(expected = AssertionError.class)
    public void testAssertHasElementsOperatorOnEmptyStream() {
        LongStream.empty()
                .custom(LongStreamMatcher.assertHasElements());
    }

    @Test
    public void testAssertElementsOperator() {
        LongStream.of(-813, 123456, Integer.MAX_VALUE)
                .custom(LongStreamMatcher.assertElements(
                        arrayContaining(new Long[] {
                            -813L, 123456L, (long) Integer.MAX_VALUE
                        })));
    }
}
