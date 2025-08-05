package javax.swing;

import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import javax.swing.JInternalFrame;
import sun.awt.AWTAccessor;
import sun.awt.SunToolkit;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:javax/swing/DefaultDesktopManager.class */
public class DefaultDesktopManager implements DesktopManager, Serializable {
    static final String HAS_BEEN_ICONIFIED_PROPERTY = "wasIconOnce";
    static final int DEFAULT_DRAG_MODE = 0;
    static final int OUTLINE_DRAG_MODE = 1;
    static final int FASTER_DRAG_MODE = 2;
    private transient boolean didDrag;
    int dragMode = 0;
    private transient Rectangle currentBounds = null;
    private transient Graphics desktopGraphics = null;
    private transient Rectangle desktopBounds = null;
    private transient Rectangle[] floatingItems = new Rectangle[0];
    private transient Point currentLoc = null;

    @Override // javax.swing.DesktopManager
    public void openFrame(JInternalFrame jInternalFrame) {
        if (jInternalFrame.getDesktopIcon().getParent() != null) {
            jInternalFrame.getDesktopIcon().getParent().add(jInternalFrame);
            removeIconFor(jInternalFrame);
        }
    }

    @Override // javax.swing.DesktopManager
    public void closeFrame(JInternalFrame jInternalFrame) {
        JDesktopPane desktopPane = jInternalFrame.getDesktopPane();
        if (desktopPane == null) {
            return;
        }
        boolean zIsSelected = jInternalFrame.isSelected();
        Container parent = jInternalFrame.getParent();
        JInternalFrame nextFrame = null;
        if (zIsSelected) {
            nextFrame = desktopPane.getNextFrame(jInternalFrame);
            try {
                jInternalFrame.setSelected(false);
            } catch (PropertyVetoException e2) {
            }
        }
        if (parent != null) {
            parent.remove(jInternalFrame);
            parent.repaint(jInternalFrame.getX(), jInternalFrame.getY(), jInternalFrame.getWidth(), jInternalFrame.getHeight());
        }
        removeIconFor(jInternalFrame);
        if (jInternalFrame.getNormalBounds() != null) {
            jInternalFrame.setNormalBounds(null);
        }
        if (wasIcon(jInternalFrame)) {
            setWasIcon(jInternalFrame, null);
        }
        if (nextFrame != null) {
            try {
                nextFrame.setSelected(true);
            } catch (PropertyVetoException e3) {
            }
        } else if (zIsSelected && desktopPane.getComponentCount() == 0) {
            desktopPane.requestFocus();
        }
    }

    @Override // javax.swing.DesktopManager
    public void maximizeFrame(JInternalFrame jInternalFrame) {
        if (jInternalFrame.isIcon()) {
            try {
                jInternalFrame.setIcon(false);
            } catch (PropertyVetoException e2) {
            }
        } else {
            jInternalFrame.setNormalBounds(jInternalFrame.getBounds());
            Rectangle bounds = jInternalFrame.getParent().getBounds();
            setBoundsForFrame(jInternalFrame, 0, 0, bounds.width, bounds.height);
        }
        try {
            jInternalFrame.setSelected(true);
        } catch (PropertyVetoException e3) {
        }
    }

    @Override // javax.swing.DesktopManager
    public void minimizeFrame(JInternalFrame jInternalFrame) {
        if (jInternalFrame.isIcon()) {
            iconifyFrame(jInternalFrame);
        } else if (jInternalFrame.getNormalBounds() != null) {
            Rectangle normalBounds = jInternalFrame.getNormalBounds();
            jInternalFrame.setNormalBounds(null);
            try {
                jInternalFrame.setSelected(true);
            } catch (PropertyVetoException e2) {
            }
            setBoundsForFrame(jInternalFrame, normalBounds.f12372x, normalBounds.f12373y, normalBounds.width, normalBounds.height);
        }
    }

