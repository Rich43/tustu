package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableIntegerValue;

/* loaded from: jfxrt.jar:javafx/beans/property/IntegerProperty.class */
public abstract class IntegerProperty extends ReadOnlyIntegerProperty implements Property<Number>, WritableIntegerValue {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(Number v2) {
        if (v2 == null) {
            Logging.getLogger().fine("Attempt to set integer property to null, using default value instead.", new NullPointerException());
            set(0);
        } else {
            set(v2.intValue());
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

    @Override // javafx.beans.property.ReadOnlyIntegerProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("IntegerProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static IntegerProperty integerProperty(final Property<Integer> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return new IntegerPropertyBase() { // from class: javafx.beans.property.IntegerProperty.1
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((IntegerProperty) this, (Property<Integer>) property);
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

    @Override // javafx.beans.property.ReadOnlyIntegerProperty, javafx.beans.binding.IntegerExpression
    public ObjectProperty<Integer> asObject() {
        return new ObjectPropertyBase<Integer>() { // from class: javafx.beans.property.IntegerProperty.2
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((Property<Integer>) this, IntegerProperty.this);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return IntegerProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    AccessController.doPrivileged(() -> {
                        BidirectionalBinding.unbindNumber(this, IntegerProperty.this);
                        return null;
                    }, this.acc);
                } finally {
                    super.finalize();
                }
            }
        };
    }
}
