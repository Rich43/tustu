package javax.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.LayerUI;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:javax/swing/JLayer.class */
public final class JLayer<V extends Component> extends JComponent implements Scrollable, PropertyChangeListener, Accessible {
    private V view;
    private LayerUI<? super V> layerUI;
    private JPanel glassPane;
    private long eventMask;
    private transient boolean isPainting;
    private transient boolean isPaintingImmediately;
    private static final LayerEventController eventController = new LayerEventController();

    public JLayer() {
        this(null);
    }

    public JLayer(V v2) {
        this(v2, new LayerUI());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JLayer(V v2, LayerUI<V> layerUI) {
        setGlassPane(createGlassPane());
        setView(v2);
        setUI((LayerUI) layerUI);
    }

    public V getView() {
        return this.view;
    }

    public void setView(V v2) {
        Component view = getView();
        if (view != null) {
            super.remove(view);
        }
        if (v2 != null) {
            super.addImpl(v2, null, getComponentCount());
        }
        this.view = v2;
        firePropertyChange("view", view, v2);
        revalidate();
        repaint();
    }

    public void setUI(LayerUI<? super V> layerUI) {
        this.layerUI = layerUI;
        super.setUI((ComponentUI) layerUI);
    }

    public LayerUI<? super V> getUI() {
        return this.layerUI;
    }

    public JPanel getGlassPane() {
        return this.glassPane;
    }

    public void setGlassPane(JPanel jPanel) {
        JPanel glassPane = getGlassPane();
        boolean zIsVisible = false;
        if (glassPane != null) {
            zIsVisible = glassPane.isVisible();
            super.remove(glassPane);
        }
        if (jPanel != null) {
            AWTAccessor.getComponentAccessor().setMixingCutoutShape(jPanel, new Rectangle());
            jPanel.setVisible(zIsVisible);
            super.addImpl(jPanel, null, 0);
        }
        this.glassPane = jPanel;
        firePropertyChange(JInternalFrame.GLASS_PANE_PROPERTY, glassPane, jPanel);
        revalidate();
        repaint();
    }

    public JPanel createGlassPane() {
        return new DefaultLayerGlassPane();
    }

    @Override // java.awt.Container
    public void setLayout(LayoutManager layoutManager) {
        if (layoutManager != null) {
            throw new IllegalArgumentException("JLayer.setLayout() not supported");
        }
    }

    @Override // javax.swing.JComponent
    public void setBorder(Border border) {
        if (border != null) {
            throw new IllegalArgumentException("JLayer.setBorder() not supported");
        }
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        throw new UnsupportedOperationException("Adding components to JLayer is not supported, use setView() or setGlassPane() instead");
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        if (component == null) {
            super.remove(component);
            return;
        }
        if (component == getView()) {
            setView(null);
        } else if (component == getGlassPane()) {
            setGlassPane(null);
        } else {
            super.remove(component);
        }
    }

    @Override // java.awt.Container
    public void removeAll() {
        if (this.view != null) {
            setView(null);
        }
        if (this.glassPane != null) {
            setGlassPane(null);
        }
    }

    @Override // javax.swing.JComponent
    protected boolean isPaintingOrigin() {
        return true;
    }

    @Override // javax.swing.JComponent
    public void paintImmediately(int i2, int i3, int i4, int i5) {
        if (!this.isPaintingImmediately && getUI() != null) {
            this.isPaintingImmediately = true;
            try {
                getUI().paintImmediately(i2, i3, i4, i5, this);
                this.isPaintingImmediately = false;
                return;
            } catch (Throwable th) {
                this.isPaintingImmediately = false;
                throw th;
            }
        }
        super.paintImmediately(i2, i3, i4, i5);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (!this.isPainting) {
            this.isPainting = true;
            try {
                super.paintComponent(graphics);
                return;
            } finally {
                this.isPainting = false;
            }
        }
        super.paint(graphics);
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics graphics) {
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (getUI() != null) {
            getUI().applyPropertyChange(propertyChangeEvent, this);
        }
    }

    public void setLayerEventMask(long j2) {
        long layerEventMask = getLayerEventMask();
        this.eventMask = j2;
        firePropertyChange("layerEventMask", layerEventMask, j2);
        if (j2 != layerEventMask) {
            disableEvents(layerEventMask);
            enableEvents(this.eventMask);
            if (!isDisplayable()) {
                return;
            }
            eventController.updateAWTEventListener(layerEventMask, j2);
        }
    }

    public long getLayerEventMask() {
        return this.eventMask;
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        if (getUI() != null) {
            getUI().updateUI(this);
        }
    }

    @Override // javax.swing.Scrollable
    public Dimension getPreferredScrollableViewportSize() {
        if (getView() instanceof Scrollable) {
            return ((Scrollable) getView()).getPreferredScrollableViewportSize();
        }
        return getPreferredSize();
    }

    @Override // javax.swing.Scrollable
    public int getScrollableBlockIncrement(Rectangle rectangle, int i2, int i3) {
        if (getView() instanceof Scrollable) {
            return ((Scrollable) getView()).getScrollableBlockIncrement(rectangle, i2, i3);
        }
        return i2 == 1 ? rectangle.height : rectangle.width;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportHeight() {
        if (getView() instanceof Scrollable) {
            return ((Scrollable) getView()).getScrollableTracksViewportHeight();
        }
        return false;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        if (getView() instanceof Scrollable) {
            return ((Scrollable) getView()).getScrollableTracksViewportWidth();
        }
        return false;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3) {
        if (getView() instanceof Scrollable) {
            return ((Scrollable) getView()).getScrollableUnitIncrement(rectangle, i2, i3);
        }
        return 1;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.layerUI != null) {
            setUI((LayerUI) this.layerUI);
        }
        if (this.eventMask == 0) {
            return;
        }
        eventController.updateAWTEventListener(0L, this.eventMask);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        eventController.updateAWTEventListener(0L, this.eventMask);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        super.removeNotify();
        eventController.updateAWTEventListener(this.eventMask, 0L);
    }

    @Override // java.awt.Container, java.awt.Component
    public void doLayout() {
        if (getUI() != null) {
            getUI().doLayout(this);
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new JComponent.AccessibleJComponent() { // from class: javax.swing.JLayer.1
                @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
                public AccessibleRole getAccessibleRole() {
                    return AccessibleRole.PANEL;
                }
            };
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JLayer$LayerEventController.class */
    private static class LayerEventController implements AWTEventListener {
        private ArrayList<Long> layerMaskList;
        private long currentEventMask;
        private static final long ACCEPTED_EVENTS = 231487;

        private LayerEventController() {
            this.layerMaskList = new ArrayList<>();
        }

        @Override // java.awt.event.AWTEventListener
        public void eventDispatched(AWTEvent aWTEvent) {
            JLayer<? extends V> jLayer;
            LayerUI<? super V> ui;
            Object source = aWTEvent.getSource();
            if (source instanceof Component) {
                Component parent = (Component) source;
                while (true) {
                    Component component = parent;
                    if (component != null) {
                        if ((component instanceof JLayer) && (ui = (jLayer = (JLayer) component).getUI()) != null && isEventEnabled(jLayer.getLayerEventMask(), aWTEvent.getID()) && (!(aWTEvent instanceof InputEvent) || !((InputEvent) aWTEvent).isConsumed())) {
                            ui.eventDispatched(aWTEvent, jLayer);
                        }
                        parent = component.getParent();
                    } else {
                        return;
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAWTEventListener(long j2, long j3) {
            if (j2 != 0) {
                this.layerMaskList.remove(Long.valueOf(j2));
            }
            if (j3 != 0) {
                this.layerMaskList.add(Long.valueOf(j3));
            }
            long jLongValue = 0;
            Iterator<Long> it = this.layerMaskList.iterator();
            while (it.hasNext()) {
                jLongValue |= it.next().longValue();
            }
            long j4 = jLongValue & ACCEPTED_EVENTS;
            if (j4 == 0) {
                removeAWTEventListener();
            } else if (getCurrentEventMask() != j4) {
                removeAWTEventListener();
                addAWTEventListener(j4);
            }
            this.currentEventMask = j4;
        }

        private long getCurrentEventMask() {
            return this.currentEventMask;
        }

        private void addAWTEventListener(final long j2) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javax.swing.JLayer.LayerEventController.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Toolkit.getDefaultToolkit().addAWTEventListener(LayerEventController.this, j2);
                    return null;
                }
            });
        }

        private void removeAWTEventListener() {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javax.swing.JLayer.LayerEventController.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Toolkit.getDefaultToolkit().removeAWTEventListener(LayerEventController.this);
                    return null;
                }
            });
        }

        private boolean isEventEnabled(long j2, int i2) {
            return ((j2 & 1) != 0 && i2 >= 100 && i2 <= 103) || ((j2 & 2) != 0 && i2 >= 300 && i2 <= 301) || (((j2 & 4) != 0 && i2 >= 1004 && i2 <= 1005) || (((j2 & 8) != 0 && i2 >= 400 && i2 <= 402) || (((j2 & 131072) != 0 && i2 == 507) || (((j2 & 32) != 0 && (i2 == 503 || i2 == 506)) || (!((j2 & 16) == 0 || i2 == 503 || i2 == 506 || i2 == 507 || i2 < 500 || i2 > 507) || (((j2 & 2048) != 0 && i2 >= 1100 && i2 <= 1101) || (((j2 & 32768) != 0 && i2 == 1400) || ((j2 & 65536) != 0 && (i2 == 1401 || i2 == 1402)))))))));
        }
    }

    /* loaded from: rt.jar:javax/swing/JLayer$DefaultLayerGlassPane.class */
    private static class DefaultLayerGlassPane extends JPanel {
        public DefaultLayerGlassPane() {
            setOpaque(false);
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public boolean contains(int i2, int i3) {
            for (int i4 = 0; i4 < getComponentCount(); i4++) {
                Component component = getComponent(i4);
                Point pointConvertPoint = SwingUtilities.convertPoint(this, new Point(i2, i3), component);
                if (component.isVisible() && component.contains(pointConvertPoint)) {
                    return true;
                }
            }
            if (getMouseListeners().length == 0 && getMouseMotionListeners().length == 0 && getMouseWheelListeners().length == 0 && !isCursorSet()) {
                return false;
            }
            return super.contains(i2, i3);
        }
    }
}
