package com.annimon.stream.test;

import static com.annimon.stream.test.CommonMatcher.description;
import static com.annimon.stream.test.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import org.hamcrest.Matcher;
import static org.junit.Assert.*;
import org.junit.Test;

public class CommonMatcherTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(CommonMatcher.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testHasOnlyPrivateConstructors() {
        Matcher matcher = hasOnlyPrivateConstructors();
        assertThat(matcher, description(is("has only private constructors")));
    }

    @Test
    public void testDescription() {
        Matcher matcher = description(is("test"));
        assertThat(matcher, description(allOf(
                containsString("description is"),
                containsString("test")
        )));
    }
}
