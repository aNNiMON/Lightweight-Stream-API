package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import com.annimon.stream.OptionalDouble;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindSingleTest {

    @Test
    public void testFindSingleOnEmptyStream() {
        assertThat(DoubleStream.empty().findSingle(),
                isEmpty());
    }

    @Test
    public void testFindSingleOnOneElementStream() {
        assertThat(DoubleStream.of(42d).findSingle(), hasValue(42d));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleOnMoreElementsStream() {
        DoubleStream.of(1, 2).findSingle();
    }

    @Test
    public void testFindSingleAfterFilteringToEmptyStream() {
        OptionalDouble result = DoubleStream.of(0, 1, 2)
                .filter(Functions.greaterThan(Math.PI))
                .findSingle();

        assertThat(result, isEmpty());
    }

    @Test
    public void testFindSingleAfterFilteringToOneElementStream() {
        OptionalDouble result = DoubleStream.of(1.0, 10.12, -3.01)
                .filter(Functions.greaterThan(Math.PI))
                .findSingle();

        assertThat(result, hasValue(10.12));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleAfterFilteringToMoreElementStream() {
        DoubleStream.of(1.0, 10.12, -3.01, 6.45)
                .filter(Functions.greaterThan(Math.PI))
                .findSingle();
    }
}
