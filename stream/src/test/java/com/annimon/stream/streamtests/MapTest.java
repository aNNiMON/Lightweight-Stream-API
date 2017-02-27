package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class MapTest {

    @Test
    public void testMapIntToSqrtString() {
        Function<Number, String> intToSqrtString = new Function<Number, String>() {
            @Override
            public String apply(Number t) {
                return String.format("[%d]", (int) Math.sqrt(t.intValue()));
            }
        };
        Stream.of(4, 9, 16, 64, 625)
                .map(intToSqrtString)
                .custom(assertElements(contains(
                      "[2]", "[3]", "[4]", "[8]", "[25]"
                )));
    }

    @Test
    public void testMapStringToSquareInt() {
        final Function<String, Integer> stringToSquareInt = new Function<String, Integer>() {
            @Override
            public Integer apply(String t) {
                final String str = t.substring(1, t.length() - 1);
                final int value = Integer.parseInt(str);
                return value * value;
            }
        };
        Stream.of("[2]", "[3]", "[4]", "[8]", "[25]")
                .map(stringToSquareInt)
                .custom(assertElements(contains(
                      4, 9, 16, 64, 625
                )));
    }

    @Test
    public void testMapWithComposedFunction() {
        final Function<Integer, Integer> mapPlus1 = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x + 1;
            }
        };
        final Function<Integer, Integer> mapPlus2 = Function.Util
                .compose(mapPlus1, mapPlus1);
        Stream.range(-10, 0)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .map(mapPlus2)
                .custom(assertElements(contains(
                      0, 1, 2, 3, 4, 5, 6, 7, 8, 9
                )));
    }
}
