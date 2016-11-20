package com.annimon.stream;

/**
 * A sequence of {@code long}-valued elements supporting aggregate operations.
 *
 * @since 1.1.4
 * @see Stream
 */
@SuppressWarnings("WeakerAccess")
public final class LongStream {

    /**
     * Returns an empty stream.
     *
     * @return the empty stream
     */
    public static LongStream empty() {
        LongStream s = new LongStream();
        s.count = 0;
        return s;
    }

    /**
     * Returns stream which contains single element passed as param
     *
     * @param t  element of the stream
     * @return the new stream
     */
    public static LongStream of(final long t) {
        LongStream s = new LongStream();
        s.count = 1;
        return s;
    }

    private long count = 0;

    public long count() {
        return count;
    }
}
