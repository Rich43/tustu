package sun.misc;

import java.util.Enumeration;
import javafx.fxml.FXMLLoader;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:sun/misc/Queue.class */
public class Queue<T> {
    int length = 0;
    QueueElement<T> head = null;
    QueueElement<T> tail = null;

    public synchronized void enqueue(T t2) {
        QueueElement<T> queueElement = new QueueElement<>(t2);
        if (this.head == null) {
            this.head = queueElement;
            this.tail = queueElement;
            this.length = 1;
        } else {
            queueElement.next = this.head;
            this.head.prev = queueElement;
            this.head = queueElement;
            this.length++;
        }
        notify();
    }

    public T dequeue() throws InterruptedException {
        return dequeue(0L);
    }

    public synchronized T dequeue(long j2) throws InterruptedException {
        while (this.tail == null) {
            wait(j2);
        }
        QueueElement<T> queueElement = this.tail;
        this.tail = queueElement.prev;
        if (this.tail == null) {
            this.head = null;
        } else {
            this.tail.next = null;
        }
        this.length--;
        return queueElement.obj;
    }

    public synchronized boolean isEmpty() {
        return this.tail == null;
    }

    public final synchronized Enumeration<T> elements() {
        return new LIFOQueueEnumerator(this);
    }

    public final synchronized Enumeration<T> reverseElements() {
        return new FIFOQueueEnumerator(this);
    }

    public synchronized void dump(String str) {
        System.err.println(">> " + str);
        System.err.println("[" + this.length + " elt(s); head = " + (this.head == null ? FXMLLoader.NULL_KEYWORD : ((Object) this.head.obj) + "") + " tail = " + (this.tail == null ? FXMLLoader.NULL_KEYWORD : ((Object) this.tail.obj) + ""));
        QueueElement<T> queueElement = null;
        for (QueueElement<T> queueElement2 = this.head; queueElement2 != null; queueElement2 = queueElement2.next) {
            System.err.println(Constants.INDENT + ((Object) queueElement2));
            queueElement = queueElement2;
        }
        if (queueElement != this.tail) {
            System.err.println("  tail != last: " + ((Object) this.tail) + ", " + ((Object) queueElement));
        }
        System.err.println("]");
    }
}
