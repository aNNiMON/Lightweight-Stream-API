package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class DistinctByTest {

    @Test
    public void testDistinct() {
        Stream.of("a", "bc", "d", "ef", "ghij")
                .distinctBy(stringLengthExtractor())
                .custom(assertElements(contains(
                        "a", "bc", "ghij"
                )));
    }

    @Test
    public void testDistinctEmpty() {
        Stream.<String>empty()
                .distinctBy(stringLengthExtractor())
                .custom(StreamMatcher.<String>assertIsEmpty());
    }

    private Function<String, Integer> stringLengthExtractor() {
        return new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };
    }
}
