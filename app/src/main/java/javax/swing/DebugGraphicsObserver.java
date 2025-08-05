package javax.swing;

import java.awt.Image;
import java.awt.image.ImageObserver;

/* loaded from: rt.jar:javax/swing/DebugGraphicsObserver.class */
class DebugGraphicsObserver implements ImageObserver {
    int lastInfo;

    DebugGraphicsObserver() {
    }

    synchronized boolean allBitsPresent() {
        return (this.lastInfo & 32) != 0;
    }

    synchronized boolean imageHasProblem() {
        return ((this.lastInfo & 64) == 0 && (this.lastInfo & 128) == 0) ? false : true;
    }

    @Override // java.awt.image.ImageObserver
    public synchronized boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        this.lastInfo = i2;
        return true;
    }
}
