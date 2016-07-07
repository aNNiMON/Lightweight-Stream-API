package com.annimon.stream;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.function.IntFunction;
import com.annimon.stream.function.ints.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests {@code IntStream}
 *
 * @see com.annimon.stream.IntStream
 */
public class IntStreamTest {

    @Test
    public void testStreamEmpty() {
        assertTrue(IntStream.empty().count() == 0);
        assertTrue(IntStream.empty().iterator().nextInt() == 0);
    }

    @Test
    public void testStreamOfInts() {
        int[] data1 = {1, 2, 3, 4, 5};
        int[] data2 = {42};
        int[] data3 = {};

        assertTrue(IntStream.of(data1).count() == 5);
        assertTrue(IntStream.of(data2).findFirst().getAsInt() == 42);
        assertTrue(!IntStream.of(data3).findFirst().isPresent());
    }

    @Test
    public void testStreamOfInt() {
        assertTrue(IntStream.of(42).count() == 1);
        assertTrue(IntStream.of(42).findFirst().isPresent());
    }

    @Test
    public void testStreamConcat() {
        IntStream a1 = IntStream.empty();
        IntStream b1 = IntStream.empty();

        assertTrue(IntStream.concat(a1,b1).count() == 0);

        IntStream a2 = IntStream.of(1,2,3);
        IntStream b2 = IntStream.empty();

        assertTrue(IntStream.concat(a2, b2).count() == 3);

        IntStream a3 = IntStream.empty();
        IntStream b3 = IntStream.of(42);

        assertTrue(IntStream.concat(a3, b3).findFirst().getAsInt() == 42);

        IntStream a4 = IntStream.of(2, 4, 6, 8);
        IntStream b4 = IntStream.of(1, 3, 5, 7, 9);

        assertTrue(IntStream.concat(a4, b4).count() == 9);
    }

    @Test
    public void testStreamGenerate() {
        IntSupplier s = new IntSupplier() {
            @Override
            public int getAsInt() {
                return 42;
            }
        };

        assertTrue(IntStream.generate(s).findFirst().getAsInt() == 42);
    }

