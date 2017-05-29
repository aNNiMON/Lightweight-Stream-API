package com.annimon.stream.intstreamtests;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntFunction;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OfCodePointTest {

    @Test
    public void testCommonSequence() {
        String input = "hi";

        String result = IntStream.ofCodePoints(input)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int i) {
                        return String.valueOf(Character.toChars(i));
                    }
                }).collect(Collectors.joining());

        assertThat(input, is(result));
    }

    @Test
    public void testSequenceHavingFourBytesEmoji() {
        String input = "This is a emoji \uD83D\uDCA9!";

        String result = IntStream.ofCodePoints(input)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int i) {
                        return String.valueOf(Character.toChars(i));
                    }
                }).collect(Collectors.joining());

        assertThat(input, is(result));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() {
        PrimitiveIterator.OfInt iterator = IntStream.ofCodePoints("test").iterator();
        iterator.nextInt();
        iterator.nextInt();
        iterator.nextInt();
        iterator.nextInt();
        iterator.nextInt();
    }

    @Test
    public void testStringBuilder() {
        final String input = "test";

        final StringBuilder sb = new StringBuilder(input);
        String result = IntStream.ofCodePoints(sb)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int i) {
                        return String.valueOf(Character.toChars(i));
                    }
                }).collect(Collectors.joining());

        assertThat(input, is(result));
    }
}
