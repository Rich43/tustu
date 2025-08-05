package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.EventListenerList;

/* loaded from: rt.jar:javax/swing/AncestorNotifier.class */
class AncestorNotifier implements ComponentListener, PropertyChangeListener, Serializable {
    transient Component firstInvisibleAncestor;
    EventListenerList listenerList = new EventListenerList();
    JComponent root;

    AncestorNotifier(JComponent jComponent) {
        this.root = jComponent;
        addListeners(jComponent, true);
    }

    void addAncestorListener(AncestorListener ancestorListener) {
        this.listenerList.add(AncestorListener.class, ancestorListener);
    }

    void removeAncestorListener(AncestorListener ancestorListener) {
        this.listenerList.remove(AncestorListener.class, ancestorListener);
    }

    AncestorListener[] getAncestorListeners() {
        return (AncestorListener[]) this.listenerList.getListeners(AncestorListener.class);
    }

    protected void fireAncestorAdded(JComponent jComponent, int i2, Container container, Container container2) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == AncestorListener.class) {
                ((AncestorListener) listenerList[length + 1]).ancestorAdded(new AncestorEvent(jComponent, i2, container, container2));
            }
        }
    }

    protected void fireAncestorRemoved(JComponent jComponent, int i2, Container container, Container container2) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == AncestorListener.class) {
                ((AncestorListener) listenerList[length + 1]).ancestorRemoved(new AncestorEvent(jComponent, i2, container, container2));
            }
        }
    }

    protected void fireAncestorMoved(JComponent jComponent, int i2, Container container, Container container2) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == AncestorListener.class) {
                ((AncestorListener) listenerList[length + 1]).ancestorMoved(new AncestorEvent(jComponent, i2, container, container2));
            }
        }
    }

    void removeAllListeners() {
        removeListeners(this.root);
    }

    void addListeners(Component component, boolean z2) {
        this.firstInvisibleAncestor = null;
        Component parent = component;
        while (true) {
            Component component2 = parent;
            if (this.firstInvisibleAncestor != null) {
                break;
            }
            if (z2 || component2 != component) {
                component2.addComponentListener(this);
                if (component2 instanceof JComponent) {
                    ((JComponent) component2).addPropertyChangeListener(this);
                }
            }
            if (!component2.isVisible() || component2.getParent() == null || (component2 instanceof Window)) {
                this.firstInvisibleAncestor = component2;
            }
            parent = component2.getParent();
        }
        if ((this.firstInvisibleAncestor instanceof Window) && this.firstInvisibleAncestor.isVisible()) {
            this.firstInvisibleAncestor = null;
        }
    }

    void removeListeners(Component component) {
        Component parent = component;
        while (true) {
            Component component2 = parent;
            if (component2 != null) {
                component2.removeComponentListener(this);
                if (component2 instanceof JComponent) {
                    ((JComponent) component2).removePropertyChangeListener(this);
                }
                if (component2 != this.firstInvisibleAncestor && !(component2 instanceof Window)) {
                    parent = component2.getParent();
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override // java.awt.event.ComponentListener
    public void componentResized(ComponentEvent componentEvent) {
    }

    @Override // java.awt.event.ComponentListener
    public void componentMoved(ComponentEvent componentEvent) {
        Component component = componentEvent.getComponent();
        fireAncestorMoved(this.root, 3, (Container) component, component.getParent());
    }

    @Override // java.awt.event.ComponentListener
    public void componentShown(ComponentEvent componentEvent) {
        Component component = componentEvent.getComponent();
        if (component == this.firstInvisibleAncestor) {
            addListeners(component, false);
            if (this.firstInvisibleAncestor == null) {
                fireAncestorAdded(this.root, 1, (Container) component, component.getParent());
            }
        }
    }

    @Override // java.awt.event.ComponentListener
    public void componentHidden(ComponentEvent componentEvent) {
        Component component = componentEvent.getComponent();
        boolean z2 = this.firstInvisibleAncestor == null;
        if (!(component instanceof Window)) {
            removeListeners(component.getParent());
        }
        this.firstInvisibleAncestor = component;
        if (z2) {
            fireAncestorRemoved(this.root, 2, (Container) component, component.getParent());
        }
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName != null) {
            if (propertyName.equals("parent") || propertyName.equals("ancestor")) {
                Container container = (JComponent) propertyChangeEvent.getSource();
                if (propertyChangeEvent.getNewValue() != null) {
                    if (container == this.firstInvisibleAncestor) {
                        addListeners(container, false);
                        if (this.firstInvisibleAncestor == null) {
                            fireAncestorAdded(this.root, 1, container, container.getParent());
                            return;
                        }
                        return;
                    }
                    return;
                }
                boolean z2 = this.firstInvisibleAncestor == null;
                Container container2 = (Container) propertyChangeEvent.getOldValue();
                removeListeners(container2);
                this.firstInvisibleAncestor = container;
                if (z2) {
                    fireAncestorRemoved(this.root, 2, container, container2);
                }
            }
        }
    }
}