    @Test
    public void testStreamIterate() {
        IntUnaryOperator operator = new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand + 1;
            }
        };

        assertTrue(IntStream.iterate(1, operator).limit(3).sum() == 6);
        assertTrue(IntStream.iterate(1, operator).iterator().hasNext());
    }

    @Test
    public void testStreamRange() {
        assertTrue(IntStream.range(1, 5).sum() == 10);
        assertTrue(IntStream.range(2, 2).count() == 0);
    }

    @Test
    public void testStreamRangeClosed() {
        assertTrue(IntStream.rangeClosed(1, 5).sum() == 15);
        assertTrue(IntStream.rangeClosed(1, 5).count() == 5);
    }

    @Test
    public void testStreamConstructor() {
        IntStream s = new IntStream(new PrimitiveIterator.OfInt() {

            @Override
            public int nextInt() {
                return 85;
            }

            @Override
            public boolean hasNext() {
                return false;
            }
        });

        assertTrue(s.iterator().nextInt() == 85);
    }

    @Test
    public void testStreamFilter() {
        assertTrue(IntStream.rangeClosed(1, 10).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }).count() == 5);

        assertTrue(IntStream.rangeClosed(1, 10).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }).sum() == 30);

        assertTrue(IntStream.iterate(0, new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand + 1;
            }
        }).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 0;
            }
        }).findFirst().getAsInt() == 0);
    }

    @Test
    public void testStreamFilterNot() {
        assertTrue(IntStream.rangeClosed(1, 10).filterNot(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }).count() == 5);

        assertTrue(IntStream.rangeClosed(1, 10).filterNot(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }).sum() == 25);
    }

    @Test
    public void testStreamMap() {
        assertTrue(IntStream.of(5).map(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand;
            }
        }).findFirst().getAsInt() == -5);

        assertTrue(IntStream.of(1,2,3,4,5).map(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand;
            }
        }).sum() < 0);
    }

    @Test
    public void testStreamFlatMap() {
        assertTrue(IntStream.range(-1, 5).flatMap(new IntFunction<IntStream>() {
            @Override
            public IntStream apply(int value) {

                if(value < 0) {
                    return null;
                }

                if(value == 0) {
                    return IntStream.empty();
                }

                return IntStream.range(0, value);
            }
        }).count() == 10);
    }

    @Test
    public void testStreamDistinct() {
        assertTrue(IntStream.of(1, 2, -1, 10, 1, 1, -1, 5).distinct().count() == 5);
        assertTrue(IntStream.of(1, 2, -1, 10, 1, 1, -1, 5).distinct().sum() == 17);
    }

    @Test
    public void testStreamLimit() {
        assertTrue(IntStream.of(1,2,3,4,5,6).limit(3).count() == 3);
        assertTrue(IntStream.generate(new IntSupplier() {

            int current = 42;

            @Override
            public int getAsInt() {
                current = current + current<<1;
                return current;
            }
        }).limit(6).count() == 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStreamLimitNegative() {
        IntStream.of(42).limit(-1).count();
    }

    @Test
    public void testStreamCount() {
        assertEquals(IntStream.empty().count(), 0);
        assertEquals(IntStream.of(42).count(), 1);
        assertEquals(IntStream.range(1, 7).count(), 6);
        assertEquals(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 1;
            }
        }).limit(10).count(), 10);

        assertEquals(IntStream.rangeClosed(1, 7).skip(3).count(), 4);
    }

    @Test
    public void testStreamFindFirst() {
        assertFalse(IntStream.empty().findFirst().isPresent());
        assertEquals(IntStream.of(42).findFirst().getAsInt(), 42);
        assertTrue(IntStream.rangeClosed(2, 5).findFirst().isPresent());
    }

    @Test
    public void testStreamSum() {
        assertEquals(IntStream.empty().sum(), 0);
        assertEquals(IntStream.of(42).sum(), 42);
        assertEquals(IntStream.rangeClosed(4, 8).sum(), 30);
    }

    @Test
    public void testStreamBoxed() {
        assertTrue(IntStream.of(1, 10, 20).boxed().reduce(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer value1, Integer value2) {
                return value1 + value2;
            }
        }).get() == 31);
    }

    @Test
    public void testStreamSorted() {
        assertTrue(IntStream.empty().sorted().count() == 0);
        assertTrue(IntStream.of(42).sorted().findFirst().getAsInt() == 42);

        final boolean[] wrongOrder = new boolean[]{false};

        IntStream.iterate(2, new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand + 1;
            }
        })
        .limit(1000)
        .sorted()
        .forEach(new IntConsumer() {

            int currentValue = Integer.MIN_VALUE;

            @Override
            public void accept(int value) {
                if(value < currentValue) {
                    wrongOrder[0] = true;
                }
                currentValue = value;
            }
        });

        assertTrue(!wrongOrder[0]);
    }

    @Test
    public void testStreamPeek() {

        assertTrue(IntStream.empty().peek(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalStateException();
            }
        }).count() == 0);

        assertTrue(IntStream.generate(new IntSupplier() {
            int value = 2;
            @Override
            public int getAsInt() {
                int v = value;
                value *= 2;
                return v;
            }
        }).peek(new IntConsumer() {
            int curValue = 1;
            @Override
            public void accept(int value) {
                if(value != curValue*2)
                    throw new IllegalStateException();

                curValue = value;
            }
        }).limit(10).count() == 10);
    }

    @Test
    public void testStreamSkip() {
        assertTrue(IntStream.empty().skip(2).count() == 0);
        assertTrue(IntStream.range(10, 20).skip(5).count() == 5);
        assertTrue(IntStream.range(10, 20).skip(0).count() == 10);
        assertTrue(IntStream.range(10, 20).skip(10).count() == 0);
        assertTrue(IntStream.range(10, 20).skip(20).count() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStreamSkipNegative() {
        IntStream.empty().skip(-5);
    }

    @Test
    public void testStreamForEach() {
        IntStream.empty().forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                throw new IllegalStateException();
            }
        });

        IntStream.of(42).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                assertTrue(value == 42);
            }
        });

        final int[] sum = new int[1];

        IntStream.rangeClosed(10, 20).forEach(new IntConsumer() {
            @Override
            public void accept(int value) {
                sum[0] += value;
            }
        });

        assertEquals(sum[0], 165);
    }

    @Test
    public void testStreamToArray() {
        assertEquals(IntStream.empty().toArray().length, 0);
        assertEquals(IntStream.of(100).toArray()[0], 100);
        assertEquals(IntStream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).skip(4).toArray().length, 5);

        assertEquals(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return -1;
            }
        }).limit(14).toArray().length, 14);

        assertEquals(IntStream.of(IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return -1;
            }
        }).limit(14).toArray()).sum(), -14);
    }

    @Test
    public void testStreamReduceIdentity() {
        assertEquals(IntStream.empty().reduce(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        }), 1);

        assertEquals(IntStream.of(42).reduce(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        }), 43);

        assertEquals(IntStream.of(5, 7, 3, 9, 1).reduce(Integer.MIN_VALUE, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                if (left >= right)
                    return left;

                return right;
            }
        }), 9);
    }

    @Test
    public void testStreamReduce() {
        assertFalse(IntStream.empty().reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                throw new IllegalStateException();
            }
        }).isPresent());

        assertEquals(IntStream.of(42).reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                throw new IllegalStateException();
            }
        }).getAsInt(), 42);

        assertEquals(IntStream.of(41, 42).reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                if (right > left)
                    return right;
                return left;
            }
        }).getAsInt(), 42);
    }

    @Test
    public void testStreamMin() {
        assertFalse(IntStream.empty().min().isPresent());

        assertTrue(IntStream.of(42).min().isPresent());
        assertEquals(IntStream.of(42).min().getAsInt(), 42);

        assertEquals(IntStream.of(-1, -2, -3, -2, -3, -5, -2, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .min().getAsInt(), Integer.MIN_VALUE);
    }

    @Test
    public void testStreamMax() {
        assertFalse(IntStream.empty().max().isPresent());

        assertTrue(IntStream.of(42).max().isPresent());
        assertEquals(IntStream.of(42).max().getAsInt(), 42);

        assertEquals(IntStream.of(-1, -2, -3, -2, -3, -5, -2, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .max().getAsInt(), Integer.MAX_VALUE);
    }

    @Test
    public void testStreamAnyMatch() {
        IntStream.empty().anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertTrue(IntStream.of(42).anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertTrue(IntStream.of(5, 7, 9, 10, 7, 5).anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }));

        assertFalse(IntStream.of(5, 7, 9, 11, 7, 5).anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }));
    }

    @Test
    public void testStreamAllMatch() {
        IntStream.empty().allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertTrue(IntStream.of(42).allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertFalse(IntStream.of(5, 7, 9, 10, 7, 5).allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 != 0;
            }
        }));

        assertTrue(IntStream.of(5, 7, 9, 11, 7, 5).allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 != 0;
            }
        }));
    }

    @Test
    public void testStreamNoneMatch() {
        IntStream.empty().noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertFalse(IntStream.of(42).noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertFalse(IntStream.of(5, 7, 9, 10, 7, 5).noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }));

        assertTrue(IntStream.of(5, 7, 9, 11, 7, 5).noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value % 2 == 0;
            }
        }));
    }
}
