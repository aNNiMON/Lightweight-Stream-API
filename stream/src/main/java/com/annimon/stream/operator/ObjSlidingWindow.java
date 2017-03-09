package com.annimon.stream.operator;

import com.annimon.stream.internal.Compat;
import com.annimon.stream.iterator.LsaIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class ObjSlidingWindow<T> extends LsaIterator<List<T>> {

    private final Queue<T> queue;
    private final Iterator<? extends T> iterator;
    private final int windowSize;
    private final int stepWidth;

    public ObjSlidingWindow(Iterator<? extends T> iterator, int windowSize, int stepWidth) {
        this.iterator = iterator;
        this.windowSize = windowSize;
        this.stepWidth = stepWidth;
        queue = Compat.queue();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public List<T> nextIteration() {
        int i = queue.size();
        while (i < windowSize && iterator.hasNext()) {
            queue.offer(iterator.next());
            i++;
        }

        // the elements that are currently in the queue are the elements of our current window
        List<T> list = new ArrayList<T>(queue);

        // remove stepWidth elements from the queue
        final int pollCount = Math.min(queue.size(), stepWidth);
        for (int j = 0; j < pollCount; j++) {
            queue.poll();
        }

        // if the stepWidth is greater than the windowSize, skip (stepWidth - windowSize) elements
        for (int j = windowSize; j < stepWidth && iterator.hasNext(); j++) {
            iterator.next();
        }

        return list;
    }
}
