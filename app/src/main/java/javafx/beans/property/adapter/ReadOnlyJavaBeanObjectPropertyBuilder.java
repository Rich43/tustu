package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanObjectPropertyBuilder.class */
public final class ReadOnlyJavaBeanObjectPropertyBuilder<T> {
    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    public static <T> ReadOnlyJavaBeanObjectPropertyBuilder<T> create() {
        return new ReadOnlyJavaBeanObjectPropertyBuilder<>();
    }

    public ReadOnlyJavaBeanObjectProperty<T> build() throws NoSuchMethodException {
        ReadOnlyPropertyDescriptor descriptor = this.helper.getDescriptor();
        return new ReadOnlyJavaBeanObjectProperty<>(descriptor, this.helper.getBean());
    }

    public ReadOnlyJavaBeanObjectPropertyBuilder<T> name(String name) {
        this.helper.name(name);
        return this;
    }

    public ReadOnlyJavaBeanObjectPropertyBuilder<T> bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public ReadOnlyJavaBeanObjectPropertyBuilder<T> beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public ReadOnlyJavaBeanObjectPropertyBuilder<T> getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public ReadOnlyJavaBeanObjectPropertyBuilder<T> getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }
}
