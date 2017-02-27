package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class SelectTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testSelect() {
        Stream.of(1, "a", 2, "b", 3, "cc").select(String.class)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String value) {
                        return value.length() == 1;
                    }
                })
                .custom(assertElements(contains(
                      "a", "b"
                )));
    }
}
