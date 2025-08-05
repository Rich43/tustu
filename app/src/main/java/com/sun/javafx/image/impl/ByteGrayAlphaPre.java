package com.sun.javafx.image.impl;

import com.sun.javafx.image.BytePixelAccessor;
import com.sun.javafx.image.BytePixelGetter;
import com.sun.javafx.image.BytePixelSetter;
import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.javafx.image.impl.ByteGrayAlpha;

/* loaded from: jfxrt.jar:com/sun/javafx/image/impl/ByteGrayAlphaPre.class */
public class ByteGrayAlphaPre {
    public static final BytePixelGetter getter = ByteGrayAlpha.Accessor.premul;
    public static final BytePixelSetter setter = ByteGrayAlpha.Accessor.premul;
    public static final BytePixelAccessor accessor = ByteGrayAlpha.Accessor.premul;

    public static ByteToBytePixelConverter ToByteBgraPreConverter() {
        return ByteGrayAlpha.ToByteBgraSameConv.premul;
    }
}
