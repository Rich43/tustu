package sun.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.peer.ComponentPeer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessController;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.RepaintManager;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import sun.awt.AWTAccessor;
import sun.awt.DisplayChangedListener;
import sun.awt.LightweightFrame;
import sun.awt.OverrideNativeWindowHandle;
import sun.security.action.GetPropertyAction;
import sun.swing.SwingAccessor;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:sun/swing/JLightweightFrame.class */
public final class JLightweightFrame extends LightweightFrame implements RootPaneContainer {
    private LightweightContent content;
    private Component component;
    private JPanel contentPane;
    private BufferedImage bbImage;
    private static boolean copyBufferEnabled;
    private int[] copyBuffer;
    private PropertyChangeListener layoutSizeListener;
    private SwingUtilities2.RepaintListener repaintListener;
    private final JRootPane rootPane = new JRootPane();
    private volatile int scaleFactor = 1;

    static {
        SwingAccessor.setJLightweightFrameAccessor(new SwingAccessor.JLightweightFrameAccessor() { // from class: sun.swing.JLightweightFrame.1
            @Override // sun.swing.SwingAccessor.JLightweightFrameAccessor
            public void updateCursor(JLightweightFrame jLightweightFrame) {
                jLightweightFrame.updateClientCursor();
            }
        });
        copyBufferEnabled = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.jlf.copyBufferEnabled", "true")));
    }

