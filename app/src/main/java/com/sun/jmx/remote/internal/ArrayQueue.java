package com.sun.jmx.remote.internal;

import java.util.AbstractList;

/* loaded from: rt.jar:com/sun/jmx/remote/internal/ArrayQueue.class */
public class ArrayQueue<T> extends AbstractList<T> {
    private int capacity;
    private T[] queue;
    private int head = 0;
    private int tail = 0;

    public ArrayQueue(int i2) {
        this.capacity = i2 + 1;
        this.queue = newArray(i2 + 1);
    }

    public void resize(int i2) {
        int size = size();
        if (i2 < size) {
            throw new IndexOutOfBoundsException("Resizing would lose data");
        }
        int i3 = i2 + 1;
        if (i3 == this.capacity) {
            return;
        }
        T[] tArrNewArray = newArray(i3);
        for (int i4 = 0; i4 < size; i4++) {
            tArrNewArray[i4] = get(i4);
        }
        this.capacity = i3;
        this.queue = tArrNewArray;
        this.head = 0;
        this.tail = size;
    }

    private T[] newArray(int i2) {
        return (T[]) new Object[i2];
    }

    @Override // java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(T t2) {
        this.queue[this.tail] = t2;
        int i2 = (this.tail + 1) % this.capacity;
        if (i2 == this.head) {
            throw new IndexOutOfBoundsException("Queue full");
        }
        this.tail = i2;
        return true;
    }

    @Override // java.util.AbstractList, java.util.List
    public T remove(int i2) {
        if (i2 != 0) {
            throw new IllegalArgumentException("Can only remove head of queue");
        }
        if (this.head == this.tail) {
            throw new IndexOutOfBoundsException("Queue empty");
        }
        T t2 = this.queue[this.head];
        this.queue[this.head] = null;
        this.head = (this.head + 1) % this.capacity;
        return t2;
    }

    @Override // java.util.AbstractList, java.util.List
    public T get(int i2) {
        int size = size();
        if (i2 < 0 || i2 >= size) {
            throw new IndexOutOfBoundsException("Index " + i2 + ", queue size " + size);
        }
        return this.queue[(this.head + i2) % this.capacity];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        int i2 = this.tail - this.head;
        if (i2 < 0) {
            i2 += this.capacity;
        }
        return i2;
    }
}
