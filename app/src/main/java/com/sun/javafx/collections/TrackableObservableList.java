package com.sun.javafx.collections;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/TrackableObservableList.class */
public abstract class TrackableObservableList<T> extends ObservableListWrapper<T> {
    protected abstract void onChanged(ListChangeListener.Change<T> change);

    public TrackableObservableList(List<T> list) {
        super(list);
    }

    public TrackableObservableList() {
        super(new ArrayList());
        addListener(c2 -> {
            onChanged(c2);
        });
    }
}
