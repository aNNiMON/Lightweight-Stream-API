package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class WithoutNullsTest {

     @Test
    public void testWithoutNulls() {
        Stream.range(0, 10)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) {
                        return integer % 3 == 0 ? null : integer.toString();
                    }
                })
                .withoutNulls()
                .custom(assertElements(contains(
                        "1", "2", "4", "5", "7", "8"
                )));
    }
}
