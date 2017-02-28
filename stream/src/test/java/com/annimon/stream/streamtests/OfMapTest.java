package com.annimon.stream.streamtests;

import com.annimon.stream.Collectors;
import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class OfMapTest {

    @Test
    public void testStreamOfMap() {
        final Map<String, Integer> map = new HashMap<String, Integer>(4);
        map.put("This", 1);
        map.put(" is ", 2);
        map.put("a", 3);
        map.put(" test", 4);

        String result = Stream.of(map)
                .sortBy(Functions.<String, Integer>entryValue())
                .map(Functions.<String, Integer>entryKey())
                .collect(Collectors.joining());
        assertThat(result, is("This is a test"));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfMapNull() {
        Stream.of((Map<?, ?>)null);
    }
}
