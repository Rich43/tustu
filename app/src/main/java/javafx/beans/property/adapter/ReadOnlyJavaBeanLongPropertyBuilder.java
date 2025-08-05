package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanLongPropertyBuilder.class */
public final class ReadOnlyJavaBeanLongPropertyBuilder {
    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    public static ReadOnlyJavaBeanLongPropertyBuilder create() {
        return new ReadOnlyJavaBeanLongPropertyBuilder();
    }

    public ReadOnlyJavaBeanLongProperty build() throws NoSuchMethodException {
        ReadOnlyPropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Long.TYPE.equals(descriptor.getType()) && !Number.class.isAssignableFrom(descriptor.getType())) {
            throw new IllegalArgumentException("Not a long property");
        }
        return new ReadOnlyJavaBeanLongProperty(descriptor, this.helper.getBean());
    }

    public ReadOnlyJavaBeanLongPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public ReadOnlyJavaBeanLongPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public ReadOnlyJavaBeanLongPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public ReadOnlyJavaBeanLongPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public ReadOnlyJavaBeanLongPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }
}
