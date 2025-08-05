package javafx.collections;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;
import javafx.collections.MapChangeListener;

/* loaded from: jfxrt.jar:javafx/collections/WeakMapChangeListener.class */
public final class WeakMapChangeListener<K, V> implements MapChangeListener<K, V>, WeakListener {
    private final WeakReference<MapChangeListener<K, V>> ref;

    public WeakMapChangeListener(@NamedArg("listener") MapChangeListener<K, V> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<>(listener);
    }

    @Override // javafx.beans.WeakListener
    public boolean wasGarbageCollected() {
        return this.ref.get() == null;
    }

    @Override // javafx.collections.MapChangeListener
    public void onChanged(MapChangeListener.Change<? extends K, ? extends V> change) {
        MapChangeListener<K, V> listener = this.ref.get();
        if (listener != null) {
            listener.onChanged(change);
        } else {
            change.getMap().removeListener(this);
        }
    }
}
