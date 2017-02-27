package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.Arrays;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.is;

public final class FilterNotTest {

    @Test
    public void testFilterNot() {
        Stream.range(0, 10)
                .filterNot(Functions.remainder(2))
                .custom(assertElements(is(Arrays.asList(
                      1, 3, 5, 7, 9
                ))));
    }
}
