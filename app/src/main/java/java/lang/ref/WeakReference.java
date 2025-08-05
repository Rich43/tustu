package java.lang.ref;

/* loaded from: rt.jar:java/lang/ref/WeakReference.class */
public class WeakReference<T> extends Reference<T> {
    public WeakReference(T t2) {
        super(t2);
    }

    public WeakReference(T t2, ReferenceQueue<? super T> referenceQueue) {
        super(t2, referenceQueue);
    }
}
