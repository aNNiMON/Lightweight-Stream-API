package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class FilterNotTest {

    @Test
    public void testFilterNot() {
        IntStream.rangeClosed(1, 10)
                .filterNot(Functions.remainderInt(2))
                .custom(assertElements(arrayContaining(
                        1, 3, 5, 7, 9
                )));
    }
}
