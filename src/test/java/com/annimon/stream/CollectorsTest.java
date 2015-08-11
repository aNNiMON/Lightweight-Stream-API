package com.annimon.stream;

import com.annimon.stream.function.Function;
import java.util.List;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class CollectorsTest {
    
    @Test
    public void toList() {
        List<Integer> list = Stream.ofRange(0, 10).collect(Collectors.<Integer>toList());
        assertEquals(10, list.size());
        int index = 0;
        for (int v : list) {
            assertEquals(index++, v);
        }
    }

    @Test
    public void toSet() {
        Set<Integer> set = Stream.of(1, 2, 2, 3, 3, 3).collect(Collectors.<Integer>toSet());
        assertEquals(3, set.size());
        int index = 1;
        for (int v : set) {
            assertEquals(index++, v);
        }
    }

    @Test
    public void joining() {
        String text = Stream.of("a", "b", "c", "def", "", "g")
                .collect(Collectors.joining());
        assertEquals("abcdefg", text);
    }

    @Test
    public void averaging() {
        double avg = Stream.of(10, 20, 30, 40)
                .collect(Collectors.averaging(new Function<Integer, Double>() {
                    @Override
                    public Double apply(Integer value) {
                        return value / 10d;
                    }
                }));
        assertEquals(2.5, avg, 0.001);
    }
}
