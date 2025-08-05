package com.intel.bluetooth;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/NativeTestInterfaces.class */
class NativeTestInterfaces {
    static native byte[] testUUIDConversion(byte[] bArr);

    static native long testReceiveBufferCreate(int i2);

    static native void testReceiveBufferClose(long j2);

    static native int testReceiveBufferWrite(long j2, byte[] bArr);

    static native int testReceiveBufferRead(long j2, byte[] bArr);

    static native int testReceiveBufferRead(long j2);

    static native int testReceiveBufferSkip(long j2, int i2);

    static native int testReceiveBufferAvailable(long j2);

    static native boolean testReceiveBufferIsOverflown(long j2);

    static native boolean testReceiveBufferIsCorrupted(long j2);

    static native void testThrowException(int i2) throws Exception;

    static native void testDebug(int i2, String str);

    static native byte[] testOsXDataElementConversion(int i2, int i3, long j2, byte[] bArr);

    static native void testOsXRunnableLoop(int i2, int i3);

    static native boolean testWIDCOMMConstants();

    NativeTestInterfaces() {
    }

    static boolean loadDllMS() {
        return NativeLibLoader.isAvailable("intelbth");
    }

    static boolean loadDllWIDCOMM() {
        return NativeLibLoader.isAvailable("bluecove");
    }
}
