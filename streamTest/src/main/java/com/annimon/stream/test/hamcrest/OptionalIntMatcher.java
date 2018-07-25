package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalInt;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class OptionalIntMatcher {

    private OptionalIntMatcher() { }

    public static Matcher<OptionalInt> isPresent() {
        return new IsPresentMatcher();
    }

    public static Matcher<OptionalInt> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<OptionalInt> hasValue(int value) {
        return hasValueThat(is(value));
    }

    public static Matcher<OptionalInt> hasValueThat(Matcher<? super Integer> matcher) {
        return new HasValueMatcher(matcher);
    }

    public static class IsPresentMatcher extends TypeSafeDiagnosingMatcher<OptionalInt> {

        @Override
        protected boolean matchesSafely(OptionalInt optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalInt was empty");
            return optional.isPresent();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalInt value should be present");
        }
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<OptionalInt> {

        @Override
        protected boolean matchesSafely(OptionalInt optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalInt was present");
            return optional.isEmpty();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalInt value should be empty");
        }
    }

    public static class HasValueMatcher extends TypeSafeDiagnosingMatcher<OptionalInt> {

        private final Matcher<? super Integer> matcher;

        public HasValueMatcher(Matcher<? super Integer> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(OptionalInt optional, Description mismatchDescription) {
            if (optional.isEmpty()) {
                mismatchDescription.appendText("OptionalInt was empty");
                return false;
            }
            final Integer value = optional.getAsInt();
            mismatchDescription.appendText("OptionalInt value ");
            matcher.describeMismatch(value, mismatchDescription);
            return matcher.matches(value);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalInt value ").appendDescriptionOf(matcher);
        }
    }
}
