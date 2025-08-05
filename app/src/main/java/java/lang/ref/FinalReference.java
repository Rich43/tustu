package java.lang.ref;

/* loaded from: rt.jar:java/lang/ref/FinalReference.class */
class FinalReference<T> extends Reference<T> {
    public FinalReference(T t2, ReferenceQueue<? super T> referenceQueue) {
        super(t2, referenceQueue);
    }

    @Override // java.lang.ref.Reference
    public boolean enqueue() {
        throw new InternalError("should never reach here");
    }
}
