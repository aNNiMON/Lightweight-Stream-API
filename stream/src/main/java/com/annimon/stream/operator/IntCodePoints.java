package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;

public class IntCodePoints extends PrimitiveIterator.OfInt {

    private final CharSequence charSequence;
    private final boolean isString;
    private int current;
    private int length;

    public IntCodePoints(CharSequence charSequence) {
        this.charSequence = charSequence;
        isString = (charSequence instanceof String);
        current = 0;
        length = -1;
    }

    @Override
    public boolean hasNext() {
        return current < ensureLength();
    }
    
    @Override
    public int nextInt() {
        final int length = ensureLength();
        if (current >= length) {
            throw new NoSuchElementException();
        }

        final char nextChar = charSequence.charAt(current++);
        if (Character.isHighSurrogate(nextChar) && current < length) {
            final char currentChar = charSequence.charAt(current);
            if (Character.isLowSurrogate(currentChar)) {
                current++;
                return Character.toCodePoint(nextChar, currentChar);
            }
        }
        return nextChar;
    }

    private int ensureLength() {
        if (isString) {
            if (length == -1) {
                length = charSequence.length();
            }
            return length;
        }
        return charSequence.length();
    }
}
