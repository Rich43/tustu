package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.FloatExpression;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyFloatProperty.class */
public abstract class ReadOnlyFloatProperty extends FloatExpression implements ReadOnlyProperty<Number> {
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyFloatProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static <T extends Number> ReadOnlyFloatProperty readOnlyFloatProperty(final ReadOnlyProperty<T> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return property instanceof ReadOnlyFloatProperty ? (ReadOnlyFloatProperty) property : new ReadOnlyFloatPropertyBase() { // from class: javafx.beans.property.ReadOnlyFloatProperty.1
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
            @Override // javafx.beans.value.ObservableFloatValue
            public float get() {
                this.valid = true;
                Number number = (Number) property.getValue2();
                if (number == null) {
                    return 0.0f;
                }
                return number.floatValue();
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

    @Override // javafx.beans.binding.FloatExpression
    public ReadOnlyObjectProperty<Float> asObject() {
        return new ReadOnlyObjectPropertyBase<Float>() { // from class: javafx.beans.property.ReadOnlyFloatProperty.2
            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (this.valid) {
                    this.valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                ReadOnlyFloatProperty.this.addListener(new WeakInvalidationListener(this.listener));
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return ReadOnlyFloatProperty.this.getName();
            }

            @Override // javafx.beans.value.ObservableObjectValue
            public Float get() {
                this.valid = true;
                return ReadOnlyFloatProperty.this.getValue2();
            }
        };
    }
}
