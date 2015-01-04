package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import java.util.Vector;

/**
 * Common implementations of {@link Collector}.
 * 
 * @author aNNiMON
 */
public final class Collectors {
    
    public static Collector toList() {
        return new Collector() {

            public Supplier supplier() {
                return new Supplier() {
                    public Object get() {
                        return new Vector();
                    }
                };
            }

            public BiConsumer accumulator() {
                return new BiConsumer() {
                    public void accept(Object t, Object u) {
                        ((Vector) t).addElement(u);
                    }
                };
            }

            public Function finisher() {
                return null;
            }
        };
    }
    
    public static Collector joining() {
        return new Collector() {
            
            public Supplier supplier() {
                return new Supplier() {
                    public Object get() {
                        return new StringBuffer();
                    }
                };
            }
            
            public BiConsumer accumulator() {
                return new BiConsumer() {
                    public void accept(Object t, Object u) {
                        ((StringBuffer)t).append(u);
                    }
                };
            }
            
            public Function finisher() {
                return new Function() {
                    public Object apply(Object value) {
                        return value.toString();
                    }
                };
            }
        };
    }
    
    public static Collector averaging(final Function mapper) {
        return new Collector() {
            public Supplier supplier() {
                return new Supplier() {
                    
                    public Object get() {
                        return new double[] { 0d, 0d };
                    }
                };
            }
            
            public BiConsumer accumulator() {
                return new BiConsumer() {
                    public void accept(Object v1, Object u) {
                        double[] t = (double[]) v1;
                        t[0]++; // count
                        t[1] += ((Double) mapper.apply(u)).doubleValue(); // sum
                    }
                };
            }
            
            public Function finisher() {
                return new Function() {
                    public Object apply(Object v1) {
                        double[] t = (double[]) v1;
                        return new Double(t[1] / t[0]);
                    }
                };
            }
        };
    }
}
