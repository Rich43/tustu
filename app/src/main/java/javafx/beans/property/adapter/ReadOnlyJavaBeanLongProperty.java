package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.Disposer;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor.ReadOnlyListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessControlContext;
import java.security.AccessController;
import javafx.beans.property.ReadOnlyLongPropertyBase;
import sun.reflect.misc.MethodUtil;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanLongProperty.class */
public final class ReadOnlyJavaBeanLongProperty extends ReadOnlyLongPropertyBase implements ReadOnlyJavaBeanProperty<Number> {
    private final ReadOnlyPropertyDescriptor descriptor;
    private final ReadOnlyPropertyDescriptor.ReadOnlyListener<Number> listener;
    private final AccessControlContext acc = AccessController.getContext();

    ReadOnlyJavaBeanLongProperty(ReadOnlyPropertyDescriptor descriptor, Object bean) throws IllegalArgumentException {
        this.descriptor = descriptor;
        descriptor.getClass();
        this.listener = descriptor.new ReadOnlyListener<>(bean, this);
        descriptor.addListener(this.listener);
        Disposer.addRecord(this, new DescriptorListenerCleaner(descriptor, this.listener));
    }

    @Override // javafx.beans.value.ObservableLongValue
    public long get() {
        return ((Long) AccessController.doPrivileged(() -> {
            try {
                return Long.valueOf(((Number) MethodUtil.invoke(this.descriptor.getGetter(), getBean(), (Object[]) null)).longValue());
            } catch (IllegalAccessException e2) {
                throw new UndeclaredThrowableException(e2);
            } catch (InvocationTargetException e3) {
                throw new UndeclaredThrowableException(e3);
            }
        }, this.acc)).longValue();
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public Object getBean() {
        return this.listener.getBean();
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public String getName() {
        return this.descriptor.getName();
    }

    @Override // javafx.beans.property.ReadOnlyLongPropertyBase, javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
    public void fireValueChangedEvent() {
        super.fireValueChangedEvent();
    }

    @Override // javafx.beans.property.adapter.ReadOnlyJavaBeanProperty
    public void dispose() throws IllegalArgumentException {
        this.descriptor.removeListener(this.listener);
    }
}
