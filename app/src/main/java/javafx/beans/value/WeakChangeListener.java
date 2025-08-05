package javafx.beans.value;

import java.lang.ref.WeakReference;
import javafx.beans.NamedArg;
import javafx.beans.WeakListener;

/* loaded from: jfxrt.jar:javafx/beans/value/WeakChangeListener.class */
public final class WeakChangeListener<T> implements ChangeListener<T>, WeakListener {
    private final WeakReference<ChangeListener<T>> ref;

    public WeakChangeListener(@NamedArg("listener") ChangeListener<T> listener) {
        if (listener == null) {
            throw new NullPointerException("Listener must be specified.");
        }
        this.ref = new WeakReference<>(listener);
    }

    @Override // javafx.beans.WeakListener
    public boolean wasGarbageCollected() {
        return this.ref.get() == null;
    }

    @Override // javafx.beans.value.ChangeListener
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        ChangeListener<T> listener = this.ref.get();
        if (listener != null) {
            listener.changed(observable, oldValue, newValue);
        } else {
            observable.removeListener(this);
        }
    }
}
