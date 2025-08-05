package de.muntjak.tinylookandfeel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollPaneUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyScrollPaneUI.class */
public class TinyScrollPaneUI extends MetalScrollPaneUI implements PropertyChangeListener {
    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyScrollPaneUI();
    }

    @Override // javax.swing.plaf.metal.MetalScrollPaneUI, javax.swing.plaf.basic.BasicScrollPaneUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        JScrollBar horizontalScrollBar = this.scrollpane.getHorizontalScrollBar();
        if (horizontalScrollBar != null) {
            horizontalScrollBar.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, Boolean.FALSE);
        }
        JScrollBar verticalScrollBar = this.scrollpane.getVerticalScrollBar();
        if (verticalScrollBar != null) {
            verticalScrollBar.putClientProperty(MetalScrollBarUI.FREE_STANDING_PROP, Boolean.FALSE);
        }
    }

    @Override // javax.swing.plaf.metal.MetalScrollPaneUI
    protected PropertyChangeListener createScrollBarSwapListener() {
        return this;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    }
}
