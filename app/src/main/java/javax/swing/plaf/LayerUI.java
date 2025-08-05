package javax.swing.plaf;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;

/* loaded from: rt.jar:javax/swing/plaf/LayerUI.class */
public class LayerUI<V extends Component> extends ComponentUI implements Serializable {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        jComponent.paint(graphics);
    }

    public void eventDispatched(AWTEvent aWTEvent, JLayer<? extends V> jLayer) {
        if (aWTEvent instanceof FocusEvent) {
            processFocusEvent((FocusEvent) aWTEvent, jLayer);
            return;
        }
        if (aWTEvent instanceof MouseEvent) {
            switch (aWTEvent.getID()) {
                case 500:
                case 501:
                case 502:
                case 504:
                case 505:
                    processMouseEvent((MouseEvent) aWTEvent, jLayer);
                    break;
                case 503:
                case 506:
                    processMouseMotionEvent((MouseEvent) aWTEvent, jLayer);
                    break;
                case 507:
                    processMouseWheelEvent((MouseWheelEvent) aWTEvent, jLayer);
                    break;
            }
        }
        if (aWTEvent instanceof KeyEvent) {
            processKeyEvent((KeyEvent) aWTEvent, jLayer);
            return;
        }
        if (aWTEvent instanceof ComponentEvent) {
            processComponentEvent((ComponentEvent) aWTEvent, jLayer);
            return;
        }
        if (aWTEvent instanceof InputMethodEvent) {
            processInputMethodEvent((InputMethodEvent) aWTEvent, jLayer);
            return;
        }
        if (aWTEvent instanceof HierarchyEvent) {
            switch (aWTEvent.getID()) {
                case 1400:
                    processHierarchyEvent((HierarchyEvent) aWTEvent, jLayer);
                    break;
                case HierarchyEvent.ANCESTOR_MOVED /* 1401 */:
                case 1402:
                    processHierarchyBoundsEvent((HierarchyEvent) aWTEvent, jLayer);
                    break;
            }
        }
    }

    protected void processComponentEvent(ComponentEvent componentEvent, JLayer<? extends V> jLayer) {
    }

    protected void processFocusEvent(FocusEvent focusEvent, JLayer<? extends V> jLayer) {
    }

    protected void processKeyEvent(KeyEvent keyEvent, JLayer<? extends V> jLayer) {
    }

    protected void processMouseEvent(MouseEvent mouseEvent, JLayer<? extends V> jLayer) {
    }

    protected void processMouseMotionEvent(MouseEvent mouseEvent, JLayer<? extends V> jLayer) {
    }

    protected void processMouseWheelEvent(MouseWheelEvent mouseWheelEvent, JLayer<? extends V> jLayer) {
    }

    protected void processInputMethodEvent(InputMethodEvent inputMethodEvent, JLayer<? extends V> jLayer) {
    }

    protected void processHierarchyEvent(HierarchyEvent hierarchyEvent, JLayer<? extends V> jLayer) {
    }

    protected void processHierarchyBoundsEvent(HierarchyEvent hierarchyEvent, JLayer<? extends V> jLayer) {
    }

    public void updateUI(JLayer<? extends V> jLayer) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        addPropertyChangeListener((JLayer) jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        removePropertyChangeListener((JLayer) jComponent);
    }

    public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.propertyChangeSupport.getPropertyChangeListeners();
    }

    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.addPropertyChangeListener(str, propertyChangeListener);
    }

    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.removePropertyChangeListener(str, propertyChangeListener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String str) {
        return this.propertyChangeSupport.getPropertyChangeListeners(str);
    }

    protected void firePropertyChange(String str, Object obj, Object obj2) {
        this.propertyChangeSupport.firePropertyChange(str, obj, obj2);
    }

    public void applyPropertyChange(PropertyChangeEvent propertyChangeEvent, JLayer<? extends V> jLayer) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        JLayer jLayer = (JLayer) jComponent;
        if (jLayer.getView() != null) {
            return jLayer.getView().getBaseline(i2, i3);
        }
        return super.getBaseline(jComponent, i2, i3);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        JLayer jLayer = (JLayer) jComponent;
        if (jLayer.getView() != null) {
            return jLayer.getView().getBaselineResizeBehavior();
        }
        return super.getBaselineResizeBehavior(jComponent);
    }

    public void doLayout(JLayer<? extends V> jLayer) {
        Component view = jLayer.getView();
        if (view != null) {
            view.setBounds(0, 0, jLayer.getWidth(), jLayer.getHeight());
        }
        JPanel glassPane = jLayer.getGlassPane();
        if (glassPane != null) {
            glassPane.setBounds(0, 0, jLayer.getWidth(), jLayer.getHeight());
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Component view = ((JLayer) jComponent).getView();
        if (view != null) {
            return view.getPreferredSize();
        }
        return super.getPreferredSize(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Component view = ((JLayer) jComponent).getView();
        if (view != null) {
            return view.getMinimumSize();
        }
        return super.getMinimumSize(jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Component view = ((JLayer) jComponent).getView();
        if (view != null) {
            return view.getMaximumSize();
        }
        return super.getMaximumSize(jComponent);
    }

    public void paintImmediately(int i2, int i3, int i4, int i5, JLayer<? extends V> jLayer) {
        jLayer.paintImmediately(i2, i3, i4, i5);
    }
}
