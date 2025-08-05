package javafx.collections;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;
import javafx.collections.ListChangeListener;

/* loaded from: jfxrt.jar:javafx/collections/WeakListChangeListener.class */
public final class WeakListChangeListener<E> implements ListChangeListener<E>, WeakListener {
    private final WeakReference<ListChangeListener<E>> ref;

    public WeakListChangeListener(@NamedArg("listener") ListChangeListener<E> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<>(listener);
    }

    @Override // javafx.beans.WeakListener
    public boolean wasGarbageCollected() {
        return this.ref.get() == null;
    }

    @Override // javafx.collections.ListChangeListener
    public void onChanged(ListChangeListener.Change<? extends E> change) {
        ListChangeListener<E> listener = this.ref.get();
        if (listener != null) {
            listener.onChanged(change);
        } else {
            change.getList().removeListener(this);
        }
    }
}
