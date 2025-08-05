package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.RenderJob;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/PaintRenderJob.class */
class PaintRenderJob extends RenderJob {
    private GlassScene scene;

    public PaintRenderJob(GlassScene gs, CompletionListener cl, Runnable r2) {
        super(r2, cl);
        this.scene = gs;
    }

    public GlassScene getScene() {
        return this.scene;
    }
}
