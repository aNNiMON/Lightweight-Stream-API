package com.annimon.stream.test.hamcrest;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.IntFunction;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class LongStreamMatcher {

    private LongStreamMatcher() { }

    public static Matcher<LongStream> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<LongStream> hasElements() {
        return new HasElementsMatcher();
    }

    public static Matcher<LongStream> elements(Matcher<Long[]> matcher) {
        return new ElementsMatcher(matcher);
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<LongStream> {

        @Override
        protected boolean matchesSafely(LongStream stream, Description mismatchDescription) {
            mismatchDescription.appendText("LongStream was not empty");
            return stream.count() == 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("an empty stream");
        }
    }

    public static class HasElementsMatcher extends TypeSafeDiagnosingMatcher<LongStream> {

        @Override
        protected boolean matchesSafely(LongStream stream, Description mismatchDescription) {
            mismatchDescription.appendText("LongStream was empty");
            return stream.count() > 0;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a non-empty stream");
        }
    }

    public static class ElementsMatcher extends TypeSafeDiagnosingMatcher<LongStream> {

        private final Matcher<Long[]> matcher;
        private Long[] streamElements;

        public ElementsMatcher(Matcher<Long[]> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(LongStream stream, Description mismatchDescription) {
            final Long[] elements;
            if (streamElements == null) {
                elements = stream.boxed().toArray(BOXED_ARRAY_CREATOR);
                streamElements = elements;
            } else {
                elements = streamElements;
            }
            if (!matcher.matches(elements)) {
                mismatchDescription.appendText("LongStream elements ");
                matcher.describeMismatch(elements, mismatchDescription);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("LongStream elements ").appendDescriptionOf(matcher);
        }
    }

    private static final IntFunction<Long[]> BOXED_ARRAY_CREATOR = new IntFunction<Long[]>() {
        @Override
        public Long[] apply(int value) {
            return new Long[value];
        }
    };
}