    @Override // javax.swing.DesktopManager
    public void iconifyFrame(JInternalFrame jInternalFrame) {
        Container parent = jInternalFrame.getParent();
        JDesktopPane desktopPane = jInternalFrame.getDesktopPane();
        boolean zIsSelected = jInternalFrame.isSelected();
        JInternalFrame.JDesktopIcon desktopIcon = jInternalFrame.getDesktopIcon();
        if (!wasIcon(jInternalFrame)) {
            Rectangle boundsForIconOf = getBoundsForIconOf(jInternalFrame);
            desktopIcon.setBounds(boundsForIconOf.f12372x, boundsForIconOf.f12373y, boundsForIconOf.width, boundsForIconOf.height);
            desktopIcon.revalidate();
            setWasIcon(jInternalFrame, Boolean.TRUE);
        }
        if (parent == null || desktopPane == null) {
            return;
        }
        if (parent instanceof JLayeredPane) {
            JLayeredPane.putLayer(desktopIcon, JLayeredPane.getLayer((JComponent) jInternalFrame));
        }
        if (!jInternalFrame.isMaximum()) {
            jInternalFrame.setNormalBounds(jInternalFrame.getBounds());
        }
        desktopPane.setComponentOrderCheckingEnabled(false);
        parent.remove(jInternalFrame);
        parent.add(desktopIcon);
        desktopPane.setComponentOrderCheckingEnabled(true);
        parent.repaint(jInternalFrame.getX(), jInternalFrame.getY(), jInternalFrame.getWidth(), jInternalFrame.getHeight());
        if (zIsSelected && desktopPane.selectFrame(true) == null) {
            jInternalFrame.restoreSubcomponentFocus();
        }
    }

    @Override // javax.swing.DesktopManager
    public void deiconifyFrame(JInternalFrame jInternalFrame) {
        Container parent = jInternalFrame.getDesktopIcon().getParent();
        JDesktopPane desktopPane = jInternalFrame.getDesktopPane();
        if (parent != null && desktopPane != null) {
            parent.add(jInternalFrame);
            if (jInternalFrame.isMaximum()) {
                Rectangle bounds = parent.getBounds();
                if (jInternalFrame.getWidth() != bounds.width || jInternalFrame.getHeight() != bounds.height) {
                    setBoundsForFrame(jInternalFrame, 0, 0, bounds.width, bounds.height);
                }
            }
            removeIconFor(jInternalFrame);
            if (jInternalFrame.isSelected()) {
                jInternalFrame.moveToFront();
                jInternalFrame.restoreSubcomponentFocus();
            } else {
                try {
                    jInternalFrame.setSelected(true);
                } catch (PropertyVetoException e2) {
                }
            }
        }
    }

    @Override // javax.swing.DesktopManager
    public void activateFrame(JInternalFrame jInternalFrame) {
        Container parent = jInternalFrame.getParent();
        JDesktopPane desktopPane = jInternalFrame.getDesktopPane();
        JInternalFrame selectedFrame = desktopPane == null ? null : desktopPane.getSelectedFrame();
        if (parent == null && jInternalFrame.getDesktopIcon().getParent() == null) {
            return;
        }
        if (selectedFrame == null) {
            if (desktopPane != null) {
                desktopPane.setSelectedFrame(jInternalFrame);
            }
        } else if (selectedFrame != jInternalFrame) {
            if (selectedFrame.isSelected()) {
                try {
                    selectedFrame.setSelected(false);
                } catch (PropertyVetoException e2) {
                }
            }
            if (desktopPane != null) {
                desktopPane.setSelectedFrame(jInternalFrame);
            }
        }
        jInternalFrame.moveToFront();
    }

    @Override // javax.swing.DesktopManager
    public void deactivateFrame(JInternalFrame jInternalFrame) {
        JDesktopPane desktopPane = jInternalFrame.getDesktopPane();
        if ((desktopPane == null ? null : desktopPane.getSelectedFrame()) == jInternalFrame) {
            desktopPane.setSelectedFrame(null);
        }
    }

    @Override // javax.swing.DesktopManager
    public void beginDraggingFrame(JComponent jComponent) {
        setupDragMode(jComponent);
        if (this.dragMode == 2) {
            Container parent = jComponent.getParent();
            this.floatingItems = findFloatingItems(jComponent);
            this.currentBounds = jComponent.getBounds();
            if (parent instanceof JComponent) {
                this.desktopBounds = ((JComponent) parent).getVisibleRect();
            } else {
                this.desktopBounds = parent.getBounds();
                Rectangle rectangle = this.desktopBounds;
                this.desktopBounds.f12373y = 0;
                rectangle.f12372x = 0;
            }
            this.desktopGraphics = JComponent.safelyGetGraphics(parent);
            ((JInternalFrame) jComponent).isDragging = true;
            this.didDrag = false;
        }
    }

