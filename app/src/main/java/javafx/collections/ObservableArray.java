package javafx.collections;

import javafx.beans.Observable;
import javafx.collections.ObservableArray;

/* loaded from: jfxrt.jar:javafx/collections/ObservableArray.class */
public interface ObservableArray<T extends ObservableArray<T>> extends Observable {
    void addListener(ArrayChangeListener<T> arrayChangeListener);

    void removeListener(ArrayChangeListener<T> arrayChangeListener);

    void resize(int i2);

    void ensureCapacity(int i2);

    void trimToSize();

    void clear();

    int size();
}
