package com.annimon.stream.streamtests;

import com.annimon.stream.Objects;
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
    public void testMerge1() {
        Stream<Integer> stream1 = Stream.of(1, 3, 8, 10);
        Stream<Integer> stream2 = Stream.of(2, 5, 6, 12);
        Stream.merge(stream1, stream2, selectorFunction())
                .custom(assertElements(contains(
                      1, 2, 3, 5, 6, 8, 10, 12
                )));
    }

    @Test
    public void testMerge2() {
        Stream<Integer> stream1 = Stream.of(2, 5, 6, 12);
        Stream<Integer> stream2 = Stream.of(1, 3, 8, 10);
        Stream.merge(stream1, stream2, selectorFunction())
                .custom(assertElements(contains(
                      1, 2, 3, 5, 6, 8, 10, 12
                )));
    }

    @Test
    public void testMergeAsConcat1() {
        Stream<Integer> stream1 = Stream.of(0, 3, 1);
        Stream<Integer> stream2 = Stream.of(2, 5, 6, 1);
        Stream.merge(stream1, stream2, only(ObjMerge.MergeResult.TAKE_FIRST))
                .custom(assertElements(contains(
                        0, 3, 1, 2, 5, 6, 1
                )));
    }

    @Test
    public void testMergeAsConcat2() {
        Stream<Integer> stream1 = Stream.of(0, 3, 1);
        Stream<Integer> stream2 = Stream.of(2, 5, 6, 1);
        Stream.merge(stream1, stream2, only(ObjMerge.MergeResult.TAKE_SECOND))
                .custom(assertElements(contains(
                        2, 5, 6, 1, 0, 3, 1
                )));
    }

    @Test
    public void testMergeOnFirstEmptyStream() {
        Stream<Integer> emptyStream = Stream.<Integer>empty();
        Stream<Integer> stream = Stream.rangeClosed(1, 5);
        Stream.merge(emptyStream, stream, selectorFunction())
                .custom(assertElements(contains(
                        1, 2, 3, 4, 5
                )));
    }

    @Test
    public void testMergeOnSecondEmptyStream() {
        Stream<Integer> stream = Stream.rangeClosed(1, 5);
        Stream<Integer> emptyStream = Stream.<Integer>empty();
        Stream.merge(stream, emptyStream, selectorFunction())
                .custom(assertElements(contains(
                        1, 2, 3, 4, 5
                )));
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
        List<Integer> longer = Stream.rangeClosed(2, 8).toList();
        Stream.merge(shorter.iterator(), longer.iterator(), selectorFunction())
                .custom(assertElements(contains(
                      1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 8
                )));
        Stream.merge(longer.iterator(), shorter.iterator(), selectorFunction())
                .custom(assertElements(contains(
                      1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 7, 8
                )));
    }

    private BiFunction<Integer, Integer, ObjMerge.MergeResult> selectorFunction() {
        return new BiFunction<Integer, Integer, ObjMerge.MergeResult>() {
            @Override
            public ObjMerge.MergeResult apply(Integer value1, Integer value2) {
                return Objects.compareInt(value1, value2) == -1
                        ? ObjMerge.MergeResult.TAKE_FIRST
                        : ObjMerge.MergeResult.TAKE_SECOND;
            }
        };
    }

    private BiFunction<Integer, Integer, ObjMerge.MergeResult> only(final ObjMerge.MergeResult result) {
        return new BiFunction<Integer, Integer, ObjMerge.MergeResult>() {
            @Override
            public ObjMerge.MergeResult apply(Integer value1, Integer value2) {
                return result;
            }
        };
    }
}
