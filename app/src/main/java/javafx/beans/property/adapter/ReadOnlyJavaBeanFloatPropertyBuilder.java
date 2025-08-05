package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanFloatPropertyBuilder.class */
public final class ReadOnlyJavaBeanFloatPropertyBuilder {
    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    public static ReadOnlyJavaBeanFloatPropertyBuilder create() {
        return new ReadOnlyJavaBeanFloatPropertyBuilder();
    }

    public ReadOnlyJavaBeanFloatProperty build() throws NoSuchMethodException {
        ReadOnlyPropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Float.TYPE.equals(descriptor.getType()) && !Number.class.isAssignableFrom(descriptor.getType())) {
            throw new IllegalArgumentException("Not a float property");
        }
        return new ReadOnlyJavaBeanFloatProperty(descriptor, this.helper.getBean());
    }

    public ReadOnlyJavaBeanFloatPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public ReadOnlyJavaBeanFloatPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public ReadOnlyJavaBeanFloatPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public ReadOnlyJavaBeanFloatPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public ReadOnlyJavaBeanFloatPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }
}
