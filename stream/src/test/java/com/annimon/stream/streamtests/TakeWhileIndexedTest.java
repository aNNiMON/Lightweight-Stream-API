package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.IndexedPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class TakeWhileIndexedTest {

    @Test
    public void testTakeWhileIndexed() {
        Stream.of(1, 2, 3,  4, -5, -6, -7)
                .takeWhileIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index + value < 5;
                    }
                })
                .custom(assertElements(contains(
                        1, 2
                )));
    }

    @Test
    public void testTakeWhileIndexedWithStartAndStep() {
        Stream.of(1, 2, 3,  4, -5, -6, -7)
                .takeWhileIndexed(2, 2, new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index + value < 8;
                    }
                })
                .custom(assertElements(contains(
                        1, 2
                )));
    }
}
