package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class TakeUntilTest {

    @Test
    public void testTakeUntil() {
        Stream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Predicate.Util.negate(Functions.remainder(2)))
                .custom(assertElements(contains(
                        2, 4, 6, 7
                )));
    }
    
    @Test
    public void testTakeUntilOnEmptyStream() {
        Stream.<Integer>empty()
                .takeUntil(Functions.remainder(2))
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }
}
