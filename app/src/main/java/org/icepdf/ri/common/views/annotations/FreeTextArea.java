package org.icepdf.ri.common.views.annotations;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.icepdf.ri.util.jxlayer.JXLayer;
import org.icepdf.ri.util.jxlayer.plaf.LayerUI;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/FreeTextArea.class */
public class FreeTextArea extends JTextArea {
    private ZoomProvider zoomProvider;
    private boolean active;

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/annotations/FreeTextArea$ZoomProvider.class */
    public interface ZoomProvider {
        float getZoom();
    }

    public FreeTextArea(ZoomProvider zoomProvider) {
        this.zoomProvider = zoomProvider;
        getDocument().putProperty("i18n", Boolean.TRUE.toString());
        putClientProperty("i18n", Boolean.TRUE.toString());
        LayerUI<JComponent> layerUI = new LayerUI<JComponent>() { // from class: org.icepdf.ri.common.views.annotations.FreeTextArea.1
            @Override // org.icepdf.ri.util.jxlayer.plaf.LayerUI, javax.swing.plaf.ComponentUI
            public void installUI(JComponent c2) {
                super.installUI(c2);
                ((JXLayer) c2).setLayerEventMask(48L);
            }

            @Override // org.icepdf.ri.util.jxlayer.plaf.LayerUI, javax.swing.plaf.ComponentUI
            public void uninstallUI(JComponent c2) {
                super.uninstallUI(c2);
                ((JXLayer) c2).setLayerEventMask(0L);
            }

            @Override // org.icepdf.ri.util.jxlayer.plaf.LayerUI
            public void eventDispatched(AWTEvent ae2, JXLayer<? extends JComponent> l2) {
                MouseEvent e2 = (MouseEvent) ae2;
                float zoom = FreeTextArea.this.zoomProvider.getZoom();
                MouseEvent newEvent = new MouseEvent((Component) e2.getSource(), e2.getID(), e2.getWhen(), e2.getModifiers(), (int) (e2.getX() / zoom), (int) (e2.getY() / zoom), e2.getClickCount(), e2.isPopupTrigger(), e2.getButton());
                e2.consume();
                FreeTextArea.this.processMouseEvent(newEvent);
                FreeTextArea.this.processMouseMotionEvent(newEvent);
            }
        };
        new JXLayer(this, layerUI);
    }

    @Override // javax.swing.JComponent
    protected void paintBorder(Graphics g2) {
        if (!this.active) {
            return;
        }
        super.paintBorder(g2);
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics g2) {
        if (!this.active) {
            return;
        }
        float zoom = this.zoomProvider.getZoom();
        Graphics2D g22 = (Graphics2D) g2;
        g22.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g22.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        AffineTransform old = g22.getTransform();
        g22.scale(zoom, zoom);
        super.paintComponent(g22);
        g22.setTransform(old);
    }

    @Override // java.awt.Component
    public void repaint(int x2, int y2, int width, int height) {
        super.repaint(0, 0, getWidth(), getHeight());
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
