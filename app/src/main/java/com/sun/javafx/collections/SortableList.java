package com.sun.javafx.collections;

import java.util.Comparator;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/SortableList.class */
public interface SortableList<E> extends List<E> {
    void sort();

    void sort(Comparator<? super E> comparator);
}
