package java.beans.beancontext;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextChildSupport.class */
public class BeanContextChildSupport implements BeanContextChild, BeanContextServicesListener, Serializable {
    static final long serialVersionUID = 6328947014421475877L;
    public BeanContextChild beanContextChildPeer;
    protected PropertyChangeSupport pcSupport;
    protected VetoableChangeSupport vcSupport;
    protected transient BeanContext beanContext;
    protected transient boolean rejectedSetBCOnce;

    public BeanContextChildSupport() {
        this.beanContextChildPeer = this;
        this.pcSupport = new PropertyChangeSupport(this.beanContextChildPeer);
        this.vcSupport = new VetoableChangeSupport(this.beanContextChildPeer);
    }

    public BeanContextChildSupport(BeanContextChild beanContextChild) {
        this.beanContextChildPeer = beanContextChild != null ? beanContextChild : this;
        this.pcSupport = new PropertyChangeSupport(this.beanContextChildPeer);
        this.vcSupport = new VetoableChangeSupport(this.beanContextChildPeer);
    }

    @Override // java.beans.beancontext.BeanContextChild
    public synchronized void setBeanContext(BeanContext beanContext) throws PropertyVetoException {
        if (beanContext == this.beanContext) {
            return;
        }
        BeanContext beanContext2 = this.beanContext;
        if (!this.rejectedSetBCOnce) {
            boolean z2 = !validatePendingSetBeanContext(beanContext);
            this.rejectedSetBCOnce = z2;
            if (z2) {
                throw new PropertyVetoException("setBeanContext() change rejected:", new PropertyChangeEvent(this.beanContextChildPeer, "beanContext", beanContext2, beanContext));
            }
            try {
                fireVetoableChange("beanContext", beanContext2, beanContext);
            } catch (PropertyVetoException e2) {
                this.rejectedSetBCOnce = true;
                throw e2;
            }
        }
        if (this.beanContext != null) {
            releaseBeanContextResources();
        }
        this.beanContext = beanContext;
        this.rejectedSetBCOnce = false;
        firePropertyChange("beanContext", beanContext2, beanContext);
        if (this.beanContext != null) {
            initializeBeanContextResources();
        }
    }

    @Override // java.beans.beancontext.BeanContextChild
    public synchronized BeanContext getBeanContext() {
        return this.beanContext;
    }

    @Override // java.beans.beancontext.BeanContextChild
    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.pcSupport.addPropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.beans.beancontext.BeanContextChild
    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.pcSupport.removePropertyChangeListener(str, propertyChangeListener);
    }

    @Override // java.beans.beancontext.BeanContextChild
    public void addVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        this.vcSupport.addVetoableChangeListener(str, vetoableChangeListener);
    }

    @Override // java.beans.beancontext.BeanContextChild
    public void removeVetoableChangeListener(String str, VetoableChangeListener vetoableChangeListener) {
        this.vcSupport.removeVetoableChangeListener(str, vetoableChangeListener);
    }

    @Override // java.beans.beancontext.BeanContextServiceRevokedListener
    public void serviceRevoked(BeanContextServiceRevokedEvent beanContextServiceRevokedEvent) {
    }

    @Override // java.beans.beancontext.BeanContextServicesListener
    public void serviceAvailable(BeanContextServiceAvailableEvent beanContextServiceAvailableEvent) {
    }

    public BeanContextChild getBeanContextChildPeer() {
        return this.beanContextChildPeer;
    }

    public boolean isDelegated() {
        return !equals(this.beanContextChildPeer);
    }

    public void firePropertyChange(String str, Object obj, Object obj2) {
        this.pcSupport.firePropertyChange(str, obj, obj2);
    }

    public void fireVetoableChange(String str, Object obj, Object obj2) throws PropertyVetoException {
        this.vcSupport.fireVetoableChange(str, obj, obj2);
    }

    public boolean validatePendingSetBeanContext(BeanContext beanContext) {
        return true;
    }

    protected void releaseBeanContextResources() {
    }

    protected void initializeBeanContextResources() {
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (!equals(this.beanContextChildPeer) && !(this.beanContextChildPeer instanceof Serializable)) {
            throw new IOException("BeanContextChildSupport beanContextChildPeer not Serializable");
        }
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
    }
}
