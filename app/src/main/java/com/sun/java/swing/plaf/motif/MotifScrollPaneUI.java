package com.sun.java.swing.plaf.motif;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifScrollPaneUI.class */
public class MotifScrollPaneUI extends BasicScrollPaneUI {
    private static final Border vsbMarginBorderR = new EmptyBorder(0, 4, 0, 0);
    private static final Border vsbMarginBorderL = new EmptyBorder(0, 0, 0, 4);
    private static final Border hsbMarginBorder = new EmptyBorder(4, 0, 0, 0);
    private CompoundBorder vsbBorder;
    private CompoundBorder hsbBorder;
    private PropertyChangeListener propertyChangeHandler;

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void installListeners(JScrollPane jScrollPane) {
        super.installListeners(jScrollPane);
        this.propertyChangeHandler = createPropertyChangeHandler();
        jScrollPane.addPropertyChangeListener(this.propertyChangeHandler);
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void uninstallListeners(JComponent jComponent) {
        super.uninstallListeners(jComponent);
        jComponent.removePropertyChangeListener(this.propertyChangeHandler);
    }

    private PropertyChangeListener createPropertyChangeHandler() {
        return new PropertyChangeListener() { // from class: com.sun.java.swing.plaf.motif.MotifScrollPaneUI.1
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                JScrollPane jScrollPane;
                JScrollBar verticalScrollBar;
                if (propertyChangeEvent.getPropertyName().equals("componentOrientation") && (verticalScrollBar = (jScrollPane = (JScrollPane) propertyChangeEvent.getSource()).getVerticalScrollBar()) != null && MotifScrollPaneUI.this.vsbBorder != null && verticalScrollBar.getBorder() == MotifScrollPaneUI.this.vsbBorder) {
                    if (MotifGraphicsUtils.isLeftToRight(jScrollPane)) {
                        MotifScrollPaneUI.this.vsbBorder = new CompoundBorder(MotifScrollPaneUI.vsbMarginBorderR, MotifScrollPaneUI.this.vsbBorder.getInsideBorder());
                    } else {
                        MotifScrollPaneUI.this.vsbBorder = new CompoundBorder(MotifScrollPaneUI.vsbMarginBorderL, MotifScrollPaneUI.this.vsbBorder.getInsideBorder());
                    }
                    verticalScrollBar.setBorder(MotifScrollPaneUI.this.vsbBorder);
                }
            }
        };
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void installDefaults(JScrollPane jScrollPane) {
        super.installDefaults(jScrollPane);
        JScrollBar verticalScrollBar = jScrollPane.getVerticalScrollBar();
        if (verticalScrollBar != null) {
            if (MotifGraphicsUtils.isLeftToRight(jScrollPane)) {
                this.vsbBorder = new CompoundBorder(vsbMarginBorderR, verticalScrollBar.getBorder());
            } else {
                this.vsbBorder = new CompoundBorder(vsbMarginBorderL, verticalScrollBar.getBorder());
            }
            verticalScrollBar.setBorder(this.vsbBorder);
        }
        JScrollBar horizontalScrollBar = jScrollPane.getHorizontalScrollBar();
        if (horizontalScrollBar != null) {
            this.hsbBorder = new CompoundBorder(hsbMarginBorder, horizontalScrollBar.getBorder());
            horizontalScrollBar.setBorder(this.hsbBorder);
        }
    }

    @Override // javax.swing.plaf.basic.BasicScrollPaneUI
    protected void uninstallDefaults(JScrollPane jScrollPane) {
        super.uninstallDefaults(jScrollPane);
        JScrollBar verticalScrollBar = this.scrollpane.getVerticalScrollBar();
        if (verticalScrollBar != null) {
            if (verticalScrollBar.getBorder() == this.vsbBorder) {
                verticalScrollBar.setBorder(null);
            }
            this.vsbBorder = null;
        }
        JScrollBar horizontalScrollBar = this.scrollpane.getHorizontalScrollBar();
        if (horizontalScrollBar != null) {
            if (horizontalScrollBar.getBorder() == this.hsbBorder) {
                horizontalScrollBar.setBorder(null);
            }
            this.hsbBorder = null;
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifScrollPaneUI();
    }
}
