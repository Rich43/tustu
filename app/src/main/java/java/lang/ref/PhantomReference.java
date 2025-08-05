package java.lang.ref;

/* loaded from: rt.jar:java/lang/ref/PhantomReference.class */
public class PhantomReference<T> extends Reference<T> {
    @Override // java.lang.ref.Reference
    public T get() {
        return null;
    }

    public PhantomReference(T t2, ReferenceQueue<? super T> referenceQueue) {
        super(t2, referenceQueue);
    }
}
