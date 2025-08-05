package com.sun.jndi.toolkit.ctx;

/* loaded from: rt.jar:com/sun/jndi/toolkit/ctx/StringHeadTail.class */
public class StringHeadTail {
    private int status;
    private String head;
    private String tail;

    public StringHeadTail(String str, String str2) {
        this(str, str2, 0);
    }

    public StringHeadTail(String str, String str2, int i2) {
        this.status = i2;
        this.head = str;
        this.tail = str2;
    }

    public void setStatus(int i2) {
        this.status = i2;
    }

    public String getHead() {
        return this.head;
    }

    public String getTail() {
        return this.tail;
    }

    public int getStatus() {
        return this.status;
    }
}
