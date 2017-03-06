package com.annimon.stream.streamtests;

import com.annimon.stream.CustomOperators;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class CustomTest {

    @Test(expected = NullPointerException.class)
    public void testCustomNull() {
        Stream.empty().custom(null);
    }

    @Test
    public void testCustomIntermediateOperator_Reverse() {
        Stream.range(0, 10)
                .custom(new CustomOperators.Reverse<Integer>())
                .custom(assertElements(contains(
                      9, 8, 7, 6, 5, 4, 3, 2, 1, 0
                )));
    }

    @Test
    public void testCustomIntermediateOperator_SkipAndLimit() {
        Stream.range(0, 10)
                .custom(new CustomOperators.SkipAndLimit<Integer>(5, 2))
                .custom(assertElements(contains(
                        5, 6
                )));
    }

    @Test
    public void testCustomIntermediateOperator_FlatMapAndCast() {
        List<List> lists = new ArrayList<List>();
        for (char ch = 'a'; ch <= 'f'; ch++) {
            lists.add( new ArrayList<Character>(Arrays.asList(ch)) );
        }
        Stream.of(lists)
                .custom(new CustomOperators.FlatMap<List, Object>(new Function<List, Stream<Object>>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Stream<Object> apply(List value) {
                        return Stream.of(value);
                    }
                }))
                .custom(new CustomOperators.Cast<Object, Character>(Character.class))
                .custom(assertElements(contains('a', 'b', 'c', 'd', 'e', 'f')));
    }

    @Test
    public void testCustomTerminalOperator_Sum() {
        int sum = Stream.of(1, 2, 3, 4, 5)
                .custom(new CustomOperators.Sum());
        assertEquals(15, sum);
    }

    @Test
    public void testCustomTerminalOperator_ForEach() {
        final List<Integer> list = new ArrayList<Integer>();
        Stream.range(0, 10)
                .custom(new CustomOperators.ForEach<Integer>(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer t) {
                        list.add(t);
                    }
                }));

        assertThat(list, contains(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    }
}
