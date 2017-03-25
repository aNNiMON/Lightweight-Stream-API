package com.annimon.stream.streamtests;

import com.annimon.stream.IntPair;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.IndexedPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isEmpty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public final class FindIndexed {

    @Test
    public void testFindIndexed() {
        IntPair<Integer> result = Stream.rangeClosed(1, 10)
                .findIndexed(sumEquals(7))
                .get();
        assertThat(result.getFirst(), is(3));
        assertThat(result.getSecond(), is(4));
    }

    @Test
    public void testFindIndexedWithStartAndStep() {
        IntPair<Integer> result = Stream.of(1, 11, 22, 12, 40)
                .findIndexed(0, 10, sumEquals(42))
                .get();
        assertThat(result.getFirst(), is(20));
        assertThat(result.getSecond(), is(22));
    }

    @Test
    public void testFindIndexedNoMatch() {
        Optional<IntPair<Integer>> result = Stream.range(0, 10)
                .findIndexed(sumEquals(42));
        assertThat(result, isEmpty());
    }


    private static IndexedPredicate<Integer> sumEquals(final int sum) {
        return new IndexedPredicate<Integer>() {
            @Override
            public boolean test(int index, Integer value) {
                return index + value == sum;
            }
        };
    }
}
