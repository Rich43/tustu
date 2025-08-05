package java.beans.beancontext;

import java.util.Iterator;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextServiceAvailableEvent.class */
public class BeanContextServiceAvailableEvent extends BeanContextEvent {
    private static final long serialVersionUID = -5333985775656400778L;
    protected Class serviceClass;

    public BeanContextServiceAvailableEvent(BeanContextServices beanContextServices, Class cls) {
        super(beanContextServices);
        this.serviceClass = cls;
    }

    public BeanContextServices getSourceAsBeanContextServices() {
        return (BeanContextServices) getBeanContext();
    }

    public Class getServiceClass() {
        return this.serviceClass;
    }

    public Iterator getCurrentServiceSelectors() {
        return ((BeanContextServices) getSource()).getCurrentServiceSelectors(this.serviceClass);
    }
}
