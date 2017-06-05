package com.annimon.stream.function;

import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@code BooleanPredicate}.
 */
public class BooleanPredicateTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(BooleanPredicate.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testFalseOr() {
        assertFalse(falseOr.test(false));
        assertTrue(falseOr.test(true));
    }

    @Test
    public void testTrueAnd() {
        assertFalse(trueAnd.test(false));
        assertTrue(trueAnd.test(true));
    }

    @Test
    public void testIdentity() {
        BooleanPredicate identity = BooleanPredicate.Util.identity();

        assertThat(identity.test(false), is(false));
    }

    @Test
    public void testAndPredicate() {
        BooleanPredicate predicate = BooleanPredicate.Util.and(falseOr, trueAnd);

        assertFalse(predicate.test(false));
        assertTrue(predicate.test(true));
    }

    @Test
    public void testOrPredicate() {
        BooleanPredicate predicate = BooleanPredicate.Util.or(falseOr, trueAnd);

        assertFalse(predicate.test(false));
        assertTrue(predicate.test(true));
    }

    @Test
    public void testXorPredicate() {
        BooleanPredicate predicate = BooleanPredicate.Util.xor(falseOr, trueAnd);

        assertFalse(predicate.test(false));
        assertFalse(predicate.test(true));
    }

    @Test
    public void testNegatePredicate() {
        BooleanPredicate trueAndNegated = BooleanPredicate.Util.negate(trueAnd);

        assertTrue(trueAndNegated.test(false));
        assertFalse(trueAndNegated.test(true));
    }

    private static final BooleanPredicate falseOr = new BooleanPredicate() {
        @Override
        public boolean test(boolean value) {
            return false || value;
        }
    };

    private static final BooleanPredicate trueAnd = new BooleanPredicate() {

        @Override
        public boolean test(boolean value) {
            return true && value;
        }
    };
}
