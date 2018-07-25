package com.annimon.stream.test.hamcrest;

import com.annimon.stream.Optional;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class OptionalMatcher {

    private OptionalMatcher() { }

    public static Matcher<Optional<?>> isPresent() {
        return new IsPresentMatcher();
    }

    public static Matcher<Optional<?>> isEmpty() {
        return new IsEmptyMatcher();
    }

    public static <T> Matcher<Optional<T>> hasValue(T object) {
        return hasValueThat(is(object));
    }

    public static <T> Matcher<Optional<T>> hasValueThat(Matcher<? super T> matcher) {
        return new HasValueMatcher<T>(matcher);
    }

    public static class IsPresentMatcher extends TypeSafeDiagnosingMatcher<Optional<?>> {

        @Override
        protected boolean matchesSafely(Optional<?> optional, Description mismatchDescription) {
            mismatchDescription.appendText("Optional was empty");
            return optional.isPresent();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Optional value should be present");
        }
    }

    public static class IsEmptyMatcher extends TypeSafeDiagnosingMatcher<Optional<?>> {

        @Override
        protected boolean matchesSafely(Optional<?> optional, Description mismatchDescription) {
            mismatchDescription.appendText("Optional was present");
            return optional.isEmpty();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Optional value should be empty");
        }
    }

    public static class HasValueMatcher<T> extends TypeSafeDiagnosingMatcher<Optional<T>> {

        private final Matcher<? super T> matcher;

        public HasValueMatcher(Matcher<? super T> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(Optional<T> optional, Description mismatchDescription) {
            if (optional.isEmpty()) {
                mismatchDescription.appendText("Optional was empty");
                return false;
            }
            final T value = optional.get();
            mismatchDescription.appendText("Optional value ");
            matcher.describeMismatch(value, mismatchDescription);
            return matcher.matches(value);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Optional value ").appendDescriptionOf(matcher);
        }
    }
}
