package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class DistinctTest {

    @Test
    public void testDistinct() {
        DoubleStream.of(0.09, 1.2, 0, 2.2, 0.09, 1.2, 3.2, 0.09)
                .distinct()
                .custom(assertElements(arrayContaining(
                        0.09, 1.2, 0d, 2.2, 3.2
                )));
    }
}
