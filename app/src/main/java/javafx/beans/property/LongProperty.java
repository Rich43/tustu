package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableLongValue;

/* loaded from: jfxrt.jar:javafx/beans/property/LongProperty.class */
public abstract class LongProperty extends ReadOnlyLongProperty implements Property<Number>, WritableLongValue {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(Number v2) {
        if (v2 == null) {
            Logging.getLogger().fine("Attempt to set long property to null, using default value instead.", new NullPointerException());
            set(0L);
        } else {
            set(v2.longValue());
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

    @Override // javafx.beans.property.ReadOnlyLongProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("LongProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static LongProperty longProperty(final Property<Long> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return new LongPropertyBase() { // from class: javafx.beans.property.LongProperty.1
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((LongProperty) this, (Property<Long>) property);
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

    @Override // javafx.beans.property.ReadOnlyLongProperty, javafx.beans.binding.LongExpression
    public ObjectProperty<Long> asObject() {
        return new ObjectPropertyBase<Long>() { // from class: javafx.beans.property.LongProperty.2
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((Property<Long>) this, LongProperty.this);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return LongProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    AccessController.doPrivileged(() -> {
                        BidirectionalBinding.unbindNumber(this, LongProperty.this);
                        return null;
                    }, this.acc);
                } finally {
                    super.finalize();
                }
            }
        };
    }
}
