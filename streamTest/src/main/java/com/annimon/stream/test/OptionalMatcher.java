package com.annimon.stream.test;

import com.annimon.stream.Optional;
import static org.hamcrest.CoreMatchers.not;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class OptionalMatcher {

    private OptionalMatcher() { }

    public static Matcher<Optional<?>> isPresent() {
        return new IsPresentMatcher();
    }

    public static Matcher<Optional<?>> isEmpty() {
        return not(isPresent());
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
}
