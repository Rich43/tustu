package javafx.beans.property;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.BooleanExpression;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyBooleanProperty.class */
public abstract class ReadOnlyBooleanProperty extends BooleanExpression implements ReadOnlyProperty<Boolean> {
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyBooleanProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static ReadOnlyBooleanProperty readOnlyBooleanProperty(final ReadOnlyProperty<Boolean> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return property instanceof ReadOnlyBooleanProperty ? (ReadOnlyBooleanProperty) property : new ReadOnlyBooleanPropertyBase() { // from class: javafx.beans.property.ReadOnlyBooleanProperty.1
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
            @Override // javafx.beans.value.ObservableBooleanValue
            public boolean get() {
                this.valid = true;
                Boolean value = (Boolean) property.getValue2();
                if (value == null) {
                    return false;
                }
                return value.booleanValue();
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

    @Override // javafx.beans.binding.BooleanExpression
    public ReadOnlyObjectProperty<Boolean> asObject() {
        return new ReadOnlyObjectPropertyBase<Boolean>() { // from class: javafx.beans.property.ReadOnlyBooleanProperty.2
            private boolean valid = true;
            private final InvalidationListener listener = observable -> {
                if (this.valid) {
                    this.valid = false;
                    fireValueChangedEvent();
                }
            };

            {
                ReadOnlyBooleanProperty.this.addListener(new WeakInvalidationListener(this.listener));
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return ReadOnlyBooleanProperty.this.getName();
            }

            @Override // javafx.beans.value.ObservableObjectValue
            public Boolean get() {
                this.valid = true;
                return ReadOnlyBooleanProperty.this.getValue2();
            }
        };
    }
}
