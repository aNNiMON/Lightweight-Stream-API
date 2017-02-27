package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import com.annimon.stream.function.DoublePredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class FilterNotTest {

    @Test
    public void testFilterNot() {
        final DoublePredicate predicate = Functions.greaterThan(Math.PI);
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).filterNot(predicate),
                elements(arrayContaining(0.012, 3.039)));

        assertThat(DoubleStream.of(4.096, 12).filterNot(predicate),
                isEmpty());
    }
}
