package com.sun.corba.se.impl.orbutil;

import java.util.EmptyStackException;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/StackImpl.class */
public class StackImpl {
    private Object[] data = new Object[3];
    private int top = -1;

    public final boolean empty() {
        return this.top == -1;
    }

    public final Object peek() {
        if (empty()) {
            throw new EmptyStackException();
        }
        return this.data[this.top];
    }

    public final Object pop() {
        Object objPeek = peek();
        this.data[this.top] = null;
        this.top--;
        return objPeek;
    }

    private void ensure() {
        if (this.top == this.data.length - 1) {
            Object[] objArr = new Object[2 * this.data.length];
            System.arraycopy(this.data, 0, objArr, 0, this.data.length);
            this.data = objArr;
        }
    }

    public final Object push(Object obj) {
        ensure();
        this.top++;
        this.data[this.top] = obj;
        return obj;
    }
}
