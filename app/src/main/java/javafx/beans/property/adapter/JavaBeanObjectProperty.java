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
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.MethodUtil;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanObjectProperty.class */
public final class JavaBeanObjectProperty<T> extends ObjectProperty<T> implements JavaBeanProperty<T> {
    private final PropertyDescriptor descriptor;
    private final PropertyDescriptor.Listener<T> listener;
    private ObservableValue<? extends T> observable = null;
    private ExpressionHelper<T> helper = null;
    private final AccessControlContext acc = AccessController.getContext();

    JavaBeanObjectProperty(PropertyDescriptor descriptor, Object bean) throws IllegalArgumentException {
        this.descriptor = descriptor;
        descriptor.getClass();
        this.listener = descriptor.new Listener<>(bean, this);
        descriptor.addListener(this.listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(descriptor, this.listener));
    }

    @Override // javafx.beans.value.ObservableObjectValue
    public T get() {
        return (T) AccessController.doPrivileged(() -> {
            try {
                return MethodUtil.invoke(this.descriptor.getGetter(), getBean(), (Object[]) null);
            } catch (IllegalAccessException e2) {
                throw new UndeclaredThrowableException(e2);
            } catch (InvocationTargetException e3) {
                throw new UndeclaredThrowableException(e3);
            }
        }, this.acc);
    }

    @Override // javafx.beans.value.WritableObjectValue
    public void set(T value) {
        if (isBound()) {
            throw new RuntimeException("A bound value cannot be set.");
        }
        AccessController.doPrivileged(() -> {
            try {
                MethodUtil.invoke(this.descriptor.getSetter(), getBean(), new Object[]{value});
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
    public void bind(ObservableValue<? extends T> observable) {
        if (observable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!observable.equals(this.observable)) {
            unbind();
            set(observable.getValue2());
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
    public void addListener(ChangeListener<? super T> listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super T> listener) {
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

    @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.ReadOnlyObjectProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ObjectProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        if (isBound()) {
            result.append("bound, ");
        }
        result.append("value: ").append((Object) get());
        result.append("]");
        return result.toString();
    }
}
