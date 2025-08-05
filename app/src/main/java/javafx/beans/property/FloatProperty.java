package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableFloatValue;

/* loaded from: jfxrt.jar:javafx/beans/property/FloatProperty.class */
public abstract class FloatProperty extends ReadOnlyFloatProperty implements Property<Number>, WritableFloatValue {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(Number v2) {
        if (v2 == null) {
            Logging.getLogger().fine("Attempt to set float property to null, using default value instead.", new NullPointerException());
            set(0.0f);
        } else {
            set(v2.floatValue());
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

    @Override // javafx.beans.property.ReadOnlyFloatProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("FloatProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static FloatProperty floatProperty(final Property<Float> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return new FloatPropertyBase() { // from class: javafx.beans.property.FloatProperty.1
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((FloatProperty) this, (Property<Float>) property);
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

    @Override // javafx.beans.property.ReadOnlyFloatProperty, javafx.beans.binding.FloatExpression
    public ObjectProperty<Float> asObject() {
        return new ObjectPropertyBase<Float>() { // from class: javafx.beans.property.FloatProperty.2
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bindNumber((Property<Float>) this, FloatProperty.this);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return FloatProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    AccessController.doPrivileged(() -> {
                        BidirectionalBinding.unbindNumber(this, FloatProperty.this);
                        return null;
                    }, this.acc);
                } finally {
                    super.finalize();
                }
            }
        };
    }
}
