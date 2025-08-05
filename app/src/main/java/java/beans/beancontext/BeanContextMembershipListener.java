package java.beans.beancontext;

import java.util.EventListener;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextMembershipListener.class */
public interface BeanContextMembershipListener extends EventListener {
    void childrenAdded(BeanContextMembershipEvent beanContextMembershipEvent);

    void childrenRemoved(BeanContextMembershipEvent beanContextMembershipEvent);
}
