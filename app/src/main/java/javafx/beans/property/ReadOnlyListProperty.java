package javafx.beans.property;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ListExpression;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyListProperty.class */
public abstract class ReadOnlyListProperty<E> extends ListExpression<E> implements ReadOnlyProperty<ObservableList<E>> {
    public void bindContentBidirectional(ObservableList<E> list) {
        Bindings.bindContentBidirectional(this, list);
    }

    public void unbindContentBidirectional(Object object) {
        Bindings.unbindContentBidirectional(this, object);
    }

    public void bindContent(ObservableList<E> list) {
        Bindings.bindContent(this, list);
    }

    public void unbindContent(Object object) {
        Bindings.unbindContent(this, object);
    }

    @Override // java.util.List
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        List list = (List) obj;
        if (size() != list.size()) {
            return false;
        }
        ListIterator<E> e1 = listIterator();
        ListIterator e2 = list.listIterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (o1 == null) {
                if (o2 != null) {
                    return false;
                }
            } else if (!o1.equals(o2)) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.List
    public int hashCode() {
        int hashCode = 1;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E e2 = it.next();
            hashCode = (31 * hashCode) + (e2 == null ? 0 : e2.hashCode());
        }
        return hashCode;
    }

    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyListProperty [");
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
