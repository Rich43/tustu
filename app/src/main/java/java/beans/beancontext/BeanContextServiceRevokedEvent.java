package java.beans.beancontext;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextServiceRevokedEvent.class */
public class BeanContextServiceRevokedEvent extends BeanContextEvent {
    private static final long serialVersionUID = -1295543154724961754L;
    protected Class serviceClass;
    private boolean invalidateRefs;

    public BeanContextServiceRevokedEvent(BeanContextServices beanContextServices, Class cls, boolean z2) {
        super(beanContextServices);
        this.serviceClass = cls;
        this.invalidateRefs = z2;
    }

    public BeanContextServices getSourceAsBeanContextServices() {
        return (BeanContextServices) getBeanContext();
    }

    public Class getServiceClass() {
        return this.serviceClass;
    }

    public boolean isServiceClass(Class cls) {
        return this.serviceClass.equals(cls);
    }

    public boolean isCurrentServiceInvalidNow() {
        return this.invalidateRefs;
    }
}
