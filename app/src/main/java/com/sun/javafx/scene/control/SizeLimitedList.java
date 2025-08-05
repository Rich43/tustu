package com.sun.javafx.scene.control;

import java.util.LinkedList;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/SizeLimitedList.class */
public class SizeLimitedList<E> {
    private final int maxSize;
    private final List<E> backingList = new LinkedList();

    public SizeLimitedList(int maxSize) {
        this.maxSize = maxSize;
    }

    public E get(int index) {
        return this.backingList.get(index);
    }

    public void add(E item) {
        this.backingList.add(0, item);
        if (this.backingList.size() > this.maxSize) {
            this.backingList.remove(this.maxSize);
        }
    }

    public int size() {
        return this.backingList.size();
    }

    public boolean contains(E item) {
        return this.backingList.contains(item);
    }
}
