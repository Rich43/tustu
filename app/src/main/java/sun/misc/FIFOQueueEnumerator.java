package sun.misc;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* compiled from: Queue.java */
/* loaded from: rt.jar:sun/misc/FIFOQueueEnumerator.class */
final class FIFOQueueEnumerator<T> implements Enumeration<T> {
    Queue<T> queue;
    QueueElement<T> cursor;

    FIFOQueueEnumerator(Queue<T> queue) {
        this.queue = queue;
        this.cursor = queue.tail;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        return this.cursor != null;
    }

    @Override // java.util.Enumeration
    public T nextElement() {
        synchronized (this.queue) {
            if (this.cursor != null) {
                QueueElement<T> queueElement = this.cursor;
                this.cursor = this.cursor.prev;
                return queueElement.obj;
            }
            throw new NoSuchElementException("FIFOQueueEnumerator");
        }
    }
}
