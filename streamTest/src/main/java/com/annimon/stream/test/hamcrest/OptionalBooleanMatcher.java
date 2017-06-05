package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalBoolean;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import static org.hamcrest.CoreMatchers.is;

public class OptionalBooleanMatcher {

    private OptionalBooleanMatcher() { }

    public static Matcher<OptionalBoolean> isPresent() {
        return new IsPresentMatcher();
    }

    public static Matcher<OptionalBoolean> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<OptionalBoolean> hasValue(boolean value) {
        return hasValueThat(is(value));
    }

    public static Matcher<OptionalBoolean> hasValueThat(Matcher<? super Boolean> matcher) {
        return new HasValueMatcher(matcher);
    }

    public static class IsPresentMatcher extends TypeSafeDiagnosingMatcher<OptionalBoolean> {

        @Override
        protected boolean matchesSafely(OptionalBoolean optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalBoolean was empty");
            return optional.isPresent();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalBoolean value should be present");
        }
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<OptionalBoolean> {

        @Override
        protected boolean matchesSafely(OptionalBoolean optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalBoolean was present");
            return !optional.isPresent();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalBoolean value should be empty");
        }
    }

    public static class HasValueMatcher extends TypeSafeDiagnosingMatcher<OptionalBoolean> {

        private final Matcher<? super Boolean> matcher;

        public HasValueMatcher(Matcher<? super Boolean> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(OptionalBoolean optional, Description mismatchDescription) {
            if (!optional.isPresent()) {
                mismatchDescription.appendText("OptionalBoolean was empty");
                return false;
            }
            final Boolean value = optional.getAsBoolean();
            mismatchDescription.appendText("OptionalBoolean value ");
            matcher.describeMismatch(value, mismatchDescription);
            return matcher.matches(value);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalBoolean value ").appendDescriptionOf(matcher);
        }
    }
}
