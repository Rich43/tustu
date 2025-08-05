package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Pixels;
import com.sun.prism.PixelSource;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/EmbeddedState.class */
final class EmbeddedState extends SceneState {
    public EmbeddedState(GlassScene vs) {
        super(vs);
    }

    @Override // com.sun.javafx.tk.quantum.SceneState, com.sun.prism.PresentableState
    public void uploadPixels(PixelSource source) {
        if (isValid()) {
            Pixels pixels = source.getLatestPixels();
            if (pixels != null) {
                try {
                    EmbeddedScene escene = (EmbeddedScene) this.scene;
                    escene.uploadPixels(pixels);
                    source.doneWithPixels(pixels);
                    return;
                } catch (Throwable th) {
                    source.doneWithPixels(pixels);
                    throw th;
                }
            }
            return;
        }
        source.skipLatestPixels();
    }

    @Override // com.sun.javafx.tk.quantum.SceneState
    public boolean isValid() {
        return this.scene != null && getWidth() > 0 && getHeight() > 0;
    }

    @Override // com.sun.javafx.tk.quantum.SceneState, com.sun.prism.PresentableState
    public void update() {
        super.update();
        float scale = ((EmbeddedScene) this.scene).getRenderScale();
        update(1.0f, scale, scale);
        if (this.scene != null) {
            this.isWindowVisible = true;
            this.isWindowMinimized = false;
        }
    }
}
