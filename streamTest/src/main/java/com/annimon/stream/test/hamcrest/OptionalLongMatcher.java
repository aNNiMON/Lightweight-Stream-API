package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalLong;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import static org.hamcrest.CoreMatchers.is;

public class OptionalLongMatcher {

    private OptionalLongMatcher() { }

    public static Matcher<OptionalLong> isPresent() {
        return new IsPresentMatcher();
    }

    public static Matcher<OptionalLong> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<OptionalLong> hasValue(long value) {
        return hasValueThat(is(value));
    }

    public static Matcher<OptionalLong> hasValueThat(Matcher<? super Long> matcher) {
        return new HasValueMatcher(matcher);
    }

    public static class IsPresentMatcher extends TypeSafeDiagnosingMatcher<OptionalLong> {

        @Override
        protected boolean matchesSafely(OptionalLong optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalLong was empty");
            return optional.isPresent();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalLong value should be present");
        }
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<OptionalLong> {

        @Override
        protected boolean matchesSafely(OptionalLong optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalLong was present");
            return optional.isEmpty();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalLong value should be empty");
        }
    }

    public static class HasValueMatcher extends TypeSafeDiagnosingMatcher<OptionalLong> {

        private final Matcher<? super Long> matcher;

        public HasValueMatcher(Matcher<? super Long> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(OptionalLong optional, Description mismatchDescription) {
            if (optional.isEmpty()) {
                mismatchDescription.appendText("OptionalLong was empty");
                return false;
            }
            final Long value = optional.getAsLong();
            mismatchDescription.appendText("OptionalLong value ");
            matcher.describeMismatch(value, mismatchDescription);
            return matcher.matches(value);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalLong value ").appendDescriptionOf(matcher);
        }
    }
}
