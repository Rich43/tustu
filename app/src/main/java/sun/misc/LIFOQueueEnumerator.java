package sun.misc;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* compiled from: Queue.java */
/* loaded from: rt.jar:sun/misc/LIFOQueueEnumerator.class */
final class LIFOQueueEnumerator<T> implements Enumeration<T> {
    Queue<T> queue;
    QueueElement<T> cursor;

    LIFOQueueEnumerator(Queue<T> queue) {
        this.queue = queue;
        this.cursor = queue.head;
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
                this.cursor = this.cursor.next;
                return queueElement.obj;
            }
            throw new NoSuchElementException("LIFOQueueEnumerator");
        }
    }
}
