package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.BiFunction;
import com.annimon.stream.operator.ObjMerge;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class MergeTest {

    @Test
    public void testMerge() {
        Stream<Integer> shorter = Stream.rangeClosed(1, 5);
        Stream<Integer> longer = Stream.rangeClosed(2, 10);
        Stream.merge(shorter, longer, selectorFunction())
                .custom(assertElements(contains(
                      1, 3, 4, 6
                )));
    }

    @Test
    public void testMergeSkipAll() {
        Stream<Integer> shorter = Stream.rangeClosed(1, 5);
        Stream<Integer> longer = Stream.rangeClosed(2, 10);
        Stream.merge(
                shorter, longer,
                new BiFunction<Integer, Integer, ObjMerge.MergeResult>() {
                    @Override
                    public ObjMerge.MergeResult apply(Integer value1, Integer value2) {
                        return ObjMerge.MergeResult.SKIP;
                    }
                })
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test
    public void testMergeOnOneEmptyStream() {
        Stream<Integer> stream = Stream.rangeClosed(1, 5);
        Stream<Integer> emptyStream = Stream.<Integer>empty();
        Stream.merge(stream, emptyStream, selectorFunction())
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test
    public void testMergeOnBothEmptyStreams() {
        Stream<Integer> emptyStream1 = Stream.<Integer>empty();
        Stream<Integer> emptyStream2 = Stream.<Integer>empty();
        Stream.merge(emptyStream1, emptyStream2, selectorFunction())
                .custom(StreamMatcher.<Integer>assertIsEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNull1() {
        Stream.merge(null, Stream.<Integer>empty(), selectorFunction());
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNull2() {
        Stream.merge(Stream.<Integer>empty(), null, selectorFunction());
    }

    @Test
    public void testMergeIterator() {
        List<Integer> shorter = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> longer = Stream.rangeClosed(2, 10);
        Stream.merge(shorter.iterator(), longer.iterator(), selectorFunction())
                .custom(assertElements(contains(
                      1, 3, 4, 6
                )));
    }

    private BiFunction<Integer, Integer, ObjMerge.MergeResult> selectorFunction() {
        return new BiFunction<Integer, Integer, ObjMerge.MergeResult>() {
            @Override
            public ObjMerge.MergeResult apply(Integer value1, Integer value2) {
                switch (value1 % 3) {
                    case 0:
                        return ObjMerge.MergeResult.SKIP;
                    case 1:
                        return ObjMerge.MergeResult.FIRST;
                    case 2:
                    default:
                        return ObjMerge.MergeResult.SECOND;
                }
            }
        };
    }
}
