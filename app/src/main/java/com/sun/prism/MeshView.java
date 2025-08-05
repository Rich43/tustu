package com.sun.prism;

import javafx.scene.shape.CullFace;

/* loaded from: jfxrt.jar:com/sun/prism/MeshView.class */
public interface MeshView extends GraphicsResource {
    public static final int CULL_NONE = CullFace.NONE.ordinal();
    public static final int CULL_BACK = CullFace.BACK.ordinal();
    public static final int CULL_FRONT = CullFace.FRONT.ordinal();

    void setCullingMode(int i2);

    void setMaterial(Material material);

    void setWireframe(boolean z2);

    void setAmbientLight(float f2, float f3, float f4);

    void setPointLight(int i2, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    void render(Graphics graphics);

    boolean isValid();
}
