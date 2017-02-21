package com.annimon.stream.test.hamcrest;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntFunction;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import static org.hamcrest.MatcherAssert.assertThat;

public class DoubleStreamMatcher {

    private DoubleStreamMatcher() { }

    public static Matcher<DoubleStream> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<DoubleStream> hasElements() {
        return new HasElementsMatcher();
    }

    public static Matcher<DoubleStream> elements(Matcher<Double[]> matcher) {
        return new ElementsMatcher(matcher);
    }

    public static Function<DoubleStream, Void> assertIsEmpty() {
        return new Function<DoubleStream, Void>() {

            @Override
            public Void apply(DoubleStream t) {
                assertThat(t, isEmpty());
                return null;
            }
        };
    }

    public static Function<DoubleStream, Void> assertHasElements() {
        return new Function<DoubleStream, Void>() {

            @Override
            public Void apply(DoubleStream t) {
                assertThat(t, hasElements());
                return null;
            }
        };
    }

    public static Function<DoubleStream, Void> assertElements(final Matcher<Double[]> matcher) {
        return new Function<DoubleStream, Void>() {

            @Override
            public Void apply(DoubleStream t) {
                assertThat(t, elements(matcher));
                return null;
            }
        };
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<DoubleStream> {

        @Override
        protected boolean matchesSafely(DoubleStream stream, Description mismatchDescription) {
            mismatchDescription.appendText("DoubleStream was not empty");
            return stream.count() == 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("an empty stream");
        }
    }

    public static class HasElementsMatcher extends TypeSafeDiagnosingMatcher<DoubleStream> {

        @Override
        protected boolean matchesSafely(DoubleStream stream, Description mismatchDescription) {
            mismatchDescription.appendText("DoubleStream was empty");
            return stream.count() > 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a non-empty stream");
        }
    }

    public static class ElementsMatcher extends TypeSafeDiagnosingMatcher<DoubleStream> {

        private final Matcher<Double[]> matcher;
        private Double[] streamElements;

        public ElementsMatcher(Matcher<Double[]> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(DoubleStream stream, Description mismatchDescription) {
            final Double[] elements;
            if (streamElements == null) {
                elements = stream.boxed().toArray(BOXED_ARRAY_CREATOR);
                streamElements = elements;
            } else {
                elements = streamElements;
            }
            if (!matcher.matches(elements)) {
                mismatchDescription.appendText("DoubleStream elements ");
                matcher.describeMismatch(elements, mismatchDescription);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("DoubleStream elements ").appendDescriptionOf(matcher);
        }
    }

    private static final IntFunction<Double[]> BOXED_ARRAY_CREATOR = new IntFunction<Double[]>() {
        @Override
        public Double[] apply(int value) {
            return new Double[value];
        }
    };
}
