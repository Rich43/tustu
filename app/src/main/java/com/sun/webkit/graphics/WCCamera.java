package com.sun.webkit.graphics;

import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGDefaultCamera;

/* loaded from: jfxrt.jar:com/sun/webkit/graphics/WCCamera.class */
public class WCCamera extends NGDefaultCamera {
    public static final NGCamera INSTANCE = new WCCamera();

    @Override // com.sun.javafx.sg.prism.NGDefaultCamera
    public void validate(int w2, int h2) {
        if (w2 != this.viewWidth || h2 != this.viewHeight) {
            setViewWidth(w2);
            setViewHeight(h2);
            this.projViewTx.ortho(0.0d, w2, h2, 0.0d, -9999999.0d, 99999.0d);
        }
    }
}
