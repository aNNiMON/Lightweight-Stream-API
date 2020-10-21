package com.annimon.stream.internal;

import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for {@link Operators}
*/
public class OperatorsTest {

    @Test
    public void testPrivateConstructor() {
        assertThat(Operators.class, hasOnlyPrivateConstructors());
    }
}
