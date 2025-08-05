package com.sun.org.apache.bcel.internal.util;

import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import java.io.Serializable;
import java.util.LinkedList;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/ClassQueue.class */
public class ClassQueue implements Serializable {
    protected LinkedList vec = new LinkedList();

    public void enqueue(JavaClass clazz) {
        this.vec.addLast(clazz);
    }

    public JavaClass dequeue() {
        return (JavaClass) this.vec.removeFirst();
    }

    public boolean empty() {
        return this.vec.isEmpty();
    }

    public String toString() {
        return this.vec.toString();
    }
}
