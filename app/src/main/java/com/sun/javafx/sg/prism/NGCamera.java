package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.prism.Graphics;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCamera.class */
public abstract class NGCamera extends NGNode {
    public static final NGCamera INSTANCE = new NGDefaultCamera();
    protected Affine3D worldTransform = new Affine3D();
    protected double viewWidth = 1.0d;
    protected double viewHeight = 1.0d;
    protected double zNear = 0.1d;
    protected double zFar = 100.0d;
    private Vec3d worldPosition = new Vec3d();
    protected GeneralTransform3D projViewTx = new GeneralTransform3D();

    public abstract PickRay computePickRay(float f2, float f3, PickRay pickRay);

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void doRender(Graphics g2) {
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean hasOverlappingContents() {
        return false;
    }

    public void setNearClip(float nearClip) {
        this.zNear = nearClip;
    }

    public double getNearClip() {
        return this.zNear;
    }

    public void setFarClip(float farClip) {
        this.zFar = farClip;
    }

    public double getFarClip() {
        return this.zFar;
    }

    public void setViewWidth(double viewWidth) {
        this.viewWidth = viewWidth;
    }

    public double getViewWidth() {
        return this.viewWidth;
    }

    public void setViewHeight(double viewHeight) {
        this.viewHeight = viewHeight;
    }

    public double getViewHeight() {
        return this.viewHeight;
    }

    public void setProjViewTransform(GeneralTransform3D projViewTx) {
        this.projViewTx.set(projViewTx);
    }

    public void setPosition(Vec3d position) {
        this.worldPosition.set(position);
    }

    public void setWorldTransform(Affine3D localToWorldTx) {
        this.worldTransform.setTransform(localToWorldTx);
    }

    public GeneralTransform3D getProjViewTx(GeneralTransform3D tx) {
        if (tx == null) {
            tx = new GeneralTransform3D();
        }
        return tx.set(this.projViewTx);
    }

    public Vec3d getPositionInWorld(Vec3d pos) {
        if (pos == null) {
            pos = new Vec3d();
        }
        pos.set(this.worldPosition);
        return pos;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    public void release() {
    }
}
