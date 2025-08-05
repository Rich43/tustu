package javax.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/ActionPropertyChangeListener.class */
abstract class ActionPropertyChangeListener<T extends JComponent> implements PropertyChangeListener, Serializable {
    private static ReferenceQueue<JComponent> queue;
    private transient OwnedWeakReference<T> target;
    private Action action;

    protected abstract void actionPropertyChanged(T t2, Action action, PropertyChangeEvent propertyChangeEvent);

    private static ReferenceQueue<JComponent> getQueue() {
        synchronized (ActionPropertyChangeListener.class) {
            if (queue == null) {
                queue = new ReferenceQueue<>();
            }
        }
        return queue;
    }

    public ActionPropertyChangeListener(T t2, Action action) {
        setTarget(t2);
        this.action = action;
    }

    @Override // java.beans.PropertyChangeListener
    public final void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        JComponent target = getTarget();
        if (target == null) {
            getAction().removePropertyChangeListener(this);
        } else {
            actionPropertyChanged(target, getAction(), propertyChangeEvent);
        }
    }

    private void setTarget(T t2) {
        ReferenceQueue<JComponent> queue2 = getQueue();
        while (true) {
            OwnedWeakReference ownedWeakReference = (OwnedWeakReference) queue2.poll();
            if (ownedWeakReference != null) {
                ActionPropertyChangeListener<?> owner = ownedWeakReference.getOwner();
                Action action = owner.getAction();
                if (action != null) {
                    action.removePropertyChangeListener(owner);
                }
            } else {
                this.target = new OwnedWeakReference<>(t2, queue2, this);
                return;
            }
        }
    }

    public T getTarget() {
        if (this.target == null) {
            return null;
        }
        return this.target.get();
    }

    public Action getAction() {
        return this.action;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(getTarget());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        JComponent jComponent = (JComponent) objectInputStream.readObject();
        if (jComponent != null) {
            setTarget(jComponent);
        }
    }

    /* loaded from: rt.jar:javax/swing/ActionPropertyChangeListener$OwnedWeakReference.class */
    private static class OwnedWeakReference<U extends JComponent> extends WeakReference<U> {
        private ActionPropertyChangeListener<?> owner;

        OwnedWeakReference(U u2, ReferenceQueue<? super U> referenceQueue, ActionPropertyChangeListener<?> actionPropertyChangeListener) {
            super(u2, referenceQueue);
            this.owner = actionPropertyChangeListener;
        }

        public ActionPropertyChangeListener<?> getOwner() {
            return this.owner;
        }
    }
}
