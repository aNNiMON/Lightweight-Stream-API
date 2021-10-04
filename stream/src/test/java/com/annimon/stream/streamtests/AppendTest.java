package com.annimon.stream.streamtests;

import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;

public final class AppendTest {

    @Test
    public void testAppendEmptyStream() {
        Stream.<String>empty()
                .append(Stream.<String>empty())
                .custom(StreamMatcher.<String>assertIsEmpty());

        Stream.of(1, 2).append(Stream.<Integer>empty()).custom(assertElements(contains(1, 2)));
    }

    @Test
    public void testAppend() {
        Stream.<Integer>empty().append(Stream.of(0)).custom(assertElements(contains(0)));
        Stream.of(1, 2).append(Stream.of(0)).custom(assertElements(contains(1, 2, 0)));
    }

    @Test
    public void testAppendAfterFiltering() {
        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainder(2))
                .append(Stream.of(1))
                .custom(assertElements(contains(2, 4, 6, 1)));
        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainder(2))
                .append(Stream.of(1, 2, 3, 4).filterNot(Functions.remainder(2)))
                .custom(assertElements(contains(2, 4, 6, 1, 3)));
    }

    @Test
    public void testManyAppends() {
        Stream.of(1)
                .append(Stream.of(2))
                .append(Stream.of(3))
                .append(Stream.of(4))
                .custom(assertElements(contains(1, 2, 3, 4)));
    }

    @Test
    public void testManyAppendsComplex() {
        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainder(2))
                .append(Stream.of(2, 3, 4, 5))
                .filter(Functions.remainder(2))
                .limit(4)
                .append(Stream.of(6, 7, 8, 9, 10).filter(Functions.remainder(2)))
                .custom(assertElements(contains(2, 4, 6, 2, 6, 8, 10)));
    }
}
