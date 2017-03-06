package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntPair;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class IndexedTest {

    @Test
    public void testIndexed() {
        int[] expectedIndices = new int[] {0, 1, 2, 3};
        int[] actualIndices = Stream.of("a", "b", "c", "d")
                .indexed()
                .mapToInt(Functions.<String>intPairIndex())
                .toArray();
        assertThat(actualIndices, is(expectedIndices));
    }

    @Test
    public void testIndexedCustomStep() {
        int[] expectedIndices = new int[] {-10, -15, -20, -25};
        int[] actualIndices = Stream.of("a", "b", "c", "d")
                .indexed(-10, -5)
                .mapToInt(Functions.<String>intPairIndex())
                .toArray();
        assertThat(actualIndices, is(expectedIndices));
    }

    @Test
    public void testIndexedReverse() {
        Stream.of("first", "second", "third", "fourth", "fifth")
                .indexed(0, -1)
                .sortBy(new Function<IntPair<String>, Integer>() {
                    @Override
                    public Integer apply(IntPair<String> t) {
                        return t.getFirst();
                    }
                })
                .map(new Function<IntPair<String>, String>() {

                    @Override
                    public String apply(IntPair<String> t) {
                        return t.getSecond();
                    }
                })
                .custom(assertElements(contains(
                        "fifth", "fourth", "third", "second", "first"
                )));
    }
}
