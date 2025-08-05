package javafx.collections;

import javafx.collections.ObservableArray;

/* loaded from: jfxrt.jar:javafx/collections/ArrayChangeListener.class */
public interface ArrayChangeListener<T extends ObservableArray<T>> {
    void onChanged(T t2, boolean z2, int i2, int i3);
}
