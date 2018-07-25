package com.annimon.stream.test.hamcrest;

import com.annimon.stream.OptionalDouble;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import static org.hamcrest.CoreMatchers.is;

public class OptionalDoubleMatcher {

    private OptionalDoubleMatcher() { }

    public static Matcher<OptionalDouble> isPresent() {
        return new IsPresentMatcher();
    }

    public static Matcher<OptionalDouble> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static Matcher<OptionalDouble> hasValue(double value) {
        return hasValueThat(is(value));
    }

    public static Matcher<OptionalDouble> hasValueThat(Matcher<? super Double> matcher) {
        return new HasValueMatcher(matcher);
    }

    public static class IsPresentMatcher extends TypeSafeDiagnosingMatcher<OptionalDouble> {

        @Override
        protected boolean matchesSafely(OptionalDouble optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalDouble was empty");
            return optional.isPresent();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalDouble value should be present");
        }
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<OptionalDouble> {

        @Override
        protected boolean matchesSafely(OptionalDouble optional, Description mismatchDescription) {
            mismatchDescription.appendText("OptionalDouble was present");
            return optional.isEmpty();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalDouble value should be empty");
        }
    }

    public static class HasValueMatcher extends TypeSafeDiagnosingMatcher<OptionalDouble> {

        private final Matcher<? super Double> matcher;

        public HasValueMatcher(Matcher<? super Double> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(OptionalDouble optional, Description mismatchDescription) {
            if (optional.isEmpty()) {
                mismatchDescription.appendText("OptionalDouble was empty");
                return false;
            }
            final Double value = optional.getAsDouble();
            mismatchDescription.appendText("OptionalDouble value ");
            matcher.describeMismatch(value, mismatchDescription);
            return matcher.matches(value);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("OptionalDouble value ").appendDescriptionOf(matcher);
        }
    }
}
