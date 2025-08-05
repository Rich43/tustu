package com.sun.webkit.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/CSSPrimitiveValueImpl.class */
public class CSSPrimitiveValueImpl extends CSSValueImpl implements CSSPrimitiveValue {
    public static final int CSS_UNKNOWN = 0;
    public static final int CSS_NUMBER = 1;
    public static final int CSS_PERCENTAGE = 2;
    public static final int CSS_EMS = 3;
    public static final int CSS_EXS = 4;
    public static final int CSS_PX = 5;
    public static final int CSS_CM = 6;
    public static final int CSS_MM = 7;
    public static final int CSS_IN = 8;
    public static final int CSS_PT = 9;
    public static final int CSS_PC = 10;
    public static final int CSS_DEG = 11;
    public static final int CSS_RAD = 12;
    public static final int CSS_GRAD = 13;
    public static final int CSS_MS = 14;
    public static final int CSS_S = 15;
    public static final int CSS_HZ = 16;
    public static final int CSS_KHZ = 17;
    public static final int CSS_DIMENSION = 18;
    public static final int CSS_STRING = 19;
    public static final int CSS_URI = 20;
    public static final int CSS_IDENT = 21;
    public static final int CSS_ATTR = 22;
    public static final int CSS_COUNTER = 23;
    public static final int CSS_RECT = 24;
    public static final int CSS_RGBCOLOR = 25;
    public static final int CSS_VW = 26;
    public static final int CSS_VH = 27;
    public static final int CSS_VMIN = 28;
    public static final int CSS_VMAX = 29;

    static native short getPrimitiveTypeImpl(long j2);

    static native void setFloatValueImpl(long j2, short s2, float f2);

    static native float getFloatValueImpl(long j2, short s2);

    static native void setStringValueImpl(long j2, short s2, String str);

    static native String getStringValueImpl(long j2);

    static native long getCounterValueImpl(long j2);

    static native long getRectValueImpl(long j2);

    static native long getRGBColorValueImpl(long j2);

    CSSPrimitiveValueImpl(long peer) {
        super(peer);
    }

    static CSSPrimitiveValue getImpl(long peer) {
        return (CSSPrimitiveValue) create(peer);
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public short getPrimitiveType() {
        return getPrimitiveTypeImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public void setFloatValue(short unitType, float floatValue) throws DOMException {
        setFloatValueImpl(getPeer(), unitType, floatValue);
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public float getFloatValue(short unitType) throws DOMException {
        return getFloatValueImpl(getPeer(), unitType);
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public void setStringValue(short stringType, String stringValue) throws DOMException {
        setStringValueImpl(getPeer(), stringType, stringValue);
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public String getStringValue() throws DOMException {
        return getStringValueImpl(getPeer());
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public Counter getCounterValue() throws DOMException {
        return CounterImpl.getImpl(getCounterValueImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public Rect getRectValue() throws DOMException {
        return RectImpl.getImpl(getRectValueImpl(getPeer()));
    }

    @Override // org.w3c.dom.css.CSSPrimitiveValue
    public RGBColor getRGBColorValue() throws DOMException {
        return RGBColorImpl.getImpl(getRGBColorValueImpl(getPeer()));
    }
}
