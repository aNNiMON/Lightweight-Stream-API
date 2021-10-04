package com.annimon.stream.streamtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public final class OfListTest {

    @Test
    public void testStreamOfList() {
        final List<String> list = new ArrayList<String>(4);
        list.add("This");
        list.add(" is ");
        list.add("a");
        list.add(" test");

        String result = Stream.of(list).collect(Collectors.joining());
        assertThat(result, is("This is a test"));
    }
}
