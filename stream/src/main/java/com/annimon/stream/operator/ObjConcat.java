package com.annimon.stream.operator;

import com.annimon.stream.LsaExtIterator;
import java.util.Iterator;

public class ObjConcat<T> extends LsaExtIterator<T> {

    private final Iterator<? extends T> iterator1;
    private final Iterator<? extends T> iterator2;

    public ObjConcat(Iterator<? extends T> iterator1, Iterator<? extends T> iterator2) {
        this.iterator1 = iterator1;
        this.iterator2 = iterator2;
    }

    @Override
    protected void nextIteration() {
        if (iterator1.hasNext()) {
            next = iterator1.next();
            hasNext = true;
            return;
        }
        if (iterator2.hasNext()) {
            next = iterator2.next();
            hasNext = true;
            return;
        }
        hasNext = false;
    }
}
