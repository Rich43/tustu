package com.sun.javafx.sg.prism;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGSphere.class */
public class NGSphere extends NGShape3D {
    public void updateMesh(NGTriangleMesh mesh) {
        this.mesh = mesh;
        invalidate();
    }
}
