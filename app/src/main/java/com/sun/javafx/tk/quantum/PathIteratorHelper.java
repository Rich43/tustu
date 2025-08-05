package com.sun.javafx.tk.quantum;

import com.sun.javafx.geom.PathIterator;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PathIteratorHelper.class */
class PathIteratorHelper {
    private PathIterator itr;

    /* renamed from: f, reason: collision with root package name */
    private float[] f11968f = new float[6];

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PathIteratorHelper$Struct.class */
    public static final class Struct {
        float f0;
        float f1;
        float f2;
        float f3;
        float f4;
        float f5;
    }

    public PathIteratorHelper(PathIterator itr) {
        this.itr = itr;
    }

    public int getWindingRule() {
        return this.itr.getWindingRule();
    }

    public boolean isDone() {
        return this.itr.isDone();
    }

    public void next() {
        this.itr.next();
    }

    public int currentSegment(Struct struct) {
        int ret = this.itr.currentSegment(this.f11968f);
        struct.f0 = this.f11968f[0];
        struct.f1 = this.f11968f[1];
        struct.f2 = this.f11968f[2];
        struct.f3 = this.f11968f[3];
        struct.f4 = this.f11968f[4];
        struct.f5 = this.f11968f[5];
        return ret;
    }
}
