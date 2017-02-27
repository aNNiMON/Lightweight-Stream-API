package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.IntFunction;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.elements;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class MapToObjTest {

    @Test
    public void testMapToObj() {
        Stream<String> stream = IntStream.rangeClosed(2, 4)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int value) {
                        return Integer.toString(value);
                    }
                });
        List<String> expected = Arrays.asList("2", "3", "4");
        assertThat(stream, elements(is(expected)));
    }
}
