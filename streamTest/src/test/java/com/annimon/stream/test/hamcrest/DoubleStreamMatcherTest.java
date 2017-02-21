package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Collectors;
import com.annimon.stream.DoubleStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.hasElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DoubleStreamMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(DoubleStreamMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIsEmpty() {
        assertThat(DoubleStream.empty(), isEmpty());

        StringDescription description = new StringDescription();
        isEmpty().describeTo(description);
        assertThat(description.toString(), is("an empty stream"));
    }

    @Test
    public void testHasElements() {
        assertThat(DoubleStream.of(1, 2), hasElements());
        assertThat(DoubleStream.empty(), not(hasElements()));

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
        final DoubleStream stream = DoubleStream.of(-0.987, 1.234, Math.PI, 1.618);
        final Double[] expected = new Double[] {-0.987, 1.234, Math.PI, 1.618};
        final Matcher<DoubleStream> matcher = elements(arrayContaining(expected));
        assertThat(stream, matcher);
        assertTrue(matcher.matches(stream));

        assertFalse(elements(arrayContaining(expected)).matches(DoubleStream.empty()));

        assertThat(matcher, description(allOf(
                containsString("DoubleStream elements"),
                containsString(Stream.of(expected)
                        .map(new Function<Double, String>() {
                            @Override
                            public String apply(Double t) {
                                return String.format("<%s>", t.toString());
                            }
                        })
                        .collect(Collectors.joining(", ")))
        )));
    }

    @Test
    public void testAssertIsEmptyOperator() {
        DoubleStream.empty()
                .custom(DoubleStreamMatcher.assertIsEmpty());
    }

    @Test(expected = AssertionError.class)
    public void testAssertIsEmptyOperatorOnEmptyStream() {
        DoubleStream.of(1, 2)
                .custom(DoubleStreamMatcher.assertIsEmpty());
    }

    @Test
    public void testAssertHasElementsOperator() {
        DoubleStream.of(1, 2)
                .custom(DoubleStreamMatcher.assertHasElements());
    }

    @Test(expected = AssertionError.class)
    public void testAssertHasElementsOperatorOnEmptyStream() {
        DoubleStream.empty()
                .custom(DoubleStreamMatcher.assertHasElements());
    }

    @Test
    public void testAssertElementsOperator() {
        DoubleStream.of(-0.987, 1.234, Math.PI, 1.618)
                .custom(DoubleStreamMatcher.assertElements(
                        arrayContaining(new Double[] {
                            -0.987, 1.234, Math.PI, 1.618
                        })));
    }
}
