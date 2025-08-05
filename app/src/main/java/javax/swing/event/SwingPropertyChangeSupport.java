package javax.swing.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import javax.swing.SwingUtilities;

/* loaded from: rt.jar:javax/swing/event/SwingPropertyChangeSupport.class */
public final class SwingPropertyChangeSupport extends PropertyChangeSupport {
    static final long serialVersionUID = 7162625831330845068L;
    private final boolean notifyOnEDT;

    public SwingPropertyChangeSupport(Object obj) {
        this(obj, false);
    }

    public SwingPropertyChangeSupport(Object obj, boolean z2) {
        super(obj);
        this.notifyOnEDT = z2;
    }

    @Override // java.beans.PropertyChangeSupport
    public void firePropertyChange(final PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent == null) {
            throw new NullPointerException();
        }
        if (!isNotifyOnEDT() || SwingUtilities.isEventDispatchThread()) {
            super.firePropertyChange(propertyChangeEvent);
        } else {
            SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.event.SwingPropertyChangeSupport.1
                @Override // java.lang.Runnable
                public void run() {
                    SwingPropertyChangeSupport.this.firePropertyChange(propertyChangeEvent);
                }
            });
        }
    }

    public final boolean isNotifyOnEDT() {
        return this.notifyOnEDT;
    }
}
