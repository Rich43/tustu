package java.beans.beancontext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: rt.jar:java/beans/beancontext/BeanContextMembershipEvent.class */
public class BeanContextMembershipEvent extends BeanContextEvent {
    private static final long serialVersionUID = 3499346510334590959L;
    protected Collection children;

    public BeanContextMembershipEvent(BeanContext beanContext, Collection collection) {
        super(beanContext);
        if (collection == null) {
            throw new NullPointerException("BeanContextMembershipEvent constructor:  changes is null.");
        }
        this.children = collection;
    }

    public BeanContextMembershipEvent(BeanContext beanContext, Object[] objArr) {
        super(beanContext);
        if (objArr == null) {
            throw new NullPointerException("BeanContextMembershipEvent:  changes is null.");
        }
        this.children = Arrays.asList(objArr);
    }

    public int size() {
        return this.children.size();
    }

    public boolean contains(Object obj) {
        return this.children.contains(obj);
    }

    public Object[] toArray() {
        return this.children.toArray();
    }

    public Iterator iterator() {
        return this.children.iterator();
    }
}
