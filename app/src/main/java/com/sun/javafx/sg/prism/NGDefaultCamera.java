package com.sun.javafx.sg.prism;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGDefaultCamera.class */
public class NGDefaultCamera extends NGParallelCamera {
    public void validate(int w2, int h2) {
        if (w2 != this.viewWidth || h2 != this.viewHeight) {
            setViewWidth(w2);
            setViewHeight(h2);
            double halfDepth = w2 > h2 ? w2 / 2.0d : h2 / 2.0d;
            this.projViewTx.ortho(0.0d, w2, h2, 0.0d, -halfDepth, halfDepth);
        }
    }
}