    private void setupDragMode(JComponent jComponent) {
        JDesktopPane desktopPane = getDesktopPane(jComponent);
        Container parent = jComponent.getParent();
        this.dragMode = 0;
        if (desktopPane != null) {
            String str = (String) desktopPane.getClientProperty("JDesktopPane.dragMode");
            Window windowAncestor = SwingUtilities.getWindowAncestor(jComponent);
            if (windowAncestor != null && !AWTUtilities.isWindowOpaque(windowAncestor)) {
                this.dragMode = 0;
                return;
            }
            if (str != null && str.equals("outline")) {
                this.dragMode = 1;
                return;
            }
            if (str != null && str.equals("faster") && (jComponent instanceof JInternalFrame) && ((JInternalFrame) jComponent).isOpaque() && (parent == null || parent.isOpaque())) {
                this.dragMode = 2;
                return;
            }
            if (desktopPane.getDragMode() == 1) {
                this.dragMode = 1;
            } else if (desktopPane.getDragMode() == 0 && (jComponent instanceof JInternalFrame) && ((JInternalFrame) jComponent).isOpaque()) {
                this.dragMode = 2;
            } else {
                this.dragMode = 0;
            }
        }
    }

    @Override // javax.swing.DesktopManager
    public void dragFrame(JComponent jComponent, int i2, int i3) {
        if (this.dragMode != 1) {
            if (this.dragMode == 2) {
                dragFrameFaster(jComponent, i2, i3);
                return;
            } else {
                setBoundsForFrame(jComponent, i2, i3, jComponent.getWidth(), jComponent.getHeight());
                return;
            }
        }
        JDesktopPane desktopPane = getDesktopPane(jComponent);
        if (desktopPane != null) {
            Graphics graphicsSafelyGetGraphics = JComponent.safelyGetGraphics(desktopPane);
            graphicsSafelyGetGraphics.setXORMode(Color.white);
            if (this.currentLoc != null) {
                graphicsSafelyGetGraphics.drawRect(this.currentLoc.f12370x, this.currentLoc.f12371y, jComponent.getWidth() - 1, jComponent.getHeight() - 1);
            }
            graphicsSafelyGetGraphics.drawRect(i2, i3, jComponent.getWidth() - 1, jComponent.getHeight() - 1);
            if (!((SunGraphics2D) graphicsSafelyGetGraphics).getSurfaceData().isSurfaceLost()) {
                this.currentLoc = new Point(i2, i3);
            }
            graphicsSafelyGetGraphics.dispose();
        }
    }

    @Override // javax.swing.DesktopManager
    public void endDraggingFrame(JComponent jComponent) {
        if (this.dragMode == 1 && this.currentLoc != null) {
            setBoundsForFrame(jComponent, this.currentLoc.f12370x, this.currentLoc.f12371y, jComponent.getWidth(), jComponent.getHeight());
            this.currentLoc = null;
        } else if (this.dragMode == 2) {
            this.currentBounds = null;
            if (this.desktopGraphics != null) {
                this.desktopGraphics.dispose();
                this.desktopGraphics = null;
            }
            this.desktopBounds = null;
            ((JInternalFrame) jComponent).isDragging = false;
        }
    }

    @Override // javax.swing.DesktopManager
    public void beginResizingFrame(JComponent jComponent, int i2) {
        setupDragMode(jComponent);
    }

    @Override // javax.swing.DesktopManager
    public void resizeFrame(JComponent jComponent, int i2, int i3, int i4, int i5) {
        if (this.dragMode == 0 || this.dragMode == 2) {
            setBoundsForFrame(jComponent, i2, i3, i4, i5);
            return;
        }
        JDesktopPane desktopPane = getDesktopPane(jComponent);
        if (desktopPane != null) {
            Graphics graphicsSafelyGetGraphics = JComponent.safelyGetGraphics(desktopPane);
            graphicsSafelyGetGraphics.setXORMode(Color.white);
            if (this.currentBounds != null) {
                graphicsSafelyGetGraphics.drawRect(this.currentBounds.f12372x, this.currentBounds.f12373y, this.currentBounds.width - 1, this.currentBounds.height - 1);
            }
            graphicsSafelyGetGraphics.drawRect(i2, i3, i4 - 1, i5 - 1);
            if (!((SunGraphics2D) graphicsSafelyGetGraphics).getSurfaceData().isSurfaceLost()) {
                this.currentBounds = new Rectangle(i2, i3, i4, i5);
            }
            graphicsSafelyGetGraphics.setPaintMode();
            graphicsSafelyGetGraphics.dispose();
        }
    }

    @Override // javax.swing.DesktopManager
    public void endResizingFrame(JComponent jComponent) {
        if (this.dragMode == 1 && this.currentBounds != null) {
            setBoundsForFrame(jComponent, this.currentBounds.f12372x, this.currentBounds.f12373y, this.currentBounds.width, this.currentBounds.height);
            this.currentBounds = null;
        }
    }

