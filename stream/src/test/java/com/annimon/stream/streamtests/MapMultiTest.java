package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class MapMultiTest {

    @Test
    public void testMapMulti() {
        Stream.rangeClosed(2, 4)
                .mapMulti(new BiConsumer<Integer, Consumer<String>>() {
                    @Override
                    public void accept(Integer i, Consumer<String> consumer) {
                        consumer.accept(String.format("%d * 2 = %d", i, (i*2)));
                        consumer.accept(String.format("%d * 4 = %d", i, (i*4)));
                    }
                })
                .custom(assertElements(contains(
                        "2 * 2 = 4",
                        "2 * 4 = 8",
                        "3 * 2 = 6",
                        "3 * 4 = 12",
                        "4 * 2 = 8",
                        "4 * 4 = 16"
                )));
    }

    @Test
    public void testMapMultiEmpty() {
        Stream.rangeClosed(2, 4)
                .mapMulti(new BiConsumer<Integer, Consumer<String>>() {
                    @Override
                    public void accept(Integer i, Consumer<String> consumer) {
                    }
                })
                .custom(StreamMatcher.<String>assertIsEmpty());
    }
}
