package java.lang.ref;

/* loaded from: rt.jar:java/lang/ref/SoftReference.class */
public class SoftReference<T> extends Reference<T> {
    private static long clock;
    private long timestamp;

    public SoftReference(T t2) {
        super(t2);
        this.timestamp = clock;
    }

    public SoftReference(T t2, ReferenceQueue<? super T> referenceQueue) {
        super(t2, referenceQueue);
        this.timestamp = clock;
    }

    @Override // java.lang.ref.Reference
    public T get() {
        T t2 = (T) super.get();
        if (t2 != null && this.timestamp != clock) {
            this.timestamp = clock;
        }
        return t2;
    }
}
