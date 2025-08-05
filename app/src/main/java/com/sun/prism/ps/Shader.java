package com.sun.prism.ps;

import com.sun.prism.GraphicsResource;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/* loaded from: jfxrt.jar:com/sun/prism/ps/Shader.class */
public interface Shader extends GraphicsResource {
    void enable();

    void disable();

    boolean isValid();

    void setConstant(String str, int i2);

    void setConstant(String str, int i2, int i3);

    void setConstant(String str, int i2, int i3, int i4);

    void setConstant(String str, int i2, int i3, int i4, int i5);

    void setConstants(String str, IntBuffer intBuffer, int i2, int i3);

    void setConstant(String str, float f2);

    void setConstant(String str, float f2, float f3);

    void setConstant(String str, float f2, float f3, float f4);

    void setConstant(String str, float f2, float f3, float f4, float f5);

    void setConstants(String str, FloatBuffer floatBuffer, int i2, int i3);
}