    @Override // javax.swing.DesktopManager
    public void setBoundsForFrame(JComponent jComponent, int i2, int i3, int i4, int i5) {
        jComponent.setBounds(i2, i3, i4, i5);
        jComponent.revalidate();
    }

    protected void removeIconFor(JInternalFrame jInternalFrame) {
        JInternalFrame.JDesktopIcon desktopIcon = jInternalFrame.getDesktopIcon();
        Container parent = desktopIcon.getParent();
        if (parent != null) {
            parent.remove(desktopIcon);
            parent.repaint(desktopIcon.getX(), desktopIcon.getY(), desktopIcon.getWidth(), desktopIcon.getHeight());
        }
    }

    protected Rectangle getBoundsForIconOf(JInternalFrame jInternalFrame) {
        JInternalFrame.JDesktopIcon desktopIcon = jInternalFrame.getDesktopIcon();
        Dimension preferredSize = desktopIcon.getPreferredSize();
        Container parent = jInternalFrame.getParent();
        if (parent == null) {
            parent = jInternalFrame.getDesktopIcon().getParent();
        }
        if (parent == null) {
            return new Rectangle(0, 0, preferredSize.width, preferredSize.height);
        }
        Rectangle bounds = parent.getBounds();
        Component[] components = parent.getComponents();
        Rectangle rectangle = null;
        JInternalFrame.JDesktopIcon desktopIcon2 = null;
        int i2 = 0;
        int i3 = bounds.height - preferredSize.height;
        int i4 = preferredSize.width;
        int i5 = preferredSize.height;
        boolean z2 = false;
        while (!z2) {
            rectangle = new Rectangle(i2, i3, i4, i5);
            z2 = true;
            int i6 = 0;
            while (true) {
                if (i6 < components.length) {
                    if (components[i6] instanceof JInternalFrame) {
                        desktopIcon2 = ((JInternalFrame) components[i6]).getDesktopIcon();
                    } else if (!(components[i6] instanceof JInternalFrame.JDesktopIcon)) {
                        continue;
                        i6++;
                    } else {
                        desktopIcon2 = (JInternalFrame.JDesktopIcon) components[i6];
                    }
                    if (desktopIcon2.equals(desktopIcon) || !rectangle.intersects(desktopIcon2.getBounds())) {
                        i6++;
                    } else {
                        z2 = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (desktopIcon2 == null) {
                return rectangle;
            }
            i2 += desktopIcon2.getBounds().width;
            if (i2 + i4 > bounds.width) {
                i2 = 0;
                i3 -= i5;
            }
        }
        return rectangle;
    }

    protected void setPreviousBounds(JInternalFrame jInternalFrame, Rectangle rectangle) {
        jInternalFrame.setNormalBounds(rectangle);
    }

    protected Rectangle getPreviousBounds(JInternalFrame jInternalFrame) {
        return jInternalFrame.getNormalBounds();
    }

    protected void setWasIcon(JInternalFrame jInternalFrame, Boolean bool) {
        if (bool != null) {
            jInternalFrame.putClientProperty(HAS_BEEN_ICONIFIED_PROPERTY, bool);
        }
    }

    protected boolean wasIcon(JInternalFrame jInternalFrame) {
        return jInternalFrame.getClientProperty(HAS_BEEN_ICONIFIED_PROPERTY) == Boolean.TRUE;
    }

    JDesktopPane getDesktopPane(JComponent jComponent) {
        JDesktopPane jDesktopPane = null;
        Container parent = jComponent.getParent();
        while (jDesktopPane == null) {
            if (parent instanceof JDesktopPane) {
                jDesktopPane = (JDesktopPane) parent;
            } else {
                if (parent == null) {
                    break;
                }
                parent = parent.getParent();
            }
        }
        return jDesktopPane;
    }

    private void dragFrameFaster(JComponent jComponent, int i2, int i3) {
        Rectangle[] rectangleArrComputeDifference;
        Rectangle rectangle = new Rectangle(this.currentBounds.f12372x, this.currentBounds.f12373y, this.currentBounds.width, this.currentBounds.height);
        this.currentBounds.f12372x = i2;
        this.currentBounds.f12373y = i3;
        if (this.didDrag) {
            emergencyCleanup(jComponent);
        } else {
            this.didDrag = true;
            ((JInternalFrame) jComponent).danger = false;
        }
        boolean zIsFloaterCollision = isFloaterCollision(rectangle, this.currentBounds);
        JComponent jComponent2 = (JComponent) jComponent.getParent();
        Rectangle rectangleIntersection = rectangle.intersection(this.desktopBounds);
        RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager(jComponent);
        repaintManagerCurrentManager.beginPaint();
        if (!zIsFloaterCollision) {
            try {
                repaintManagerCurrentManager.copyArea(jComponent2, this.desktopGraphics, rectangleIntersection.f12372x, rectangleIntersection.f12373y, rectangleIntersection.width, rectangleIntersection.height, i2 - rectangle.f12372x, i3 - rectangle.f12373y, true);
            } finally {
                repaintManagerCurrentManager.endPaint();
            }
        }
        jComponent.setBounds(this.currentBounds);
        if (!zIsFloaterCollision) {
            Rectangle rectangle2 = this.currentBounds;
            repaintManagerCurrentManager.notifyRepaintPerformed(jComponent2, rectangle2.f12372x, rectangle2.f12373y, rectangle2.width, rectangle2.height);
        }
        if (zIsFloaterCollision) {
            ((JInternalFrame) jComponent).isDragging = false;
            jComponent2.paintImmediately(this.currentBounds);
            ((JInternalFrame) jComponent).isDragging = true;
        }
        repaintManagerCurrentManager.markCompletelyClean(jComponent2);
        repaintManagerCurrentManager.markCompletelyClean(jComponent);
        if (rectangle.intersects(this.currentBounds)) {
            rectangleArrComputeDifference = SwingUtilities.computeDifference(rectangle, this.currentBounds);
        } else {
            rectangleArrComputeDifference = new Rectangle[]{rectangle};
        }
        for (int i4 = 0; i4 < rectangleArrComputeDifference.length; i4++) {
            jComponent2.paintImmediately(rectangleArrComputeDifference[i4]);
            Rectangle rectangle3 = rectangleArrComputeDifference[i4];
            repaintManagerCurrentManager.notifyRepaintPerformed(jComponent2, rectangle3.f12372x, rectangle3.f12373y, rectangle3.width, rectangle3.height);
        }
        if (!rectangleIntersection.equals(rectangle)) {
            Rectangle[] rectangleArrComputeDifference2 = SwingUtilities.computeDifference(rectangle, this.desktopBounds);
            for (int i5 = 0; i5 < rectangleArrComputeDifference2.length; i5++) {
                rectangleArrComputeDifference2[i5].f12372x += i2 - rectangle.f12372x;
                rectangleArrComputeDifference2[i5].f12373y += i3 - rectangle.f12373y;
                ((JInternalFrame) jComponent).isDragging = false;
                jComponent2.paintImmediately(rectangleArrComputeDifference2[i5]);
                ((JInternalFrame) jComponent).isDragging = true;
                Rectangle rectangle4 = rectangleArrComputeDifference2[i5];
                repaintManagerCurrentManager.notifyRepaintPerformed(jComponent2, rectangle4.f12372x, rectangle4.f12373y, rectangle4.width, rectangle4.height);
            }
        }
        Window windowAncestor = SwingUtilities.getWindowAncestor(jComponent);
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!windowAncestor.isOpaque() && (defaultToolkit instanceof SunToolkit) && ((SunToolkit) defaultToolkit).needUpdateWindow()) {
            AWTAccessor.getWindowAccessor().updateWindow(windowAncestor);
        }
    }

    private boolean isFloaterCollision(Rectangle rectangle, Rectangle rectangle2) {
        if (this.floatingItems.length == 0) {
            return false;
        }
        for (int i2 = 0; i2 < this.floatingItems.length; i2++) {
            if (rectangle.intersects(this.floatingItems[i2]) || rectangle2.intersects(this.floatingItems[i2])) {
                return true;
            }
        }
        return false;
    }

    private Rectangle[] findFloatingItems(JComponent jComponent) {
        Component[] components = jComponent.getParent().getComponents();
        int i2 = 0;
        while (i2 < components.length && components[i2] != jComponent) {
            i2++;
        }
        Rectangle[] rectangleArr = new Rectangle[i2];
        for (int i3 = 0; i3 < rectangleArr.length; i3++) {
            rectangleArr[i3] = components[i3].getBounds();
        }
        return rectangleArr;
    }

    private void emergencyCleanup(final JComponent jComponent) {
        if (((JInternalFrame) jComponent).danger) {
            SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.DefaultDesktopManager.1
                @Override // java.lang.Runnable
                public void run() {
                    ((JInternalFrame) jComponent).isDragging = false;
                    jComponent.paintImmediately(0, 0, jComponent.getWidth(), jComponent.getHeight());
                    ((JInternalFrame) jComponent).isDragging = true;
                }
            });
            ((JInternalFrame) jComponent).danger = false;
        }
    }
}
