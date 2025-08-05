package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.prism.PixelSource;
import com.sun.prism.PresentableState;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/SceneState.class */
class SceneState extends PresentableState {
    final GlassScene scene;
    private Color clearColor;
    private Paint currentPaint;
    private NGCamera camera;

    public SceneState(GlassScene vs) {
        this.scene = vs;
    }

    @Override // com.sun.prism.PresentableState
    public boolean isMSAA() {
        return this.scene.isMSAA();
    }

    public GlassScene getScene() {
        return this.scene;
    }

    public boolean isValid() {
        return (getWindow() == null || getView() == null || isViewClosed() || getWidth() <= 0 || getHeight() <= 0) ? false : true;
    }

    @Override // com.sun.prism.PresentableState
    public void update() {
        this.view = this.scene.getPlatformView();
        this.clearColor = this.scene.getClearColor();
        this.currentPaint = this.scene.getCurrentPaint();
        super.update();
        this.camera = this.scene.getCamera();
        if (this.camera != null) {
            this.viewWidth = (int) this.camera.getViewWidth();
            this.viewHeight = (int) this.camera.getViewHeight();
        }
    }

    @Override // com.sun.prism.PresentableState
    public void uploadPixels(PixelSource source) {
        Application.invokeLater(() -> {
            if (isValid()) {
                super.uploadPixels(source);
            } else {
                source.skipLatestPixels();
            }
        });
    }

    Color getClearColor() {
        return this.clearColor;
    }

    Paint getCurrentPaint() {
        return this.currentPaint;
    }

    NGCamera getCamera() {
        return this.camera;
    }
}
