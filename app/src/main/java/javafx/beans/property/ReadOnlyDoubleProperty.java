package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.DoubleExpression;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyDoubleProperty.class */
public abstract class ReadOnlyDoubleProperty extends DoubleExpression implements ReadOnlyProperty<Number> {
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyDoubleProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static <T extends Number> ReadOnlyDoubleProperty readOnlyDoubleProperty(final ReadOnlyProperty<T> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return property instanceof ReadOnlyDoubleProperty ? (ReadOnlyDoubleProperty) property : new ReadOnlyDoublePropertyBase() { // from class: javafx.beans.property.ReadOnlyDoubleProperty.1
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
            @Override // javafx.beans.value.ObservableDoubleValue
            public double get() {
                this.valid = true;
                Number number = (Number) property.getValue2();
                if (number == null) {
                    return 0.0d;
                }
                return number.doubleValue();
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

    @Override // javafx.beans.binding.DoubleExpression
    public ReadOnlyObjectProperty<Double> asObject() {
        return new ReadOnlyObjectPropertyBase<Double>() { // from class: javafx.beans.property.ReadOnlyDoubleProperty.2
            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (this.valid) {
                    this.valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                ReadOnlyDoubleProperty.this.addListener(new WeakInvalidationListener(this.listener));
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return ReadOnlyDoubleProperty.this.getName();
            }

            @Override // javafx.beans.value.ObservableObjectValue
            public Double get() {
                this.valid = true;
                return ReadOnlyDoubleProperty.this.getValue2();
            }
        };
    }
}
