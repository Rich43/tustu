package java.beans.beancontext;

import java.util.EventObject;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextEvent.class */
public abstract class BeanContextEvent extends EventObject {
    private static final long serialVersionUID = 7267998073569045052L;
    protected BeanContext propagatedFrom;

    protected BeanContextEvent(BeanContext beanContext) {
        super(beanContext);
    }

    public BeanContext getBeanContext() {
        return (BeanContext) getSource();
    }

    public synchronized void setPropagatedFrom(BeanContext beanContext) {
        this.propagatedFrom = beanContext;
    }

    public synchronized BeanContext getPropagatedFrom() {
        return this.propagatedFrom;
    }

    public synchronized boolean isPropagated() {
        return this.propagatedFrom != null;
    }
}
