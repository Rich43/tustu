package com.sun.pisces;

import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/pisces/JavaSurface.class */
public final class JavaSurface extends AbstractSurface {
    private IntBuffer dataBuffer;
    private int[] dataInt;

    private native void initialize(int i2, int i3, int i4);

    public JavaSurface(int[] dataInt, int dataType, int width, int height) {
        super(width, height);
        if (dataInt.length / width < height) {
            throw new IllegalArgumentException("width(=" + width + ") * height(=" + height + ") is greater than dataInt.length(=" + dataInt.length + ")");
        }
        this.dataInt = dataInt;
        this.dataBuffer = IntBuffer.wrap(this.dataInt);
        initialize(dataType, width, height);
    }

    public IntBuffer getDataIntBuffer() {
        return this.dataBuffer;
    }
}
