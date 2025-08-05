package com.sun.javafx.collections;

import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/SourceAdapterChange.class */
public class SourceAdapterChange<E> extends ListChangeListener.Change<E> {
    private final ListChangeListener.Change<? extends E> change;
    private int[] perm;

    public SourceAdapterChange(ObservableList<E> list, ListChangeListener.Change<? extends E> change) {
        super(list);
        this.change = change;
    }

    @Override // javafx.collections.ListChangeListener.Change
    public boolean next() {
        this.perm = null;
        return this.change.next();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public void reset() {
        this.change.reset();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public int getTo() {
        return this.change.getTo();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public List<E> getRemoved() {
        return this.change.getRemoved();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public int getFrom() {
        return this.change.getFrom();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public boolean wasUpdated() {
        return this.change.wasUpdated();
    }

    @Override // javafx.collections.ListChangeListener.Change
    protected int[] getPermutation() {
        if (this.perm == null) {
            if (this.change.wasPermutated()) {
                int from = this.change.getFrom();
                int n2 = this.change.getTo() - from;
                this.perm = new int[n2];
                for (int i2 = 0; i2 < n2; i2++) {
                    this.perm[i2] = this.change.getPermutation(from + i2);
                }
            } else {
                this.perm = new int[0];
            }
        }
        return this.perm;
    }

    public String toString() {
        return this.change.toString();
    }
}