    public JLightweightFrame() {
        copyBufferEnabled = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.jlf.copyBufferEnabled", "true")));
        add(this.rootPane, BorderLayout.CENTER);
        setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        if (getGraphicsConfiguration().isTranslucencyCapable()) {
            setBackground(new Color(0, 0, 0, 0));
        }
        this.layoutSizeListener = new PropertyChangeListener() { // from class: sun.swing.JLightweightFrame.2
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                Dimension dimension = (Dimension) propertyChangeEvent.getNewValue();
                if ("preferredSize".equals(propertyChangeEvent.getPropertyName())) {
                    JLightweightFrame.this.content.preferredSizeChanged(dimension.width, dimension.height);
                } else if ("maximumSize".equals(propertyChangeEvent.getPropertyName())) {
                    JLightweightFrame.this.content.maximumSizeChanged(dimension.width, dimension.height);
                } else if ("minimumSize".equals(propertyChangeEvent.getPropertyName())) {
                    JLightweightFrame.this.content.minimumSizeChanged(dimension.width, dimension.height);
                }
            }
        };
        this.repaintListener = (jComponent, i2, i3, i4, i5) -> {
            Window windowAncestor = SwingUtilities.getWindowAncestor(jComponent);
            if (windowAncestor != this) {
                return;
            }
            Point pointConvertPoint = SwingUtilities.convertPoint(jComponent, i2, i3, windowAncestor);
            Rectangle rectangleIntersection = new Rectangle(pointConvertPoint.f12370x, pointConvertPoint.f12371y, i4, i5).intersection(new Rectangle(0, 0, this.bbImage.getWidth() / this.scaleFactor, this.bbImage.getHeight() / this.scaleFactor));
            if (!rectangleIntersection.isEmpty()) {
                notifyImageUpdated(rectangleIntersection.f12372x, rectangleIntersection.f12373y, rectangleIntersection.width, rectangleIntersection.height);
            }
        };
        SwingAccessor.getRepaintManagerAccessor().addRepaintListener(RepaintManager.currentManager(this), this.repaintListener);
    }

    @Override // java.awt.Window
    public void dispose() {
        SwingAccessor.getRepaintManagerAccessor().removeRepaintListener(RepaintManager.currentManager(this), this.repaintListener);
        super.dispose();
    }

    public void setContent(LightweightContent lightweightContent) {
        if (lightweightContent == null) {
            System.err.println("JLightweightFrame.setContent: content may not be null!");
            return;
        }
        this.content = lightweightContent;
        this.component = lightweightContent.getComponent();
        Dimension preferredSize = this.component.getPreferredSize();
        lightweightContent.preferredSizeChanged(preferredSize.width, preferredSize.height);
        Dimension maximumSize = this.component.getMaximumSize();
        lightweightContent.maximumSizeChanged(maximumSize.width, maximumSize.height);
        Dimension minimumSize = this.component.getMinimumSize();
        lightweightContent.minimumSizeChanged(minimumSize.width, minimumSize.height);
        initInterior();
    }

    @Override // sun.awt.LightweightFrame, java.awt.Component
    public Graphics getGraphics() {
        if (this.bbImage == null) {
            return null;
        }
        Graphics2D graphics2DCreateGraphics = this.bbImage.createGraphics();
        graphics2DCreateGraphics.setBackground(getBackground());
        graphics2DCreateGraphics.setColor(getForeground());
        graphics2DCreateGraphics.setFont(getFont());
        graphics2DCreateGraphics.scale(this.scaleFactor, this.scaleFactor);
        return graphics2DCreateGraphics;
    }

    @Override // sun.awt.LightweightFrame
    public void grabFocus() {
        if (this.content != null) {
            this.content.focusGrabbed();
        }
    }

    @Override // sun.awt.LightweightFrame
    public void ungrabFocus() {
        if (this.content != null) {
            this.content.focusUngrabbed();
        }
    }

    @Override // sun.awt.LightweightFrame
    public int getScaleFactor() {
        return this.scaleFactor;
    }

    @Override // sun.awt.LightweightFrame
    public void notifyDisplayChanged(int i2) {
        if (i2 != this.scaleFactor) {
            if (!copyBufferEnabled) {
                this.content.paintLock();
            }
            try {
                if (this.bbImage != null) {
                    resizeBuffer(getWidth(), getHeight(), i2);
                }
                if (!copyBufferEnabled) {
                    this.content.paintUnlock();
                }
                this.scaleFactor = i2;
            } catch (Throwable th) {
                if (!copyBufferEnabled) {
                    this.content.paintUnlock();
                }
                throw th;
            }
        }
        if (getPeer() instanceof DisplayChangedListener) {
            ((DisplayChangedListener) getPeer()).displayChanged();
        }
        repaint();
    }

    @Override // sun.awt.LightweightFrame, java.awt.Frame, java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        if (getPeer() instanceof DisplayChangedListener) {
            ((DisplayChangedListener) getPeer()).displayChanged();
        }
    }

    private void syncCopyBuffer(boolean z2, int i2, int i3, int i4, int i5, int i6) {
        this.content.paintLock();
        try {
            int[] data = ((DataBufferInt) this.bbImage.getRaster().getDataBuffer()).getData();
            if (z2) {
                this.copyBuffer = new int[data.length];
            }
            int width = this.bbImage.getWidth();
            int i7 = i2 * i6;
            int i8 = i3 * i6;
            int i9 = i4 * i6;
            int i10 = i5 * i6;
            for (int i11 = 0; i11 < i10; i11++) {
                int i12 = ((i8 + i11) * width) + i7;
                System.arraycopy(data, i12, this.copyBuffer, i12, i9);
            }
        } finally {
            this.content.paintUnlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyImageUpdated(int i2, int i3, int i4, int i5) {
        if (copyBufferEnabled) {
            syncCopyBuffer(false, i2, i3, i4, i5, this.scaleFactor);
        }
        this.content.imageUpdated(i2, i3, i4, i5);
    }

    private void initInterior() {
        this.contentPane = new JPanel() { // from class: sun.swing.JLightweightFrame.3
            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                Rectangle rectangle;
                if (!JLightweightFrame.copyBufferEnabled) {
                    JLightweightFrame.this.content.paintLock();
                }
                try {
                    super.paint(graphics);
                    if (graphics.getClipBounds() == null) {
                        rectangle = new Rectangle(0, 0, JLightweightFrame.this.contentPane.getWidth(), JLightweightFrame.this.contentPane.getHeight());
                    } else {
                        rectangle = graphics.getClipBounds();
                    }
                    final Rectangle rectangle2 = rectangle;
                    rectangle2.f12372x = Math.max(0, rectangle2.f12372x);
                    rectangle2.f12373y = Math.max(0, rectangle2.f12373y);
                    rectangle2.width = Math.min(JLightweightFrame.this.contentPane.getWidth(), rectangle2.width);
                    rectangle2.height = Math.min(JLightweightFrame.this.contentPane.getHeight(), rectangle2.height);
                    EventQueue.invokeLater(new Runnable() { // from class: sun.swing.JLightweightFrame.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Rectangle rectangleIntersection = JLightweightFrame.this.contentPane.getBounds().intersection(rectangle2);
                            JLightweightFrame.this.notifyImageUpdated(rectangleIntersection.f12372x, rectangleIntersection.f12373y, rectangleIntersection.width, rectangleIntersection.height);
                        }
                    });
                    if (!JLightweightFrame.copyBufferEnabled) {
                        JLightweightFrame.this.content.paintUnlock();
                    }
                } catch (Throwable th) {
                    if (!JLightweightFrame.copyBufferEnabled) {
                        JLightweightFrame.this.content.paintUnlock();
                    }
                    throw th;
                }
            }

            @Override // javax.swing.JComponent
            protected boolean isPaintingOrigin() {
                return true;
            }
        };
        this.contentPane.setLayout(new BorderLayout());
        this.contentPane.add(this.component);
        if ("true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.jlf.contentPaneTransparent", "false")))) {
            this.contentPane.setOpaque(false);
        }
        setContentPane(this.contentPane);
        this.contentPane.addContainerListener(new ContainerListener() { // from class: sun.swing.JLightweightFrame.4
            @Override // java.awt.event.ContainerListener
            public void componentAdded(ContainerEvent containerEvent) {
                Component component = JLightweightFrame.this.component;
                if (containerEvent.getChild() == component) {
                    component.addPropertyChangeListener("preferredSize", JLightweightFrame.this.layoutSizeListener);
                    component.addPropertyChangeListener("maximumSize", JLightweightFrame.this.layoutSizeListener);
                    component.addPropertyChangeListener("minimumSize", JLightweightFrame.this.layoutSizeListener);
                }
            }

            @Override // java.awt.event.ContainerListener
            public void componentRemoved(ContainerEvent containerEvent) {
                Component component = JLightweightFrame.this.component;
                if (containerEvent.getChild() == component) {
                    component.removePropertyChangeListener(JLightweightFrame.this.layoutSizeListener);
                }
            }
        });
    }

    @Override // java.awt.Window, java.awt.Component
    public void reshape(int i2, int i3, int i4, int i5) {
        super.reshape(i2, i3, i4, i5);
        if (i4 == 0 || i5 == 0) {
            return;
        }
        if (!copyBufferEnabled) {
            this.content.paintLock();
        }
        try {
            boolean z2 = this.bbImage == null;
            int iMax = i4;
            int iMax2 = i5;
            if (this.bbImage != null) {
                int width = this.bbImage.getWidth() / this.scaleFactor;
                int height = this.bbImage.getHeight() / this.scaleFactor;
                if (i4 != width || i5 != height) {
                    z2 = true;
                    if (this.bbImage != null) {
                        if (width >= iMax && height >= iMax2) {
                            z2 = false;
                        } else {
                            if (width >= iMax) {
                                iMax = width;
                            } else {
                                iMax = Math.max((int) (width * 1.2d), i4);
                            }
                            iMax2 = height >= iMax2 ? height : Math.max((int) (height * 1.2d), i5);
                        }
                    }
                }
            }
            if (z2) {
                resizeBuffer(iMax, iMax2, this.scaleFactor);
                if (copyBufferEnabled) {
                    return;
                }
                this.content.paintUnlock();
                return;
            }
            this.content.imageReshaped(0, 0, i4, i5);
            if (!copyBufferEnabled) {
                this.content.paintUnlock();
            }
        } catch (Throwable th) {
            if (!copyBufferEnabled) {
                this.content.paintUnlock();
            }
            throw th;
        }
    }

    private void resizeBuffer(int i2, int i3, int i4) {
        this.bbImage = new BufferedImage(i2 * i4, i3 * i4, 3);
        int[] data = ((DataBufferInt) this.bbImage.getRaster().getDataBuffer()).getData();
        if (copyBufferEnabled) {
            syncCopyBuffer(true, 0, 0, i2, i3, i4);
            data = this.copyBuffer;
        }
        this.content.imageBufferReset(data, 0, 0, i2, i3, i2 * i4, i4);
    }

    @Override // javax.swing.RootPaneContainer
    public JRootPane getRootPane() {
        return this.rootPane;
    }

    @Override // javax.swing.RootPaneContainer
    public void setContentPane(Container container) {
        getRootPane().setContentPane(container);
    }

    @Override // javax.swing.RootPaneContainer
    public Container getContentPane() {
        return getRootPane().getContentPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setLayeredPane(JLayeredPane jLayeredPane) {
        getRootPane().setLayeredPane(jLayeredPane);
    }

    @Override // javax.swing.RootPaneContainer
    public JLayeredPane getLayeredPane() {
        return getRootPane().getLayeredPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setGlassPane(Component component) {
        getRootPane().setGlassPane(component);
    }

    @Override // javax.swing.RootPaneContainer
    public Component getGlassPane() {
        return getRootPane().getGlassPane();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateClientCursor() {
        Point location = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(location, this);
        Component deepestComponentAt = SwingUtilities.getDeepestComponentAt(this, location.f12370x, location.f12371y);
        if (deepestComponentAt != null) {
            this.content.setCursor(deepestComponentAt.getCursor());
        }
    }

    public void overrideNativeWindowHandle(long j2, Runnable runnable) {
        ComponentPeer peer = AWTAccessor.getComponentAccessor().getPeer(this);
        if (peer instanceof OverrideNativeWindowHandle) {
            ((OverrideNativeWindowHandle) peer).overrideWindowHandle(j2);
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    @Override // sun.awt.LightweightFrame
    public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        if (this.content == null) {
            return null;
        }
        return (T) this.content.createDragGestureRecognizer(cls, dragSource, component, i2, dragGestureListener);
    }

    @Override // sun.awt.LightweightFrame
    public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException {
        if (this.content == null) {
            return null;
        }
        return this.content.createDragSourceContextPeer(dragGestureEvent);
    }

    @Override // sun.awt.LightweightFrame
    public void addDropTarget(DropTarget dropTarget) {
        if (this.content == null) {
            return;
        }
        this.content.addDropTarget(dropTarget);
    }

    @Override // sun.awt.LightweightFrame
    public void removeDropTarget(DropTarget dropTarget) {
        if (this.content == null) {
            return;
        }
        this.content.removeDropTarget(dropTarget);
    }
}
