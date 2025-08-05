package javafx.collections;

import com.sun.javafx.collections.ArrayListenerHelper;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableArray;

/* loaded from: jfxrt.jar:javafx/collections/ObservableArrayBase.class */
public abstract class ObservableArrayBase<T extends ObservableArray<T>> implements ObservableArray<T> {
    private ArrayListenerHelper<T> listenerHelper;

    @Override // javafx.beans.Observable
    public final void addListener(InvalidationListener listener) {
        this.listenerHelper = ArrayListenerHelper.addListener(this.listenerHelper, this, listener);
    }

    @Override // javafx.beans.Observable
    public final void removeListener(InvalidationListener listener) {
        this.listenerHelper = ArrayListenerHelper.removeListener(this.listenerHelper, listener);
    }

    @Override // javafx.collections.ObservableArray
    public final void addListener(ArrayChangeListener<T> listener) {
        this.listenerHelper = ArrayListenerHelper.addListener(this.listenerHelper, this, listener);
    }

    @Override // javafx.collections.ObservableArray
    public final void removeListener(ArrayChangeListener<T> listener) {
        this.listenerHelper = ArrayListenerHelper.removeListener(this.listenerHelper, listener);
    }

    protected final void fireChange(boolean sizeChanged, int from, int to) {
        ArrayListenerHelper.fireValueChangedEvent(this.listenerHelper, sizeChanged, from, to);
    }
}
