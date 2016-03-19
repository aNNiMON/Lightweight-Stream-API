package com.annimon.stream.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class CommonMatcher {

    private CommonMatcher() { }

    public static Matcher<Class<?>> hasOnlyPrivateConstructors() {
        return new PrivateConstructorsMatcher();
    }

    public static Matcher<Matcher> description(Matcher<String> matcher) {
        return new DescriptionMatcher(matcher);
    }

    public static class PrivateConstructorsMatcher extends BaseMatcher<Class<?>> {

        @Override
        public boolean matches(Object item) {
            final Class<?> clazz = (Class<?>) item;
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if (!Modifier.isPrivate(constructor.getModifiers())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("has only private constructors");
        }
    }

    public static class DescriptionMatcher<T> extends TypeSafeDiagnosingMatcher<Matcher<T>> {

        private final Matcher<String> matcher;

        public DescriptionMatcher(Matcher<String> matcher) {
            this.matcher = matcher;
        }

        @Override
        protected boolean matchesSafely(Matcher<T> m, Description mismatchDescription) {
            StringDescription stringDescription = new StringDescription();
            m.describeTo(stringDescription);
            final String description = stringDescription.toString();

            if (!matcher.matches(description)) {
                mismatchDescription.appendText("description ");
                matcher.describeMismatch(description, mismatchDescription);
                return false;
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("description ").appendDescriptionOf(matcher);
        }
    }
}
