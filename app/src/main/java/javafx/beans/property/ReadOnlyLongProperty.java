package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.LongExpression;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyLongProperty.class */
public abstract class ReadOnlyLongProperty extends LongExpression implements ReadOnlyProperty<Number> {
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyLongProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static <T extends Number> ReadOnlyLongProperty readOnlyLongProperty(final ReadOnlyProperty<T> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return property instanceof ReadOnlyLongProperty ? (ReadOnlyLongProperty) property : new ReadOnlyLongPropertyBase() { // from class: javafx.beans.property.ReadOnlyLongProperty.1
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
            @Override // javafx.beans.value.ObservableLongValue
            public long get() {
                this.valid = true;
                Number number = (Number) property.getValue2();
                if (number == null) {
                    return 0L;
                }
                return number.longValue();
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

    @Override // javafx.beans.binding.LongExpression
    public ReadOnlyObjectProperty<Long> asObject() {
        return new ReadOnlyObjectPropertyBase<Long>() { // from class: javafx.beans.property.ReadOnlyLongProperty.2
            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (this.valid) {
                    this.valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                ReadOnlyLongProperty.this.addListener(new WeakInvalidationListener(this.listener));
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return ReadOnlyLongProperty.this.getName();
            }

            @Override // javafx.beans.value.ObservableObjectValue
            public Long get() {
                this.valid = true;
                return ReadOnlyLongProperty.this.getValue2();
            }
        };
    }
}
