package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor.ReadOnlyListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import sun.reflect.misc.MethodUtil;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanObjectProperty.class */
public final class ReadOnlyJavaBeanObjectProperty<T> extends ReadOnlyObjectPropertyBase<T> implements ReadOnlyJavaBeanProperty<T> {
    private final ReadOnlyPropertyDescriptor descriptor;
    private final ReadOnlyPropertyDescriptor.ReadOnlyListener<T> listener;
    private final AccessControlContext acc = AccessController.getContext();

    ReadOnlyJavaBeanObjectProperty(ReadOnlyPropertyDescriptor descriptor, Object bean) throws IllegalArgumentException {
        this.descriptor = descriptor;
        descriptor.getClass();
        this.listener = descriptor.new ReadOnlyListener<>(bean, this);
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

    @Override // javafx.beans.property.ReadOnlyProperty
    public Object getBean() {
        return this.listener.getBean();
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public String getName() {
        return this.descriptor.getName();
    }

    @Override // javafx.beans.property.ReadOnlyObjectPropertyBase
    public void fireValueChangedEvent() {
        super.fireValueChangedEvent();
    }

    @Override // javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
    public void dispose() throws IllegalArgumentException {
        this.descriptor.removeListener(this.listener);
    }
}
