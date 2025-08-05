package com.sun.webkit.graphics;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/Ref.class */
public class Ref {
    private int count = 0;
    private final int id = WCGraphicsManager.getGraphicsManager().createID();

    public synchronized void ref() {
        if (this.count == 0) {
            WCGraphicsManager.getGraphicsManager().ref(this);
        }
        this.count++;
    }

    public synchronized void deref() {
        if (this.count == 0) {
            throw new IllegalStateException("Object  " + this.id + " has no references.");
        }
        this.count--;
        if (this.count == 0) {
            WCGraphicsManager.getGraphicsManager().deref(this);
        }
    }

    public boolean hasRefs() {
        return this.count > 0;
    }

    public int getID() {
        return this.id;
    }
}
