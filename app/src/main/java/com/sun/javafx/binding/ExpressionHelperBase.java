package com.sun.javafx.binding;

import javafx.beans.WeakListener;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/ExpressionHelperBase.class */
public class ExpressionHelperBase {
    protected static int trim(int size, Object[] listeners) {
        int index = 0;
        while (index < size) {
            Object listener = listeners[index];
            if ((listener instanceof WeakListener) && ((WeakListener) listener).wasGarbageCollected()) {
                int numMoved = (size - index) - 1;
                if (numMoved > 0) {
                    System.arraycopy(listeners, index + 1, listeners, index, numMoved);
                }
                size--;
                listeners[size] = null;
                index--;
            }
            index++;
        }
        return size;
    }
}
