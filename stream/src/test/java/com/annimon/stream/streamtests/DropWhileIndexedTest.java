package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.IndexedPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class DropWhileIndexedTest {

    @Test
    public void testDropWhileIndexed() {
        Stream.of(1, 2, 3, 4, 0, 1, 2)
                .dropWhileIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return (index + value) < 5;
                    }
                })
                .custom(assertElements(contains(
                        3, 4, 0, 1, 2
                )));
    }

    @Test
    public void testDropWhileIndexedWithStartAndStep() {
        Stream.of(1, 2, 3, 4, -5, -6, -7)
                .dropWhileIndexed(2, 2, new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return (index + value) < 10;
                    }
                })
                .custom(assertElements(contains(
                        4, -5, -6, -7
                )));
    }
}
