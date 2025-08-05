package org.icepdf.ri.util.jxlayer;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import org.icepdf.ri.util.jxlayer.plaf.LayerUI;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/jxlayer/JXLayer.class */
public final class JXLayer<V extends Component> extends JComponent implements Scrollable, PropertyChangeListener, Accessible {
    private V view;
    private LayerUI<? super V> layerUI;
    private JPanel glassPane;
    private boolean isPainting;
    private long eventMask;
    private static final long ACCEPTED_EVENTS = 231487;
    private static final DefaultLayerLayout sharedLayoutInstance = new DefaultLayerLayout();
    private static final LayerEventController eventController = new LayerEventController();

    public JXLayer() {
        this(null);
    }

    public JXLayer(V view) {
        this(view, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public JXLayer(V view, LayerUI<V> layerUI) {
        super.setLayout(sharedLayoutInstance);
        setGlassPane(createGlassPane());
        setView(view);
        setUI((LayerUI) layerUI);
    }

    public V getView() {
        return this.view;
    }

    public void setView(V view) {
        Component oldView = getView();
        if (oldView != null) {
            super.remove(oldView);
        }
        if (view != null) {
            super.addImpl(view, null, getComponentCount());
        }
        this.view = view;
        firePropertyChange("view", oldView, view);
        revalidate();
        repaint();
    }

    public void setUI(LayerUI<? super V> ui) {
        this.layerUI = ui;
        super.setUI((ComponentUI) ui);
    }

    public LayerUI<? super V> getUI() {
        return this.layerUI;
    }

    public JPanel getGlassPane() {
        return this.glassPane;
    }

    public void setGlassPane(JPanel glassPane) {
        Container oldGlassPane = getGlassPane();
        if (oldGlassPane != null) {
            super.remove(oldGlassPane);
        }
        if (glassPane != null) {
            super.addImpl(glassPane, null, 0);
        }
        this.glassPane = glassPane;
        firePropertyChange(JInternalFrame.GLASS_PANE_PROPERTY, oldGlassPane, glassPane);
        revalidate();
        repaint();
    }

    public JPanel createGlassPane() {
        return new DefaultLayerGlassPane();
    }

    @Override // java.awt.Container
    protected void addImpl(Component comp, Object constraints, int index) {
        throw new UnsupportedOperationException("Adding components to JXLayer is not supported, use setView() or setGlassPane() instead");
    }

    @Override // java.awt.Container
    public void remove(Component comp) {
        if (comp == getView()) {
            setView(null);
        } else if (comp == getGlassPane()) {
            setGlassPane(null);
        } else {
            super.remove(comp);
        }
    }

    @Override // java.awt.Container
    public void removeAll() {
        setView(null);
        setGlassPane(null);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics g2) {
        if (!this.isPainting && getUI() != null) {
            this.isPainting = true;
            super.paintComponent(g2);
            this.isPainting = false;
            return;
        }
        super.paint(g2);
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics g2) {
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return !this.glassPane.isVisible();
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent evt) {
        if (getUI() != null) {
            getUI().handlePropertyChangeEvent(evt, this);
        }
    }

    public void setLayerEventMask(long layerEventMask) {
        if (layerEventMask != (layerEventMask & ACCEPTED_EVENTS)) {
            throw new IllegalArgumentException("The event bitmask contains unsupported event types");
        }
        long oldEventMask = getLayerEventMask();
        this.eventMask = layerEventMask;
        firePropertyChange("layerEventMask", oldEventMask, layerEventMask);
        if (layerEventMask != oldEventMask) {
            disableEvents(oldEventMask);
            enableEvents(this.eventMask);
            eventController.updateAWTEventListener(this);
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
        if (getUI() != null) {
            return getUI().getPreferredScrollableViewportSize(this);
        }
        return getPreferredSize();
    }

    @Override // javax.swing.Scrollable
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (getUI() != null) {
            return getUI().getScrollableBlockIncrement(this, visibleRect, orientation, direction);
        }
        return orientation == 1 ? visibleRect.height : visibleRect.width;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportHeight() {
        if (getUI() != null) {
            return getUI().getScrollableTracksViewportHeight(this);
        }
        return (getParent() instanceof JViewport) && getParent().getHeight() > getPreferredSize().height;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        if (getUI() != null) {
            return getUI().getScrollableTracksViewportWidth(this);
        }
        return (getParent() instanceof JViewport) && getParent().getWidth() > getPreferredSize().width;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (getUI() != null) {
            return getUI().getScrollableUnitIncrement(this, visibleRect, orientation, direction);
        }
        return 1;
    }

    private void readObject(ObjectInputStream s2) throws IOException, ClassNotFoundException {
        s2.defaultReadObject();
        if (getUI() != null) {
            setUI((LayerUI) getUI());
        }
        if (getLayerEventMask() == 0) {
            return;
        }
        eventController.updateAWTEventListener(this);
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new JComponent.AccessibleJComponent() { // from class: org.icepdf.ri.util.jxlayer.JXLayer.1
                @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
                public AccessibleRole getAccessibleRole() {
                    return AccessibleRole.PANEL;
                }
            };
        }
        return this.accessibleContext;
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/jxlayer/JXLayer$LayerEventController.class */
    private static class LayerEventController implements AWTEventListener {
        private ArrayList<WeakReference<JXLayer>> layerList;
        private long currentEventMask;

        private LayerEventController() {
            this.layerList = new ArrayList<>();
        }

        @Override // java.awt.event.AWTEventListener
        public void eventDispatched(AWTEvent event) {
            JXLayer<? extends V> l2;
            LayerUI ui;
            Object source = event.getSource();
            if (source instanceof Component) {
                Component parent = (Component) source;
                while (true) {
                    Component component = parent;
                    if (component != null) {
                        if ((component instanceof JXLayer) && (ui = (l2 = (JXLayer) component).getUI()) != null && isEventEnabled(l2.getLayerEventMask(), event.getID())) {
                            ui.eventDispatched(event, l2);
                        }
                        parent = component.getParent();
                    } else {
                        return;
                    }
                }
            }
        }

        private boolean layerListContains(JXLayer l2) {
            Iterator i$ = this.layerList.iterator();
            while (i$.hasNext()) {
                WeakReference<JXLayer> layerWeakReference = i$.next();
                if (layerWeakReference.get() == l2) {
                    return true;
                }
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAWTEventListener(JXLayer layer) {
            if (!layerListContains(layer) && layer.getLayerEventMask() != 0) {
                this.layerList.add(new WeakReference<>(layer));
            }
            long combinedMask = 0;
            Iterator<WeakReference<JXLayer>> it = this.layerList.iterator();
            while (it.hasNext()) {
                WeakReference<JXLayer> weakRef = it.next();
                JXLayer currLayer = weakRef.get();
                if (currLayer == null) {
                    it.remove();
                } else {
                    combinedMask |= currLayer.getLayerEventMask();
                }
            }
            if (combinedMask == 0) {
                removeAWTEventListener();
                this.layerList.clear();
            } else if (getCurrentEventMask() != combinedMask) {
                removeAWTEventListener();
                addAWTEventListener(combinedMask);
            }
        }

        private long getCurrentEventMask() {
            return this.currentEventMask;
        }

        private void addAWTEventListener(final long eventMask) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: org.icepdf.ri.util.jxlayer.JXLayer.LayerEventController.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Toolkit.getDefaultToolkit().addAWTEventListener(LayerEventController.this, eventMask);
                    return null;
                }
            });
            this.currentEventMask = eventMask;
        }

        private void removeAWTEventListener() {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: org.icepdf.ri.util.jxlayer.JXLayer.LayerEventController.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Toolkit.getDefaultToolkit().removeAWTEventListener(LayerEventController.this);
                    return null;
                }
            });
            this.currentEventMask = 0L;
        }

        private boolean isEventEnabled(long eventMask, int id) {
            return ((eventMask & 1) != 0 && id >= 100 && id <= 103) || ((eventMask & 2) != 0 && id >= 300 && id <= 301) || (((eventMask & 4) != 0 && id >= 1004 && id <= 1005) || (((eventMask & 8) != 0 && id >= 400 && id <= 402) || (((eventMask & 131072) != 0 && id == 507) || (((eventMask & 32) != 0 && (id == 503 || id == 506)) || (!((eventMask & 16) == 0 || id == 503 || id == 506 || id == 507 || id < 500 || id > 507) || (((eventMask & 2048) != 0 && id >= 1100 && id <= 1101) || (((eventMask & 32768) != 0 && id == 1400) || ((eventMask & 65536) != 0 && (id == 1401 || id == 1402)))))))));
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/jxlayer/JXLayer$DefaultLayerLayout.class */
    private static class DefaultLayerLayout implements LayoutManager, Serializable {
        private DefaultLayerLayout() {
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container parent) {
            JXLayer layer = (JXLayer) parent;
            Component view = layer.getView();
            Component glassPane = layer.getGlassPane();
            if (view != null) {
                Insets insets = layer.getInsets();
                view.setLocation(insets.left, insets.top);
                view.setSize((layer.getWidth() - insets.left) - insets.right, (layer.getHeight() - insets.top) - insets.bottom);
            }
            if (glassPane != null) {
                glassPane.setLocation(0, 0);
                glassPane.setSize(layer.getWidth(), layer.getHeight());
            }
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container parent) {
            JXLayer layer = (JXLayer) parent;
            Insets insets = layer.getInsets();
            Dimension ret = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
            Component view = layer.getView();
            if (view != null) {
                Dimension size = view.getMinimumSize();
                ret.width += size.width;
                ret.height += size.height;
            }
            if (ret.width == 0 || ret.height == 0) {
                ret.height = 4;
                ret.width = 4;
            }
            return ret;
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container parent) {
            JXLayer layer = (JXLayer) parent;
            Insets insets = layer.getInsets();
            Dimension ret = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
            Component view = layer.getView();
            if (view != null) {
                Dimension size = view.getPreferredSize();
                if (size.width > 0 && size.height > 0) {
                    ret.width += size.width;
                    ret.height += size.height;
                }
            }
            return ret;
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component comp) {
        }
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/jxlayer/JXLayer$DefaultLayerGlassPane.class */
    private static class DefaultLayerGlassPane extends JPanel {
        public DefaultLayerGlassPane() {
            setOpaque(false);
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public boolean contains(int x2, int y2) {
            for (int i2 = 0; i2 < getComponentCount(); i2++) {
                Component c2 = getComponent(i2);
                Point point = SwingUtilities.convertPoint(this, new Point(x2, y2), c2);
                if (c2.isVisible() && c2.contains(point)) {
                    return true;
                }
            }
            if (getMouseListeners().length == 0 && getMouseMotionListeners().length == 0 && getMouseWheelListeners().length == 0 && !isCursorSet()) {
                return false;
            }
            return super.contains(x2, y2);
        }
    }
}
