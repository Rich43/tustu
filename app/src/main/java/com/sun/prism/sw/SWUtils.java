package com.sun.prism.sw;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.pisces.Transform6;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWUtils.class */
final class SWUtils {
    static final int TO_PISCES = 65536;

    SWUtils() {
    }

    static int fastFloor(float f2) {
        int n2 = (int) f2;
        return (f2 >= 0.0f || f2 == ((float) n2)) ? n2 : n2 - 1;
    }

    static int fastCeil(float f2) {
        int n2 = (int) f2;
        return (f2 < 0.0f || f2 == ((float) n2)) ? n2 : n2 + 1;
    }

    static void convertToPiscesTransform(BaseTransform prismTx, Transform6 piscesTx) {
        piscesTx.m00 = (int) (65536.0d * prismTx.getMxx());
        piscesTx.m10 = (int) (65536.0d * prismTx.getMyx());
        piscesTx.m01 = (int) (65536.0d * prismTx.getMxy());
        piscesTx.m11 = (int) (65536.0d * prismTx.getMyy());
        piscesTx.m02 = (int) (65536.0d * prismTx.getMxt());
        piscesTx.m12 = (int) (65536.0d * prismTx.getMyt());
    }
}
