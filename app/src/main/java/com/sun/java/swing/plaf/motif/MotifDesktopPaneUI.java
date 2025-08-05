package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicDesktopPaneUI;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopPaneUI.class */
public class MotifDesktopPaneUI extends BasicDesktopPaneUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifDesktopPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopPaneUI
    protected void installDesktopManager() {
        this.desktopManager = this.desktop.getDesktopManager();
        if (this.desktopManager == null) {
            this.desktopManager = new MotifDesktopManager();
            this.desktop.setDesktopManager(this.desktopManager);
            ((MotifDesktopManager) this.desktopManager).adjustIcons(this.desktop);
        }
    }

    public Insets getInsets(JComponent jComponent) {
        return new Insets(0, 0, 0, 0);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopPaneUI$DragPane.class */
    private class DragPane extends JComponent {
        private DragPane() {
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            graphics.setColor(Color.darkGray);
            graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopPaneUI$MotifDesktopManager.class */
    private class MotifDesktopManager extends DefaultDesktopManager implements Serializable, UIResource {
        JComponent dragPane;
        boolean usingDragPane;
        private transient JLayeredPane layeredPaneForDragPane;
        int iconWidth;
        int iconHeight;

        private MotifDesktopManager() {
            this.usingDragPane = false;
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void setBoundsForFrame(JComponent jComponent, int i2, int i3, int i4, int i5) {
            if (!this.usingDragPane) {
                boolean z2 = (jComponent.getWidth() == i4 && jComponent.getHeight() == i5) ? false : true;
                Rectangle bounds = jComponent.getBounds();
                jComponent.setBounds(i2, i3, i4, i5);
                SwingUtilities.computeUnion(i2, i3, i4, i5, bounds);
                jComponent.getParent().repaint(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
                if (z2) {
                    jComponent.validate();
                    return;
                }
                return;
            }
            Rectangle bounds2 = this.dragPane.getBounds();
            this.dragPane.setBounds(i2, i3, i4, i5);
            SwingUtilities.computeUnion(i2, i3, i4, i5, bounds2);
            this.dragPane.getParent().repaint(bounds2.f12372x, bounds2.f12373y, bounds2.width, bounds2.height);
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void beginDraggingFrame(JComponent jComponent) {
            this.usingDragPane = false;
            if (jComponent.getParent() instanceof JLayeredPane) {
                if (this.dragPane == null) {
                    this.dragPane = new DragPane();
                }
                this.layeredPaneForDragPane = (JLayeredPane) jComponent.getParent();
                this.layeredPaneForDragPane.setLayer(this.dragPane, Integer.MAX_VALUE);
                this.dragPane.setBounds(jComponent.getX(), jComponent.getY(), jComponent.getWidth(), jComponent.getHeight());
                this.layeredPaneForDragPane.add(this.dragPane);
                this.usingDragPane = true;
            }
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void dragFrame(JComponent jComponent, int i2, int i3) {
            setBoundsForFrame(jComponent, i2, i3, jComponent.getWidth(), jComponent.getHeight());
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void endDraggingFrame(JComponent jComponent) {
            if (this.usingDragPane) {
                this.layeredPaneForDragPane.remove(this.dragPane);
                this.usingDragPane = false;
                if (jComponent instanceof JInternalFrame) {
                    setBoundsForFrame(jComponent, this.dragPane.getX(), this.dragPane.getY(), this.dragPane.getWidth(), this.dragPane.getHeight());
                } else if (jComponent instanceof JInternalFrame.JDesktopIcon) {
                    adjustBoundsForIcon((JInternalFrame.JDesktopIcon) jComponent, this.dragPane.getX(), this.dragPane.getY());
                }
            }
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void beginResizingFrame(JComponent jComponent, int i2) {
            this.usingDragPane = false;
            if (jComponent.getParent() instanceof JLayeredPane) {
                if (this.dragPane == null) {
                    this.dragPane = new DragPane();
                }
                JLayeredPane jLayeredPane = (JLayeredPane) jComponent.getParent();
                jLayeredPane.setLayer(this.dragPane, Integer.MAX_VALUE);
                this.dragPane.setBounds(jComponent.getX(), jComponent.getY(), jComponent.getWidth(), jComponent.getHeight());
                jLayeredPane.add(this.dragPane);
                this.usingDragPane = true;
            }
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void resizeFrame(JComponent jComponent, int i2, int i3, int i4, int i5) {
            setBoundsForFrame(jComponent, i2, i3, i4, i5);
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void endResizingFrame(JComponent jComponent) {
            if (this.usingDragPane) {
                ((JLayeredPane) jComponent.getParent()).remove(this.dragPane);
                this.usingDragPane = false;
                setBoundsForFrame(jComponent, this.dragPane.getX(), this.dragPane.getY(), this.dragPane.getWidth(), this.dragPane.getHeight());
            }
        }

        @Override // javax.swing.DefaultDesktopManager, javax.swing.DesktopManager
        public void iconifyFrame(JInternalFrame jInternalFrame) {
            JInternalFrame.JDesktopIcon desktopIcon = jInternalFrame.getDesktopIcon();
            Point location = desktopIcon.getLocation();
            adjustBoundsForIcon(desktopIcon, location.f12370x, location.f12371y);
            super.iconifyFrame(jInternalFrame);
        }

        protected void adjustIcons(JDesktopPane jDesktopPane) {
            Dimension preferredSize = new JInternalFrame.JDesktopIcon(new JInternalFrame()).getPreferredSize();
            this.iconWidth = preferredSize.width;
            this.iconHeight = preferredSize.height;
            for (JInternalFrame jInternalFrame : jDesktopPane.getAllFrames()) {
                JInternalFrame.JDesktopIcon desktopIcon = jInternalFrame.getDesktopIcon();
                Point location = desktopIcon.getLocation();
                adjustBoundsForIcon(desktopIcon, location.f12370x, location.f12371y);
            }
        }

        protected void adjustBoundsForIcon(JInternalFrame.JDesktopIcon jDesktopIcon, int i2, int i3) {
            JDesktopPane desktopPane = jDesktopIcon.getDesktopPane();
            int height = desktopPane.getHeight();
            int i4 = this.iconWidth;
            int i5 = this.iconHeight;
            desktopPane.repaint(i2, i3, i4, i5);
            int i6 = i2 < 0 ? 0 : i2;
            int i7 = i3 < 0 ? 0 : i3;
            int i8 = i7 >= height ? height - 1 : i7;
            int i9 = (i6 / i4) * i4;
            int i10 = height % i5;
            int i11 = (((i8 - i10) / i5) * i5) + i10;
            int i12 = i6 - i9;
            int i13 = i8 - i11;
            int i14 = i12 < i4 / 2 ? i9 : i9 + i4;
            int i15 = (i13 >= i5 / 2 && i11 + i5 < height) ? i11 + i5 : i11;
            int i16 = i15;
            while (getIconAt(desktopPane, jDesktopIcon, i14, i16) != null) {
                i14 += i4;
            }
            if (i14 > desktopPane.getWidth()) {
                return;
            }
            if (jDesktopIcon.getParent() != null) {
                setBoundsForFrame(jDesktopIcon, i14, i16, i4, i5);
            } else {
                jDesktopIcon.setLocation(i14, i16);
            }
        }

        protected JInternalFrame.JDesktopIcon getIconAt(JDesktopPane jDesktopPane, JInternalFrame.JDesktopIcon jDesktopIcon, int i2, int i3) {
            for (Component component : jDesktopPane.getComponents()) {
                if ((component instanceof JInternalFrame.JDesktopIcon) && component != jDesktopIcon) {
                    Point location = component.getLocation();
                    if (location.f12370x == i2 && location.f12371y == i3) {
                        return (JInternalFrame.JDesktopIcon) component;
                    }
                }
            }
            return null;
        }
    }
}
