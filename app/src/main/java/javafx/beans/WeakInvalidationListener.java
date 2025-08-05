package javafx.beans;

import java.lang.ref.WeakReference;

/* loaded from: jfxrt.jar:javafx/beans/WeakInvalidationListener.class */
public final class WeakInvalidationListener implements InvalidationListener, WeakListener {
    private final WeakReference<InvalidationListener> ref;

    public WeakInvalidationListener(@NamedArg("listener") InvalidationListener listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<>(listener);
    }

    @Override // javafx.beans.WeakListener
    public boolean wasGarbageCollected() {
        return this.ref.get() == null;
    }

    @Override // javafx.beans.InvalidationListener
    public void invalidated(Observable observable) {
        InvalidationListener listener = this.ref.get();
        if (listener != null) {
            listener.invalidated(observable);
        } else {
            observable.removeListener(this);
        }
    }
}
