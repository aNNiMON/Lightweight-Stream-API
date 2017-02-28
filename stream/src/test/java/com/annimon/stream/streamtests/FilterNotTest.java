package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class FilterNotTest {

    @Test
    public void testFilterNot() {
        Stream.range(0, 10)
                .filterNot(Functions.remainder(2))
                .custom(assertElements(contains(
                      1, 3, 5, 7, 9
                )));
    }
}
