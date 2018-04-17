package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code Consumer}.
 * 
 * @see com.annimon.stream.function.Consumer
 */
public class ConsumerTest {
    
    @Test
    public void testAccept() {
        IntHolder holder = new IntHolder(10);
        
        increment.accept(holder);
        assertEquals(11, holder.value);
        
        increment.accept(holder);
        assertEquals(12, holder.value);
        
        mulBy2.accept(holder);
        assertEquals(24, holder.value);
    }
    
    @Test
    public void testAndThen() {
        Consumer<IntHolder> consumer = Consumer.Util.andThen(increment, mulBy2);
        IntHolder holder = new IntHolder(10);
        // (10+1) * 2
        consumer.accept(holder);
        assertEquals(22, holder.value);
        // (22+1) * 2
        consumer.accept(holder);
        assertEquals(46, holder.value);
    }

    @Test
    public void testSafe() {
        Consumer<OutputStream> consumer = Consumer.Util.safe(writer);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(5);
        consumer.accept(baos);
        consumer.accept(null);
        consumer.accept(null);
        consumer.accept(baos);
        assertEquals(">>", baos.toString());
    }

    @Test
    public void testSafeWithOnFailedConsumer() {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(5);
        Consumer<OutputStream> consumer = Consumer.Util.safe(writer, new Consumer<OutputStream>() {
            @Override
            public void accept(OutputStream os) {
                baos.write('<');
            }
        });

        consumer.accept(baos);
        consumer.accept(baos);
        consumer.accept(null);
        consumer.accept(null);
        assertEquals(">><<", baos.toString());
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(Consumer.Util.class, hasOnlyPrivateConstructors());
    }
    
    private static final Consumer<IntHolder> increment = new Consumer<IntHolder>() {
        @Override
        public void accept(IntHolder holder) {
            holder.value++;
        }
    };
    
    private static final Consumer<IntHolder> mulBy2 = new Consumer<IntHolder>() {
        @Override
        public void accept(IntHolder holder) {
            holder.value *= 2;
        }
    };

    private static final ThrowableConsumer<OutputStream, Throwable> writer
            = new ThrowableConsumer<OutputStream, Throwable>() {
        @Override
        public void accept(OutputStream os) throws Throwable {
            os.write('>');
        }
    };
    
    static class IntHolder {
        int value;

        IntHolder(int value) {
            this.value = value;
        }
    }
}
