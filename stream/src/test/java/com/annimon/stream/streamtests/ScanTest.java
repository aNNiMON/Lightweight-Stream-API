package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class ScanTest {

    @Test
    public void testScan() {
        Stream.of(1, 2, 3)
                .scan(Functions.addition())
                .custom(assertElements(contains(
                        1, 3, 6
                )));
    }

    @Test
    public void testScanNonAssociative() {
        Stream.of(1800, 2, 3, 5)
                .scan(new BinaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer value1, Integer value2) {
                        return value1 / value2;
                    }
                })
                .custom(assertElements(contains(
                        1800,
                        1800 / 2,
                        1800 / 2 / 3,
                        1800 / 2 / 3 / 5
                )));
    }

    @Test
    public void testScanOnEmptyStream() {
        Stream.<Integer>empty()
                .scan(Functions.addition())
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test
    public void testScanWithIdentity() {
        Stream.of("a", "bb", "ccc", "dddd")
                .scan(0, new BiFunction<Integer, String, Integer>() {
                    @Override
                    public Integer apply(Integer length, String s) {
                        return length + s.length();
                    }
                })
                .custom(assertElements(contains(
                        0, 1, 3, 6, 10
                )));
    }

    @Test
    public void testScanWithIdentityOnEmptyStream() {
        Stream.<Integer>empty()
                .scan(9, Functions.addition())
                .custom(assertElements(contains(
                        9
                )));
    }
}
