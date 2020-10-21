package com.annimon.stream.test.hamcrest;

import org.hamcrest.Matcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.description;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CommonMatcherTest {

    @Test
    public void testPrivateConstructor() {
        assertThat(CommonMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testHasOnlyPrivateConstructors() {
        assertThat(CommonMatcherTest.class, not(hasOnlyPrivateConstructors()));

        Matcher<?> matcher = hasOnlyPrivateConstructors();
        assertThat(matcher, description(is("has only private constructors")));
    }

    @Test
    public void testDescription() {
        Matcher<?> matcher = description(is("test"));
        assertThat(matcher, description(allOf(
                containsString("description is"),
                containsString("test")
        )));

        assertThat(matcher, description(not(equalTo("test"))));
    }
}
