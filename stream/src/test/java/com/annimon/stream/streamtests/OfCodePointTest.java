package com.annimon.stream.streamtests;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OfCodePointTest {

    @Test
    public void testStreamOfCommonSequence() {
        String input = "hi";

        String result = Stream.ofCodePoints(input)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer i) {
                        return String.valueOf(Character.toChars(i));
                    }
                }).collect(Collectors.joining());

        assertThat(input, is(result));
    }

    @Test
    public void testStreamOfSequenceHavingFourBytesEmoji() {
        String input = "This is a emoji \uD83D\uDCA9!";

        String result = Stream.ofCodePoints(input)
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer i) {
                        return String.valueOf(Character.toChars(i));
                    }
                }).collect(Collectors.joining());

        assertThat(input, is(result));
    }
}
