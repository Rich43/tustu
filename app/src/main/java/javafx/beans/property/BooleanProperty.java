package javafx.beans.property;

import com.sun.javafx.binding.BidirectionalBinding;
import com.sun.javafx.binding.Logging;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableBooleanValue;

/* loaded from: jfxrt.jar:javafx/beans/property/BooleanProperty.class */
public abstract class BooleanProperty extends ReadOnlyBooleanProperty implements Property<Boolean>, WritableBooleanValue {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(Boolean v2) {
        if (v2 == null) {
            Logging.getLogger().fine("Attempt to set boolean property to null, using default value instead.", new NullPointerException());
            set(false);
        } else {
            set(v2.booleanValue());
        }
    }

    @Override // javafx.beans.property.Property
    public void bindBidirectional(Property<Boolean> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override // javafx.beans.property.Property
    public void unbindBidirectional(Property<Boolean> other) {
        Bindings.unbindBidirectional((Property) this, (Property) other);
    }

    @Override // javafx.beans.property.ReadOnlyBooleanProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("BooleanProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }

    public static BooleanProperty booleanProperty(final Property<Boolean> property) {
        if (property == null) {
            throw new NullPointerException("Property cannot be null");
        }
        return property instanceof BooleanProperty ? (BooleanProperty) property : new BooleanPropertyBase() { // from class: javafx.beans.property.BooleanProperty.1
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bind(this, property);
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
                        BidirectionalBinding.unbind(property2, (Property) this);
                        return null;
                    }, this.acc);
                } finally {
                    super.finalize();
                }
            }
        };
    }

    @Override // javafx.beans.property.ReadOnlyBooleanProperty, javafx.beans.binding.BooleanExpression
    public ObjectProperty<Boolean> asObject() {
        return new ObjectPropertyBase<Boolean>() { // from class: javafx.beans.property.BooleanProperty.2
            private final AccessControlContext acc = AccessController.getContext();

            {
                BidirectionalBinding.bind(this, BooleanProperty.this);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return null;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return BooleanProperty.this.getName();
            }

            protected void finalize() throws Throwable {
                try {
                    AccessController.doPrivileged(() -> {
                        BidirectionalBinding.unbind((Property) this, (Property) BooleanProperty.this);
                        return null;
                    }, this.acc);
                } finally {
                    super.finalize();
                }
            }
        };
    }
}
