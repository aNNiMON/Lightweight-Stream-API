package com.annimon.stream;

import java.util.Iterator;

/**
 *
 * @author aNNiMON
 */
abstract class LsaIterator<T> implements Iterator<T> {
 
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }
}
