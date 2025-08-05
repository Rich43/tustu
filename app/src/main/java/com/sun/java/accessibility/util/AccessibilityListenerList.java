package com.sun.java.accessibility.util;

import java.util.EventListener;
import jdk.Exported;

@Exported
/* loaded from: jaccess.jar:com/sun/java/accessibility/util/AccessibilityListenerList.class */
public class AccessibilityListenerList {
    private static final Object[] NULL_ARRAY = new Object[0];
    protected transient Object[] listenerList = NULL_ARRAY;

    public Object[] getListenerList() {
        return this.listenerList;
    }

    public int getListenerCount() {
        return this.listenerList.length / 2;
    }

    public int getListenerCount(Class cls) {
        int i2 = 0;
        Object[] objArr = this.listenerList;
        for (int i3 = 0; i3 < objArr.length; i3 += 2) {
            if (cls == ((Class) objArr[i3])) {
                i2++;
            }
        }
        return i2;
    }

    public synchronized void add(Class cls, EventListener eventListener) {
        if (!cls.isInstance(eventListener)) {
            throw new IllegalArgumentException("Listener " + ((Object) eventListener) + " is not of type " + ((Object) cls));
        }
        if (eventListener == null) {
            throw new IllegalArgumentException("Listener " + ((Object) eventListener) + " is null");
        }
        if (this.listenerList == NULL_ARRAY) {
            this.listenerList = new Object[]{cls, eventListener};
            return;
        }
        int length = this.listenerList.length;
        Object[] objArr = new Object[length + 2];
        System.arraycopy(this.listenerList, 0, objArr, 0, length);
        objArr[length] = cls;
        objArr[length + 1] = eventListener;
        this.listenerList = objArr;
    }

    public synchronized void remove(Class cls, EventListener eventListener) {
        if (!cls.isInstance(eventListener)) {
            throw new IllegalArgumentException("Listener " + ((Object) eventListener) + " is not of type " + ((Object) cls));
        }
        if (eventListener == null) {
            throw new IllegalArgumentException("Listener " + ((Object) eventListener) + " is null");
        }
        int i2 = -1;
        int length = this.listenerList.length - 2;
        while (true) {
            if (length >= 0) {
                if (this.listenerList[length] != cls || this.listenerList[length + 1] != eventListener) {
                    length -= 2;
                } else {
                    i2 = length;
                    break;
                }
            } else {
                break;
            }
        }
        if (i2 != -1) {
            Object[] objArr = new Object[this.listenerList.length - 2];
            System.arraycopy(this.listenerList, 0, objArr, 0, i2);
            if (i2 < objArr.length) {
                System.arraycopy(this.listenerList, i2 + 2, objArr, i2, objArr.length - i2);
            }
            this.listenerList = objArr.length == 0 ? NULL_ARRAY : objArr;
        }
    }

    public String toString() {
        Object[] objArr = this.listenerList;
        String str = "EventListenerList: " + (objArr.length / 2) + " listeners: ";
        for (int i2 = 0; i2 <= objArr.length - 2; i2 += 2) {
            str = (str + " type " + ((Class) objArr[i2]).getName()) + " listener " + objArr[i2 + 1];
        }
        return str;
    }
}
