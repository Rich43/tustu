package com.sun.javafx.iio.common;

import java.nio.ByteBuffer;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/common/PushbroomScaler.class */
public interface PushbroomScaler {
    ByteBuffer getDestination();

    boolean putSourceScanline(byte[] bArr, int i2);
}
