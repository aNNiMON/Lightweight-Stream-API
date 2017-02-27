package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.is;

public final class DropWhileTest {

    @Test
    public void testDropWhile() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainder(2))
                .custom(assertElements(is(Arrays.asList(
                        7, 8, 10, 11
                ))));
    }

    @Test
    public void testDropWhileNonFirstMatch() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainder(3))
                .custom(assertElements(is(Arrays.asList(
                        2, 4, 6, 7, 8, 10, 11
                ))));
    }

    @Test
    public void testDropWhileAllMatch() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainder(1))
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }
}
