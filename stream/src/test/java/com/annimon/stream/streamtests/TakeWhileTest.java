package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(2))
                .custom(assertElements(contains(
                        2, 4, 6
                )));
    }
    
    @Test
    public void testTakeWhileNonFirstMatch() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(3))
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainder(1))
                .custom(assertElements(contains(
                        2, 4, 6, 7, 8, 10, 11
                )));
    }
}
