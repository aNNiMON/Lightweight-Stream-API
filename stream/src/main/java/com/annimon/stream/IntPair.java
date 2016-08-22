package com.annimon.stream;

/**
 * A pair with int-valued first element and object-valued second element.
 *
 * @param <T> the type of the second element
 * @since 1.1.2
 */
public final class IntPair<T> {

    private final int first;
    private final T second;

    public IntPair(int first, T second) {
        this.first = first;
        this.second = second;
    }

    /**
     * A first element in a pair.
     *
     * @return a first element
     */
    public int getFirst() {
        return first;
    }

    /**
     * A second element in a pair.
     *
     * @return a second element
     */
    public T getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.first;
        hash = 97 * hash + (this.second != null ? this.second.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass())
            return false;
        final IntPair<?> other = (IntPair<?>) obj;
        if (this.first != other.first)
            return false;
        return !(this.second != other.second &&
                (this.second == null || !this.second.equals(other.second)));
    }

    @Override
    public String toString() {
        return "IntPair[" + first + ", " + second + ']';
    }
}
