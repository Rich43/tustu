package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGParallelCamera.class */
public class NGParallelCamera extends NGCamera {
    @Override // com.sun.javafx.sg.prism.NGCamera
    public PickRay computePickRay(float x2, float y2, PickRay pickRay) {
        return PickRay.computeParallelPickRay(x2, y2, this.viewHeight, this.worldTransform, this.zNear, this.zFar, pickRay);
    }
}
