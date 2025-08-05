package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IUnknown.class */
class IUnknown {
    long ptr;

    IUnknown(long ptr) {
        this.ptr = ptr;
    }

    int AddRef() {
        return OS.AddRef(this.ptr);
    }

    int Release() {
        int result = 0;
        if (this.ptr != 0) {
            result = OS.Release(this.ptr);
            this.ptr = 0L;
        }
        return result;
    }
}
