package javafx.beans.property.adapter;

import com.sun.javafx.binding.ExpressionHelper;
import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import com.sun.javafx.property.adapter.PropertyDescriptor.Listener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.InvalidationListener;
import javafx.beans.property.FloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanFloatProperty.class */
public final class JavaBeanFloatProperty extends FloatProperty implements JavaBeanProperty<Number> {
    private final PropertyDescriptor descriptor;
    private final PropertyDescriptor.Listener<Number> listener;
    private ObservableValue<? extends Number> observable = null;
    private ExpressionHelper<Number> helper = null;
    private final AccessControlContext acc = AccessController.getContext();

    JavaBeanFloatProperty(PropertyDescriptor descriptor, Object bean) throws IllegalArgumentException {
        this.descriptor = descriptor;
        descriptor.getClass();
        this.listener = descriptor.new Listener<>(bean, this);
        descriptor.addListener(this.listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(descriptor, this.listener));
    }

    @Override // javafx.beans.value.ObservableFloatValue
    public float get() {
        return ((Float) AccessController.doPrivileged(() -> {
            try {
                return Float.valueOf(((Number) MethodUtil.invoke(this.descriptor.getGetter(), getBean(), (Object[]) null)).floatValue());
            } catch (IllegalAccessException e2) {
                throw new UndeclaredThrowableException(e2);
            } catch (InvocationTargetException e3) {
                throw new UndeclaredThrowableException(e3);
            }
        }, this.acc)).floatValue();
    }

    @Override // javafx.beans.value.WritableFloatValue
    public void set(float value) {
        if (isBound()) {
            throw new RuntimeException("A bound value cannot be set.");
        }
        AccessController.doPrivileged(() -> {
            try {
                MethodUtil.invoke(this.descriptor.getSetter(), getBean(), new Object[]{Float.valueOf(value)});
                ExpressionHelper.fireValueChangedEvent(this.helper);
                return null;
            } catch (IllegalAccessException e2) {
                throw new UndeclaredThrowableException(e2);
            } catch (InvocationTargetException e3) {
                throw new UndeclaredThrowableException(e3);
            }
        }, this.acc);
    }

    @Override // javafx.beans.property.Property
    public void bind(ObservableValue<? extends Number> observable) {
        if (observable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!observable.equals(this.observable)) {
            unbind();
            set(observable.getValue2().floatValue());
            this.observable = observable;
            this.observable.addListener(this.listener);
        }
    }

    @Override // javafx.beans.property.Property
    public void unbind() {
        if (this.observable != null) {
            this.observable.removeListener(this.listener);
            this.observable = null;
        }
    }

    @Override // javafx.beans.property.Property
    public boolean isBound() {
        return this.observable != null;
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public Object getBean() {
        return this.listener.getBean();
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public String getName() {
        return this.descriptor.getName();
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super Number> listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Number> listener) {
        this.helper = ExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = ExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
    public void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper);
    }

    @Override // javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
    public void dispose() throws IllegalArgumentException {
        this.descriptor.removeListener(this.listener);
    }

    @Override // javafx.beans.property.FloatProperty, javafx.beans.property.ReadOnlyFloatProperty
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
        if (isBound()) {
            result.append("bound, ");
        }
        result.append("value: ").append(get());
        result.append("]");
        return result.toString();
    }
}
