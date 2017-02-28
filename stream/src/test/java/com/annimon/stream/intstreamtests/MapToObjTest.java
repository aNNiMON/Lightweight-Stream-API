package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntFunction;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class MapToObjTest {

    @Test
    public void testMapToObj() {
        IntStream.rangeClosed(2, 4)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int value) {
                        return Integer.toString(value);
                    }
                })
                .custom(assertElements(contains(
                        "2", "3", "4"
                )));
    }
}
