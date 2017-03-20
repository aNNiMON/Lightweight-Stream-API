package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import static org.hamcrest.MatcherAssert.assertThat;

public class StreamMatcher {

    private StreamMatcher() { }

    public static Matcher<Stream<?>> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<Stream<?>> hasElements() {
        return new HasElementsMatcher();
    }

    public static <T> Matcher<Stream<T>> elements(Matcher<Iterable<? extends T>> matcher) {
        return new ElementsMatcher<T>(matcher);
    }
    
    public static <T> Function<Stream<T>, Void> assertIsEmpty() {
        return new Function<Stream<T>, Void>() {

            @Override
            public Void apply(Stream<T> t) {
                assertThat(t, isEmpty());
                return null;
            }
        };
    }

    public static <T> Function<Stream<T>, Void> assertHasElements() {
        return new Function<Stream<T>, Void>() {

            @Override
            public Void apply(Stream<T> t) {
                assertThat(t, hasElements());
                return null;
            }
        };
    }

    public static <T> Function<Stream<T>, Void> assertElements(final Matcher<Iterable<? extends T>> matcher) {
        return new Function<Stream<T>, Void>() {

            @Override
            public Void apply(Stream<T> t) {
                assertThat(t, elements(matcher));
                return null;
            }
        };
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<Stream<?>> {

        @Override
        protected boolean matchesSafely(Stream<?> stream, Description mismatchDescription) {
            mismatchDescription.appendText("Stream was not empty");
            return stream.count() == 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("an empty stream");
        }
    }

    public static class HasElementsMatcher extends TypeSafeDiagnosingMatcher<Stream<?>> {

        @Override
        protected boolean matchesSafely(Stream<?> stream, Description mismatchDescription) {
            mismatchDescription.appendText("Stream was empty");
            return stream.count() > 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a non-empty stream");
        }
    }

    public static class ElementsMatcher<T> extends TypeSafeDiagnosingMatcher<Stream<T>> {

        private final Matcher<Iterable<? extends T>> matcher;
        private List<T> streamElements;

        public ElementsMatcher(Matcher<Iterable<? extends T>> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(Stream<T> stream, Description mismatchDescription) {
            final List<T> elements;
            if (streamElements == null) {
                elements = stream.toList();
                streamElements = elements;
            } else {
                elements = streamElements;
            }
            if (!matcher.matches(elements)) {
                mismatchDescription.appendText("Stream elements ");
                matcher.describeMismatch(elements, mismatchDescription);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Stream elements ").appendDescriptionOf(matcher);
        }
    }
}
