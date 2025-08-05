package sun.misc;

/* compiled from: Queue.java */
/* loaded from: rt.jar:sun/misc/QueueElement.class */
class QueueElement<T> {
    QueueElement<T> next = null;
    QueueElement<T> prev = null;
    T obj;

    QueueElement(T t2) {
        this.obj = null;
        this.obj = t2;
    }

    public String toString() {
        return "QueueElement[obj=" + ((Object) this.obj) + (this.prev == null ? " null" : " prev") + (this.next == null ? " null" : " next") + "]";
    }
}
