package com.sun.javafx.tk.quantum;

import java.util.Collection;
import java.util.Vector;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GestureRecognizers.class */
class GestureRecognizers implements GestureRecognizer {
    private Collection<GestureRecognizer> recognizers = new Vector();
    private GestureRecognizer[] workList;

    GestureRecognizers() {
    }

    void add(GestureRecognizer r2) {
        if (!contains(r2)) {
            this.recognizers.add(r2);
            this.workList = null;
        }
    }

    void remove(GestureRecognizer r2) {
        if (contains(r2)) {
            this.recognizers.remove(r2);
            this.workList = null;
        }
    }

    boolean contains(GestureRecognizer r2) {
        return this.recognizers.contains(r2);
    }

    private GestureRecognizer[] synchWorkList() {
        if (this.workList == null) {
            this.workList = (GestureRecognizer[]) this.recognizers.toArray(new GestureRecognizer[0]);
        }
        return this.workList;
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyBeginTouchEvent(long time, int modifiers, boolean isDirect, int touchEventCount) {
        GestureRecognizer[] wl = synchWorkList();
        for (int idx = 0; idx != wl.length; idx++) {
            wl[idx].notifyBeginTouchEvent(time, modifiers, isDirect, touchEventCount);
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyNextTouchEvent(long time, int type, long touchId, int x2, int y2, int xAbs, int yAbs) {
        GestureRecognizer[] wl = synchWorkList();
        for (int idx = 0; idx != wl.length; idx++) {
            wl[idx].notifyNextTouchEvent(time, type, touchId, x2, y2, xAbs, yAbs);
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassTouchEventListener
    public void notifyEndTouchEvent(long time) {
        GestureRecognizer[] wl = synchWorkList();
        for (int idx = 0; idx != wl.length; idx++) {
            wl[idx].notifyEndTouchEvent(time);
        }
    }
}
