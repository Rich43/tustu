package com.sun.demo.jvmti.hprof;

/* loaded from: rt.jar:com/sun/demo/jvmti/hprof/Tracker.class */
public class Tracker {
    private static int engaged = 0;

    private static native void nativeObjectInit(Object obj, Object obj2);

    private static native void nativeNewArray(Object obj, Object obj2);

    private static native void nativeCallSite(Object obj, int i2, int i3);

    private static native void nativeReturnSite(Object obj, int i2, int i3);

    public static void ObjectInit(Object obj) {
        if (engaged != 0) {
            if (obj == null) {
                throw new IllegalArgumentException("Null object.");
            }
            nativeObjectInit(Thread.currentThread(), obj);
        }
    }

    public static void NewArray(Object obj) {
        if (engaged != 0) {
            if (obj == null) {
                throw new IllegalArgumentException("Null object.");
            }
            nativeNewArray(Thread.currentThread(), obj);
        }
    }

    public static void CallSite(int i2, int i3) {
        if (engaged != 0) {
            if (i2 < 0) {
                throw new IllegalArgumentException("Negative class index");
            }
            if (i3 < 0) {
                throw new IllegalArgumentException("Negative method index");
            }
            nativeCallSite(Thread.currentThread(), i2, i3);
        }
    }

    public static void ReturnSite(int i2, int i3) {
        if (engaged != 0) {
            if (i2 < 0) {
                throw new IllegalArgumentException("Negative class index");
            }
            if (i3 < 0) {
                throw new IllegalArgumentException("Negative method index");
            }
            nativeReturnSite(Thread.currentThread(), i2, i3);
        }
    }
}
