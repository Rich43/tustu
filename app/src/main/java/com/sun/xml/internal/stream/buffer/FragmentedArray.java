package com.sun.xml.internal.stream.buffer;

/* loaded from: rt.jar:com/sun/xml/internal/stream/buffer/FragmentedArray.class */
final class FragmentedArray<T> {
    private T _item;
    private FragmentedArray<T> _next;
    private FragmentedArray<T> _previous;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FragmentedArray.class.desiredAssertionStatus();
    }

    FragmentedArray(T item) {
        this(item, null);
    }

    FragmentedArray(T item, FragmentedArray<T> previous) {
        setArray(item);
        if (previous != null) {
            previous._next = this;
            this._previous = previous;
        }
    }

    T getArray() {
        return this._item;
    }

    void setArray(T item) {
        if (!$assertionsDisabled && !item.getClass().isArray()) {
            throw new AssertionError();
        }
        this._item = item;
    }

    FragmentedArray<T> getNext() {
        return this._next;
    }

    void setNext(FragmentedArray<T> next) {
        this._next = next;
        if (next != null) {
            next._previous = this;
        }
    }

    FragmentedArray<T> getPrevious() {
        return this._previous;
    }

    void setPrevious(FragmentedArray<T> previous) {
        this._previous = previous;
        if (previous != null) {
            previous._next = this;
        }
    }
}
