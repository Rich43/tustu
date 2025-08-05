package com.sun.javafx.collections;

import java.util.AbstractList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/MappingChange.class */
public final class MappingChange<E, F> extends ListChangeListener.Change<F> {
    private final Map<E, F> map;
    private final ListChangeListener.Change<? extends E> original;
    private List<F> removed;
    public static final Map NOOP_MAP = new Map() { // from class: com.sun.javafx.collections.MappingChange.1
        @Override // com.sun.javafx.collections.MappingChange.Map
        public Object map(Object original) {
            return original;
        }
    };

    /* loaded from: jfxrt.jar:com/sun/javafx/collections/MappingChange$Map.class */
    public interface Map<E, F> {
        F map(E e2);
    }

    public MappingChange(ListChangeListener.Change<? extends E> original, Map<E, F> map, ObservableList<F> list) {
        super(list);
        this.original = original;
        this.map = map;
    }

    @Override // javafx.collections.ListChangeListener.Change
    public boolean next() {
        return this.original.next();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public void reset() {
        this.original.reset();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public int getFrom() {
        return this.original.getFrom();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public int getTo() {
        return this.original.getTo();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public List<F> getRemoved() {
        if (this.removed == null) {
            this.removed = new AbstractList<F>() { // from class: com.sun.javafx.collections.MappingChange.2
                @Override // java.util.AbstractList, java.util.List
                public F get(int i2) {
                    return (F) MappingChange.this.map.map(MappingChange.this.original.getRemoved().get(i2));
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public int size() {
                    return MappingChange.this.original.getRemovedSize();
                }
            };
        }
        return this.removed;
    }

    @Override // javafx.collections.ListChangeListener.Change
    protected int[] getPermutation() {
        return new int[0];
    }

    @Override // javafx.collections.ListChangeListener.Change
    public boolean wasPermutated() {
        return this.original.wasPermutated();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public boolean wasUpdated() {
        return this.original.wasUpdated();
    }

    @Override // javafx.collections.ListChangeListener.Change
    public int getPermutation(int i2) {
        return this.original.getPermutation(i2);
    }

    public String toString() {
        int posToEnd = 0;
        while (next()) {
            posToEnd++;
        }
        int size = 0;
        reset();
        while (next()) {
            size++;
        }
        reset();
        StringBuilder b2 = new StringBuilder();
        b2.append("{ ");
        while (next()) {
            if (wasPermutated()) {
                b2.append(ChangeHelper.permChangeToString(getPermutation()));
            } else if (wasUpdated()) {
                b2.append(ChangeHelper.updateChangeToString(getFrom(), getTo()));
            } else {
                b2.append(ChangeHelper.addRemoveChangeToString(getFrom(), getTo(), getList(), getRemoved()));
            }
            if (0 != size) {
                b2.append(", ");
            }
        }
        b2.append(" }");
        reset();
        int pos = size - posToEnd;
        while (true) {
            int i2 = pos;
            pos--;
            if (i2 > 0) {
                next();
            } else {
                return b2.toString();
            }
        }
    }
}
