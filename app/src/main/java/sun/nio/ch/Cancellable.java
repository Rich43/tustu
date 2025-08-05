package sun.nio.ch;

/* loaded from: rt.jar:sun/nio/ch/Cancellable.class */
interface Cancellable {
    void onCancel(PendingFuture<?, ?> pendingFuture);
}
