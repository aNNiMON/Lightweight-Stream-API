package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.UnaryOperator;
import java.util.Arrays;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class ChunkByTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testChunkBy() {
        Stream.of(1, 1, 2, 2, 2, 3, 1)
                .chunkBy(UnaryOperator.Util.<Integer>identity())
                .custom(assertElements(contains(
                        Arrays.asList(1, 1),
                        Arrays.asList(2, 2, 2),
                        Arrays.asList(3),
                        Arrays.asList(1)
                )));
    }
}
