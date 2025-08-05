package javafx.beans.property;

import java.util.Iterator;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.SetExpression;
import javafx.collections.ObservableSet;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlySetProperty.class */
public abstract class ReadOnlySetProperty<E> extends SetExpression<E> implements ReadOnlyProperty<ObservableSet<E>> {
    public void bindContentBidirectional(ObservableSet<E> set) {
        Bindings.bindContentBidirectional(this, set);
    }

    public void unbindContentBidirectional(Object object) {
        Bindings.unbindContentBidirectional(this, object);
    }

    public void bindContent(ObservableSet<E> set) {
        Bindings.bindContent(this, set);
    }

    public void unbindContent(Object object) {
        Bindings.unbindContent(this, object);
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set c2 = (Set) obj;
        if (c2.size() != size()) {
            return false;
        }
        try {
            return containsAll(c2);
        } catch (ClassCastException e2) {
            return false;
        } catch (NullPointerException e3) {
            return false;
        }
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public int hashCode() {
        int h2 = 0;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e2 = it.next();
            if (e2 != null) {
                h2 += e2.hashCode();
            }
        }
        return h2;
    }

    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlySetProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append((Object) get()).append("]");
        return result.toString();
    }
}
