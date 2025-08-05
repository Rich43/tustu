package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor.ReadOnlyListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import sun.reflect.misc.MethodUtil;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanBooleanProperty.class */
public final class ReadOnlyJavaBeanBooleanProperty extends ReadOnlyBooleanPropertyBase implements ReadOnlyJavaBeanProperty<Boolean> {
    private final ReadOnlyPropertyDescriptor descriptor;
    private final ReadOnlyPropertyDescriptor.ReadOnlyListener<Boolean> listener;
    private final AccessControlContext acc = AccessController.getContext();

    ReadOnlyJavaBeanBooleanProperty(ReadOnlyPropertyDescriptor descriptor, Object bean) throws IllegalArgumentException {
        this.descriptor = descriptor;
        descriptor.getClass();
        this.listener = descriptor.new ReadOnlyListener<>(bean, this);
        descriptor.addListener(this.listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(descriptor, this.listener));
    }

    @Override // javafx.beans.value.ObservableBooleanValue
    public boolean get() {
        return ((Boolean) AccessController.doPrivileged(() -> {
            try {
                return (Boolean) MethodUtil.invoke(this.descriptor.getGetter(), getBean(), (Object[]) null);
            } catch (IllegalAccessException e2) {
                throw new UndeclaredThrowableException(e2);
            } catch (InvocationTargetException e3) {
                throw new UndeclaredThrowableException(e3);
            }
        }, this.acc)).booleanValue();
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public Object getBean() {
        return this.listener.getBean();
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public String getName() {
        return this.descriptor.getName();
    }

    @Override // javafx.beans.property.ReadOnlyBooleanPropertyBase
    public void fireValueChangedEvent() {
        super.fireValueChangedEvent();
    }

    @Override // javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
    public void dispose() throws IllegalArgumentException {
        this.descriptor.removeListener(this.listener);
    }
}
