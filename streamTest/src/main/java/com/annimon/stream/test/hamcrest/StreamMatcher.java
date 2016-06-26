package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;

public class StreamMatcher {

    private StreamMatcher() { }

    public static Matcher<Stream<?>> isEmpty() {
        return new IsEmptyMatcher();
    }

    /**
     * @deprecated Use not({@link StreamMatcher#isEmpty()}) or {@link StreamMatcher#hasElements()} instead
     */
    @Deprecated
    public static Matcher<Stream<?>> isNotEmpty() {
        return not(isEmpty());
    }

    public static Matcher<Stream<?>> hasElements() {
        return new HasElementsMatcher();
    }

    public static <T> Matcher<Stream<T>> elements(Matcher<List<T>> matcher) {
        return new ElementsMatcher<T>(matcher);
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

        private final Matcher<List<T>> matcher;
        private List<T> streamElements;

        public ElementsMatcher(Matcher<List<T>> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(Stream<T> stream, Description mismatchDescription) {
            final List<T> elements;
            if (streamElements == null) {
                elements = stream.collect(Collectors.<T>toList());
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
