package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.hasElements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.isEmpty;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class IntStreamMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IntStreamMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsEmpty() {
        assertThat(IntStream.empty(), isEmpty());

        StringDescription description = new StringDescription();
        isEmpty().describeTo(description);
        assertThat(description.toString(), is("an empty stream"));
    }

    @Test
    public void testHasElements() {
        assertThat(IntStream.of(1, 2), hasElements());
        assertThat(IntStream.empty(), not(hasElements()));

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
        final IntStream stream = IntStream.of(-813, 123456, Short.MAX_VALUE);
        final Integer[] expected = new Integer[] {-813, 123456, (int) Short.MAX_VALUE};
        final Matcher<IntStream> matcher = elements(arrayContaining(expected));
        assertThat(stream, matcher);
        assertTrue(matcher.matches(stream));

        assertFalse(elements(arrayContaining(expected)).matches(IntStream.empty()));

        assertThat(matcher, description(allOf(
                containsString("IntStream elements"),
                containsString(Stream.of(expected)
                        .map(new Function<Integer, String>() {
                            @Override
                            public String apply(Integer t) {
                                return String.format("<%s>", t.toString());
                            }
                        })
                        .collect(Collectors.joining(", ")))
        )));
    }

    @Test
    public void testAssertIsEmptyOperator() {
        IntStream.empty()
                .custom(IntStreamMatcher.assertIsEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testAssertIsEmptyOperatorOnEmptyStream() {
        IntStream.of(1, 2)
                .custom(IntStreamMatcher.assertIsEmpty());
    }

    @Test
    public void testAssertHasElementsOperator() {
        IntStream.of(1, 2)
                .custom(IntStreamMatcher.assertHasElements());
    }

    @Test(expected = AssertionError.class)
    public void testAssertHasElementsOperatorOnEmptyStream() {
        IntStream.empty()
                .custom(IntStreamMatcher.assertHasElements());
    }

    @Test
    public void testAssertElementsOperator() {
        IntStream.of(-813, 123456, Short.MAX_VALUE)
                .custom(IntStreamMatcher.assertElements(
                        arrayContaining(new Integer[] {
                            -813, 123456, (int) Short.MAX_VALUE
                        })));
    }
}
