package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanFloatPropertyBuilder.class */
public final class JavaBeanFloatPropertyBuilder {
    private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

    public static JavaBeanFloatPropertyBuilder create() {
        return new JavaBeanFloatPropertyBuilder();
    }

    public JavaBeanFloatProperty build() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Float.TYPE.equals(descriptor.getType()) && !Number.class.isAssignableFrom(descriptor.getType())) {
            throw new IllegalArgumentException("Not a float property");
        }
        return new JavaBeanFloatProperty(descriptor, this.helper.getBean());
    }

    public JavaBeanFloatPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public JavaBeanFloatPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public JavaBeanFloatPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public JavaBeanFloatPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public JavaBeanFloatPropertyBuilder setter(String setter) {
        this.helper.setterName(setter);
        return this;
    }

    public JavaBeanFloatPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }

    public JavaBeanFloatPropertyBuilder setter(Method setter) {
        this.helper.setter(setter);
        return this;
    }
}
