package com.annimon.stream;

import com.annimon.stream.function.IntSupplier;

import java.util.Random;

/**
 * Backported stream apis from {@link java.util.Random} class,
 * based on {@link IntStream} only for now.
 */
@SuppressWarnings("WeakerAccess")
public final class RandomCompat {

    private final Random random;

    /**
     * Constructs object, inner {@code random} created with default constructor
     */
    public RandomCompat() {
        this.random = new Random();
    }

    /**
     * Constructs object, inner {@code random} created with seed passed as param
     *
     * @param seed seed to initialize {@code random} object.
     */
    public RandomCompat(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Returns underlying {@link java.util.Random} instance
     * @return {@link java.util.Random} object instance
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Returns a stream producing the given {@code streamSize} number of
     * pseudorandom {@code int} values.
     *
     * <p>A pseudorandom {@code int} value is generated as if it's the result of
     * calling the method {@link Random#nextInt()}
     *
     * @param streamSize the number of values to generate
     * @return a stream of pseudorandom {@code int} values
     * @throws IllegalArgumentException if {@code streamSize} is
     *          less than zero
     */
    public IntStream ints(long streamSize) {

        if(streamSize < 0L)
            throw new IllegalArgumentException();

        if(streamSize == 0L)
            return IntStream.empty();

        return IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return random.nextInt();
            }
        }).limit(streamSize);
    }

    /**
     * Returns an effectively unlimited stream of pseudorandom {@code int}
     * values.
     *
     * <p>A pseudorandom {@code int} value is generated as if it's the result of
     * calling the method {@link Random#nextInt()}.
     *
     * @return a stream of pseudorandom {@code int} values
     */
    public IntStream ints() {
        return IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return random.nextInt();
            }
        });
    }

    /**
     * Returns a stream producing the given {@code streamSize} number
     * of pseudorandom {@code int} values, each conforming to the given
     * origin (inclusive) and bound (exclusive).
     *
     * @param streamSize the number of values to generate
     * @param randomNumberOrigin the origin (inclusive) of each random value
     * @param randomNumberBound the bound (exclusive) if each random value
     * @return a stream of pseudorandom {@code int} values,
     *          each with the given origin (inclusive) and bound (exclusive)
     * @throws IllegalArgumentException if {@code streamSize} is
     *          less than zero, or {@code randomNumberOrigin} is
     *          greater than or equal to {@code randomNumberBound}
     */
    public IntStream ints(long streamSize, final int randomNumberOrigin, final int randomNumberBound) {
        if(streamSize < 0L)
            throw new IllegalArgumentException();

        if(streamSize == 0L)
            return IntStream.empty();

        return ints(randomNumberOrigin, randomNumberBound).limit(streamSize);
    }

    /**
     * Returns an effectively unlimited stream of pseudorandom {@code int}
     * values, each conforming to the given origin (inclusive) and bound (exclusive)
     *
     * @param randomNumberOrigin the origin (inclusive) of each random value
     * @param randomNumberBound the bound (exclusive) of each random value
     * @return a stream of pseudorandom {@code int} values,
     *          each with the given origin (inclusive) and bound (exclusive)
     * @throws IllegalArgumentException if {@code randomNumberOrigin}
     *          is greater than or equal to {@code randomNumberBound}
     */
    public IntStream ints(final int randomNumberOrigin, final int randomNumberBound) {

        if(randomNumberOrigin >= randomNumberBound)
            throw new IllegalArgumentException();

        return IntStream.generate(new IntSupplier() {

            private int bound = randomNumberBound - randomNumberOrigin;

            @Override
            public int getAsInt() {
                return randomNumberOrigin + random.nextInt(bound);
            }
        });
    }

}
