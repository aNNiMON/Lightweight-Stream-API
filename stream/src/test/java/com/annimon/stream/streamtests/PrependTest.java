package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class PrependTest {

    @Test
    public void testPrependEmptyStream() {
        Stream.<String>empty()
                .prepend(Stream.<String>empty())
                .custom(StreamMatcher.<String>assertIsEmpty());

        Stream.of(1, 2)
                .prepend(Stream.<Integer>empty())
                .custom(assertElements(contains(
                        1, 2
                )));
    }

    @Test
    public void testPrepend() {
        Stream.<Integer>empty()
                .prepend(Stream.of(0))
                .custom(assertElements(contains(
                        0
                )));
        Stream.of(1, 2)
                .prepend(Stream.of(0))
                .custom(assertElements(contains(
                        0, 1, 2
                )));
    }

    @Test
    public void testPrependAfterFiltering() {
        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainder(2))
                .prepend(Stream.of(1))
                .custom(assertElements(contains(
                        1, 2, 4, 6
                )));
        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainder(2))
                .prepend(Stream.of(1, 2, 3, 4).filterNot(Functions.remainder(2)))
                .custom(assertElements(contains(
                        1, 3, 2, 4, 6
                )));
    }

    @Test
    public void testManyPrepends() {
        Stream.of(1)
                .prepend(Stream.of(2))
                .prepend(Stream.of(3))
                .prepend(Stream.of(4))
                .custom(assertElements(contains(
                        4, 3, 2, 1
                )));
    }

    @Test
    public void testManyPrependsComplex() {
        Stream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainder(2))
                .prepend(Stream.of(2, 3, 4, 5))
                .filter(Functions.remainder(2))
                .limit(4)
                .prepend(Stream.of(6, 7, 8, 9, 10).filter(Functions.remainder(2)))
                .custom(assertElements(contains(
                        6, 8, 10, 2, 4, 2, 4
                )));
    }
}
