package javafx.collections;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:javafx/collections/WeakSetChangeListener.class */
public final class WeakSetChangeListener<E> implements SetChangeListener<E>, WeakListener {
    private final WeakReference<SetChangeListener<E>> ref;

    public WeakSetChangeListener(@NamedArg("listener") SetChangeListener<E> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<>(listener);
    }

    @Override // javafx.beans.WeakListener
    public boolean wasGarbageCollected() {
        return this.ref.get() == null;
    }

    @Override // javafx.collections.SetChangeListener
    public void onChanged(SetChangeListener.Change<? extends E> change) {
        SetChangeListener<E> listener = this.ref.get();
        if (listener != null) {
            listener.onChanged(change);
        } else {
            change.getSet().removeListener(this);
        }
    }
}
