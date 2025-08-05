package com.sun.javafx.collections;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/SetAdapterChange.class */
public class SetAdapterChange<E> extends SetChangeListener.Change<E> {
    private final SetChangeListener.Change<? extends E> change;

    public SetAdapterChange(ObservableSet<E> set, SetChangeListener.Change<? extends E> change) {
        super(set);
        this.change = change;
    }

    public String toString() {
        return this.change.toString();
    }

    @Override // javafx.collections.SetChangeListener.Change
    public boolean wasAdded() {
        return this.change.wasAdded();
    }

    @Override // javafx.collections.SetChangeListener.Change
    public boolean wasRemoved() {
        return this.change.wasRemoved();
    }

    @Override // javafx.collections.SetChangeListener.Change
    public E getElementAdded() {
        return this.change.getElementAdded();
    }

    @Override // javafx.collections.SetChangeListener.Change
    public E getElementRemoved() {
        return this.change.getElementRemoved();
    }
}
