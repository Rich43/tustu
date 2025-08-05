package com.sun.jndi.toolkit.ctx;

import javax.naming.Name;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/HeadTail.class */
public class HeadTail {
    private int status;
    private Name head;
    private Name tail;

    public HeadTail(Name name, Name name2) {
        this(name, name2, 0);
    }

    public HeadTail(Name name, Name name2, int i2) {
        this.status = i2;
        this.head = name;
        this.tail = name2;
    }

    public void setStatus(int i2) {
        this.status = i2;
    }

    public Name getHead() {
        return this.head;
    }

    public Name getTail() {
        return this.tail;
    }

    public int getStatus() {
        return this.status;
    }
}
