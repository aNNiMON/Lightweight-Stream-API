package com.annimon.stream.test.hamcrest;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntFunction;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntStreamMatcher {

    private IntStreamMatcher() { }

    public static Matcher<IntStream> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<IntStream> hasElements() {
        return new HasElementsMatcher();
    }

    public static Matcher<IntStream> elements(Matcher<Integer[]> matcher) {
        return new ElementsMatcher(matcher);
    }

    public static Function<IntStream, Void> assertIsEmpty() {
        return new Function<IntStream, Void>() {

            @Override
            public Void apply(IntStream t) {
                assertThat(t, isEmpty());
                return null;
            }
        };
    }

    public static Function<IntStream, Void> assertHasElements() {
        return new Function<IntStream, Void>() {

            @Override
            public Void apply(IntStream t) {
                assertThat(t, hasElements());
                return null;
            }
        };
    }

    public static Function<IntStream, Void> assertElements(final Matcher<Integer[]> matcher) {
        return new Function<IntStream, Void>() {

            @Override
            public Void apply(IntStream t) {
                assertThat(t, elements(matcher));
                return null;
            }
        };
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<IntStream> {

        @Override
        protected boolean matchesSafely(IntStream stream, Description mismatchDescription) {
            mismatchDescription.appendText("IntStream was not empty");
            return stream.count() == 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("an empty stream");
        }
    }

    public static class HasElementsMatcher extends TypeSafeDiagnosingMatcher<IntStream> {

        @Override
        protected boolean matchesSafely(IntStream stream, Description mismatchDescription) {
            mismatchDescription.appendText("IntStream was empty");
            return stream.count() > 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a non-empty stream");
        }
    }

    public static class ElementsMatcher extends TypeSafeDiagnosingMatcher<IntStream> {

        private final Matcher<Integer[]> matcher;
        private Integer[] streamElements;

        public ElementsMatcher(Matcher<Integer[]> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(IntStream stream, Description mismatchDescription) {
            final Integer[] elements;
            if (streamElements == null) {
                elements = stream.boxed().toArray(BOXED_ARRAY_CREATOR);
                streamElements = elements;
            } else {
                elements = streamElements;
            }
            if (!matcher.matches(elements)) {
                mismatchDescription.appendText("IntStream elements ");
                matcher.describeMismatch(elements, mismatchDescription);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("IntStream elements ").appendDescriptionOf(matcher);
        }
    }

    private static final IntFunction<Integer[]> BOXED_ARRAY_CREATOR = new IntFunction<Integer[]>() {
        @Override
        public Integer[] apply(int value) {
            return new Integer[value];
        }
    };
}
