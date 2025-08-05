package com.sun.javafx.tk.quantum;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassTouchEventListener.class */
interface GlassTouchEventListener {
    void notifyBeginTouchEvent(long j2, int i2, boolean z2, int i3);

    void notifyNextTouchEvent(long j2, int i2, long j3, int i3, int i4, int i5, int i6);

    void notifyEndTouchEvent(long j2);
}
