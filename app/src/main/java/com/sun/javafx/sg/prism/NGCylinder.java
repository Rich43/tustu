package com.sun.javafx.sg.prism;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCylinder.class */
public class NGCylinder extends NGShape3D {
    public void updateMesh(NGTriangleMesh mesh) {
        this.mesh = mesh;
        invalidate();
    }
}
