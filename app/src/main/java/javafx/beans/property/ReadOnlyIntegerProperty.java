package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.IntegerExpression;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyIntegerProperty.class */
public abstract class ReadOnlyIntegerProperty extends IntegerExpression implements ReadOnlyProperty<Number> {
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyIntegerProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static <T extends Number> ReadOnlyIntegerProperty readOnlyIntegerProperty(final ReadOnlyProperty<T> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return property instanceof ReadOnlyIntegerProperty ? (ReadOnlyIntegerProperty) property : new ReadOnlyIntegerPropertyBase() { // from class: javafx.beans.property.ReadOnlyIntegerProperty.1
            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (this.valid) {
                    this.valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                property.addListener(new WeakInvalidationListener(this.listener));
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // javafx.beans.value.ObservableIntegerValue
            public int get() {
                this.valid = true;
                Number number = (Number) property.getValue2();
                if (number == null) {
                    return 0;
                }
                return number.intValue();
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return property.getName();
            }
        };
    }

    @Override // javafx.beans.binding.IntegerExpression
    public ReadOnlyObjectProperty<Integer> asObject() {
        return new ReadOnlyObjectPropertyBase<Integer>() { // from class: javafx.beans.property.ReadOnlyIntegerProperty.2
            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (this.valid) {
                    this.valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                ReadOnlyIntegerProperty.this.addListener(new WeakInvalidationListener(this.listener));
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return ReadOnlyIntegerProperty.this.getName();
            }

            @Override // javafx.beans.value.ObservableObjectValue
            public Integer get() {
                this.valid = true;
                return ReadOnlyIntegerProperty.this.getValue2();
            }
        };
    }
}
