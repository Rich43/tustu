package com.sun.javafx.sg.prism.web;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGGroup;
import com.sun.prism.Graphics;
import com.sun.prism.PrinterGraphics;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCRectangle;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/web/NGWebView.class */
public final class NGWebView extends NGGroup {
    private static final Logger log = Logger.getLogger(NGWebView.class.getName());
    private volatile WebPage page;
    private volatile float width;
    private volatile float height;

    public void setPage(WebPage page) {
        this.page = page;
    }

    public void resize(float w2, float h2) {
        if (this.width != w2 || this.height != h2) {
            this.width = w2;
            this.height = h2;
            geometryChanged();
            if (this.page != null) {
                this.page.setBounds(0, 0, (int) w2, (int) h2);
            }
        }
    }

    public void update() {
        if (this.page != null) {
            BaseBounds clip = getClippedBounds(new RectBounds(), BaseTransform.IDENTITY_TRANSFORM);
            if (!clip.isEmpty()) {
                log.log(Level.FINEST, "updating rectangle: {0}", clip);
                this.page.updateContent(new WCRectangle(clip.getMinX(), clip.getMinY(), clip.getWidth(), clip.getHeight()));
            }
        }
    }

    public void requestRender() {
        visualsChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGGroup, com.sun.javafx.sg.prism.NGNode
    protected void renderContent(Graphics g2) {
        log.log(Level.FINEST, "rendering into {0}", g2);
        if (g2 == null || this.page == null || this.width <= 0.0f || this.height <= 0.0f) {
            return;
        }
        WCGraphicsContext gc = WCGraphicsManager.getGraphicsManager().createGraphicsContext(g2);
        try {
            if (g2 instanceof PrinterGraphics) {
                this.page.print(gc, 0, 0, (int) this.width, (int) this.height);
            } else {
                this.page.paint(gc, 0, 0, (int) this.width, (int) this.height);
            }
            gc.flush();
        } finally {
            gc.dispose();
        }
    }

    @Override // com.sun.javafx.sg.prism.NGGroup, com.sun.javafx.sg.prism.NGNode
    public boolean hasOverlappingContents() {
        return false;
    }

    @Override // com.sun.javafx.sg.prism.NGGroup, com.sun.javafx.sg.prism.NGNode
    protected boolean hasVisuals() {
        return true;
    }
}
