package com.sun.javafx.webkit.prism;

import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/prism/WCRenderQueueImpl.class */
final class WCRenderQueueImpl extends WCRenderQueue {
    WCRenderQueueImpl(WCGraphicsContext gc) {
        super(gc);
    }

    WCRenderQueueImpl(WCRectangle clip, boolean opaque) {
        super(clip, opaque);
    }

    @Override // com.sun.webkit.graphics.WCRenderQueue
    protected void flush() {
        if (!isEmpty()) {
            PrismInvoker.invokeOnRenderThread(() -> {
                decode();
            });
        }
    }

    @Override // com.sun.webkit.graphics.WCRenderQueue
    protected void disposeGraphics() {
        PrismInvoker.invokeOnRenderThread(() -> {
            if (this.gc != null) {
                this.gc.dispose();
            }
        });
    }
}
