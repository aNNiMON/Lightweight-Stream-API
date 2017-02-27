package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class FlatMapTest {

    @Test
    public void testFlatMap() {
        Stream.rangeClosed(2, 4)
                .flatMap(new Function<Integer, Stream<String>>() {

                    @Override
                    public Stream<String> apply(final Integer i) {
                        return Stream.rangeClosed(2, 4)
                                .filter(Functions.remainder(2))
                                .map(new Function<Integer, String>() {

                                    @Override
                                    public String apply(Integer p) {
                                        return String.format("%d * %d = %d", i, p, (i*p));
                                    }
                                });
                    }
                })
                .custom(assertElements(contains(
                        "2 * 2 = 4",
                        "2 * 4 = 8",
                        "3 * 2 = 6",
                        "3 * 4 = 12",
                        "4 * 2 = 8",
                        "4 * 4 = 16"
                )));
    }
}
