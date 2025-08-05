package com.sun.corba.se.impl.encoding;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/BufferQueue.class */
public class BufferQueue {
    private LinkedList list = new LinkedList();

    public void enqueue(ByteBufferWithInfo byteBufferWithInfo) {
        this.list.addLast(byteBufferWithInfo);
    }

    public ByteBufferWithInfo dequeue() throws NoSuchElementException {
        return (ByteBufferWithInfo) this.list.removeFirst();
    }

    public int size() {
        return this.list.size();
    }

    public void push(ByteBufferWithInfo byteBufferWithInfo) {
        this.list.addFirst(byteBufferWithInfo);
    }
}
