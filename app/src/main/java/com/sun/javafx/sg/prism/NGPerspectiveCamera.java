package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGPerspectiveCamera.class */
public class NGPerspectiveCamera extends NGCamera {
    private final boolean fixedEyeAtCameraZero;
    private double fovrad;
    private boolean verticalFieldOfView;

    public NGPerspectiveCamera(boolean fixedEyeAtCameraZero) {
        this.fixedEyeAtCameraZero = fixedEyeAtCameraZero;
    }

    public void setFieldOfView(float fieldOfViewDegrees) {
        this.fovrad = Math.toRadians(fieldOfViewDegrees);
    }

    public void setVerticalFieldOfView(boolean verticalFieldOfView) {
        this.verticalFieldOfView = verticalFieldOfView;
    }

    @Override // com.sun.javafx.sg.prism.NGCamera
    public PickRay computePickRay(float x2, float y2, PickRay pickRay) {
        return PickRay.computePerspectivePickRay(x2, y2, this.fixedEyeAtCameraZero, this.viewWidth, this.viewHeight, this.fovrad, this.verticalFieldOfView, this.worldTransform, this.zNear, this.zFar, pickRay);
    }
}
