package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.IndexedPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class TakeUntilIndexedTest {
    
    @Test
    public void testTakeUntilIndexed() {
        Stream.of(1, 2, 3, 4, 0, 1, 2)
                .takeUntilIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return (index + value) > 4;
                    }
                })
                .custom(assertElements(contains(
                        1, 2, 3
                )));
    }

    @Test
    public void testTakeUntilIndexedWithStartAndStep() {
        Stream.of(1, 2, 3, 4, 0, 1, 2)
                .takeUntilIndexed(2, 2, new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return (index + value) > 8;
                    }
                })
                .custom(assertElements(contains(
                        1, 2, 3
                )));
    }
}
