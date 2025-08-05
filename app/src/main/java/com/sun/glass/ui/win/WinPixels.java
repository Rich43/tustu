package com.sun.glass.ui.win;

import com.sun.glass.ui.Pixels;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinPixels.class */
final class WinPixels extends Pixels {
    private static final int nativeFormat = _initIDs();

    private static native int _initIDs();

    @Override // com.sun.glass.ui.Pixels
    protected native void _fillDirectByteBuffer(ByteBuffer byteBuffer);

    @Override // com.sun.glass.ui.Pixels
    protected native void _attachInt(long j2, int i2, int i3, IntBuffer intBuffer, int[] iArr, int i4);

    @Override // com.sun.glass.ui.Pixels
    protected native void _attachByte(long j2, int i2, int i3, ByteBuffer byteBuffer, byte[] bArr, int i4);

    protected WinPixels(int width, int height, ByteBuffer data) {
        super(width, height, data);
    }

    protected WinPixels(int width, int height, IntBuffer data) {
        super(width, height, data);
    }

    protected WinPixels(int width, int height, IntBuffer data, float scale) {
        super(width, height, data, scale);
    }

    static int getNativeFormat_impl() {
        return nativeFormat;
    }
}
