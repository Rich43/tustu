package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableDoubleValue;

/* loaded from: jfxrt.jar:javafx/beans/property/DoubleProperty.class */
public abstract class DoubleProperty extends ReadOnlyDoubleProperty implements Property<Number>, WritableDoubleValue {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(Number v2) {
        if (v2 == null) {
            Logging.getLogger().fine("Attempt to set double property to null, using default value instead.", new NullPointerException());
            set(0.0d);
        } else {
            set(v2.doubleValue());
        }
    }

    @Override // javafx.beans.property.Property
    public void bindBidirectional(Property<Number> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override // javafx.beans.property.Property
    public void unbindBidirectional(Property<Number> other) {
        Bindings.unbindBidirectional((Property) this, (Property) other);
    }

    @Override // javafx.beans.property.ReadOnlyDoubleProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("DoubleProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static DoubleProperty doubleProperty(final Property<Double> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return new DoublePropertyBase() { // from class: javafx.beans.property.DoubleProperty.1
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((DoubleProperty) this, (Property<Double>) property);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return property.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    Property property2 = property;
                    AccessController.doPrivileged(() -> {
                        BidirectionalBinding.unbindNumber(property2, this);
                        return null;
                    }, this.acc);
                } finally {
                    super.finalize();
                }
            }
        };
    }

    @Override // javafx.beans.property.ReadOnlyDoubleProperty, javafx.beans.binding.DoubleExpression
    public ObjectProperty<Double> asObject() {
        return new ObjectPropertyBase<Double>() { // from class: javafx.beans.property.DoubleProperty.2
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((Property<Double>) this, DoubleProperty.this);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return DoubleProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    AccessController.doPrivileged(() -> {
                        BidirectionalBinding.unbindNumber(this, DoubleProperty.this);
                        return null;
                    }, this.acc);
                } finally {
                    super.finalize();
                }
            }
        };
    }
}
