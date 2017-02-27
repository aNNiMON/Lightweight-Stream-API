package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class PeekTest {

    @Test
    public void testPeek() {
        final List<Integer> result = new ArrayList<Integer>();
        long count = Stream.range(0, 5)
                .peek(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer t) {
                        result.add(t);
                    }
                })
                .count();
        assertEquals(5, count);
        assertThat(result, contains(
                0, 1, 2, 3, 4
        ));
    }
}
