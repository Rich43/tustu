package org.icepdf.ri.util.jxlayer.plaf;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.plaf.ComponentUI;
import org.icepdf.ri.util.jxlayer.JXLayer;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/jxlayer/plaf/LayerUI.class */
public abstract class LayerUI<V extends Component> extends ComponentUI implements Serializable {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics g2, JComponent c2) {
        c2.paint(g2);
    }

    public void eventDispatched(AWTEvent e2, JXLayer<? extends V> l2) {
    }

    public void updateUI(JXLayer<? extends V> l2) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent c2) {
        addPropertyChangeListener((JXLayer) c2);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent c2) {
        removePropertyChangeListener((JXLayer) c2);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return this.propertyChangeSupport.getPropertyChangeListeners(propertyName);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        this.propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void handlePropertyChangeEvent(PropertyChangeEvent evt, JXLayer<? extends V> l2) {
    }

    public Dimension getPreferredScrollableViewportSize(JXLayer<? extends V> l2) {
        if (l2.getView() instanceof Scrollable) {
            return ((Scrollable) l2.getView()).getPreferredScrollableViewportSize();
        }
        return l2.getPreferredSize();
    }

    public int getScrollableBlockIncrement(JXLayer<? extends V> l2, Rectangle visibleRect, int orientation, int direction) {
        if (l2.getView() instanceof Scrollable) {
            return ((Scrollable) l2.getView()).getScrollableBlockIncrement(visibleRect, orientation, direction);
        }
        return orientation == 1 ? visibleRect.height : visibleRect.width;
    }

    public boolean getScrollableTracksViewportHeight(JXLayer<? extends V> l2) {
        if (l2.getView() instanceof Scrollable) {
            return ((Scrollable) l2.getView()).getScrollableTracksViewportHeight();
        }
        return (l2.getParent() instanceof JViewport) && ((JViewport) l2.getParent()).getHeight() > l2.getPreferredSize().height;
    }

    public boolean getScrollableTracksViewportWidth(JXLayer<? extends V> l2) {
        if (l2.getView() instanceof Scrollable) {
            return ((Scrollable) l2.getView()).getScrollableTracksViewportWidth();
        }
        return (l2.getParent() instanceof JViewport) && ((JViewport) l2.getParent()).getWidth() > l2.getPreferredSize().width;
    }

    public int getScrollableUnitIncrement(JXLayer<? extends V> l2, Rectangle visibleRect, int orientation, int direction) {
        if (l2.getView() instanceof Scrollable) {
            return ((Scrollable) l2.getView()).getScrollableUnitIncrement(visibleRect, orientation, direction);
        }
        return 1;
    }
}
