package javafx.beans.property;

import javafx.beans.binding.ObjectExpression;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyObjectProperty.class */
public abstract class ReadOnlyObjectProperty<T> extends ObjectExpression<T> implements ReadOnlyProperty<T> {
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyObjectProperty [");
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
