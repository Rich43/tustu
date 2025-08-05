package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.MeshView;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.ResourceFactory;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGShape3D.class */
public abstract class NGShape3D extends NGNode {
    private NGPhongMaterial material;
    private DrawMode drawMode;
    private CullFace cullFace;
    private boolean materialDirty = false;
    private boolean drawModeDirty = false;
    NGTriangleMesh mesh;
    private MeshView meshView;

    public void setMaterial(NGPhongMaterial material) {
        this.material = material;
        this.materialDirty = true;
        visualsChanged();
    }

    public void setDrawMode(Object drawMode) {
        this.drawMode = (DrawMode) drawMode;
        this.drawModeDirty = true;
        visualsChanged();
    }

    public void setCullFace(Object cullFace) {
        this.cullFace = (CullFace) cullFace;
        visualsChanged();
    }

    void invalidate() {
        this.meshView = null;
        visualsChanged();
    }

    private void renderMeshView(Graphics g2) {
        NGLightBase lightBase;
        g2.setup3DRendering();
        ResourceFactory rf = g2.getResourceFactory();
        if (rf == null || rf.isDisposed()) {
            return;
        }
        if (this.meshView != null && !this.meshView.isValid()) {
            this.meshView.dispose();
            this.meshView = null;
        }
        if (this.meshView == null && this.mesh != null) {
            this.meshView = rf.createMeshView(this.mesh.createMesh(rf));
            this.drawModeDirty = true;
            this.materialDirty = true;
        }
        if (this.meshView == null || !this.mesh.validate()) {
            return;
        }
        Material mtl = this.material.createMaterial(rf);
        if (this.materialDirty) {
            this.meshView.setMaterial(mtl);
            this.materialDirty = false;
        }
        int cullingMode = this.cullFace.ordinal();
        if (this.cullFace.ordinal() != MeshView.CULL_NONE && g2.getTransformNoClone().getDeterminant() < 0.0d) {
            cullingMode = cullingMode == MeshView.CULL_BACK ? MeshView.CULL_FRONT : MeshView.CULL_BACK;
        }
        this.meshView.setCullingMode(cullingMode);
        if (this.drawModeDirty) {
            this.meshView.setWireframe(this.drawMode == DrawMode.LINE);
            this.drawModeDirty = false;
        }
        int pointLightIdx = 0;
        if (g2.getLights() == null || g2.getLights()[0] == null) {
            this.meshView.setAmbientLight(0.0f, 0.0f, 0.0f);
            Vec3d cameraPos = g2.getCameraNoClone().getPositionInWorld(null);
            pointLightIdx = 0 + 1;
            this.meshView.setPointLight(0, (float) cameraPos.f11930x, (float) cameraPos.f11931y, (float) cameraPos.f11932z, 1.0f, 1.0f, 1.0f, 1.0f);
        } else {
            float ambientRed = 0.0f;
            float ambientBlue = 0.0f;
            float ambientGreen = 0.0f;
            for (int i2 = 0; i2 < g2.getLights().length && (lightBase = g2.getLights()[i2]) != null; i2++) {
                if (lightBase.affects(this)) {
                    float rL = lightBase.getColor().getRed();
                    float gL = lightBase.getColor().getGreen();
                    float bL2 = lightBase.getColor().getBlue();
                    if (lightBase instanceof NGPointLight) {
                        NGPointLight light = (NGPointLight) lightBase;
                        if (rL != 0.0f || gL != 0.0f || bL2 != 0.0f) {
                            Affine3D lightWT = light.getWorldTransform();
                            int i3 = pointLightIdx;
                            pointLightIdx++;
                            this.meshView.setPointLight(i3, (float) lightWT.getMxt(), (float) lightWT.getMyt(), (float) lightWT.getMzt(), rL, gL, bL2, 1.0f);
                        }
                    } else if (lightBase instanceof NGAmbientLight) {
                        ambientRed += rL;
                        ambientGreen += gL;
                        ambientBlue += bL2;
                    }
                }
            }
            this.meshView.setAmbientLight(saturate(ambientRed), saturate(ambientGreen), saturate(ambientBlue));
        }
        while (pointLightIdx < 3) {
            int i4 = pointLightIdx;
            pointLightIdx++;
            this.meshView.setPointLight(i4, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        }
        this.meshView.render(g2);
    }

    private static float saturate(float value) {
        if (value >= 1.0f) {
            return 1.0f;
        }
        if (value < 0.0f) {
            return 0.0f;
        }
        return value;
    }

    public void setMesh(NGTriangleMesh triangleMesh) {
        this.mesh = triangleMesh;
        this.meshView = null;
        visualsChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D) || this.material == null || (g2 instanceof PrinterGraphics)) {
            return;
        }
        renderMeshView(g2);
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    boolean isShape3D() {
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return false;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void release() {
    }
}
