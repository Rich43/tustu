package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanDoublePropertyBuilder.class */
public final class ReadOnlyJavaBeanDoublePropertyBuilder {
    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    public static ReadOnlyJavaBeanDoublePropertyBuilder create() {
        return new ReadOnlyJavaBeanDoublePropertyBuilder();
    }

    public ReadOnlyJavaBeanDoubleProperty build() throws NoSuchMethodException {
        ReadOnlyPropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Double.TYPE.equals(descriptor.getType()) && !Number.class.isAssignableFrom(descriptor.getType())) {
            throw new IllegalArgumentException("Not a double property");
        }
        return new ReadOnlyJavaBeanDoubleProperty(descriptor, this.helper.getBean());
    }

    public ReadOnlyJavaBeanDoublePropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public ReadOnlyJavaBeanDoublePropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public ReadOnlyJavaBeanDoublePropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public ReadOnlyJavaBeanDoublePropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public ReadOnlyJavaBeanDoublePropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }
}
