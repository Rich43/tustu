package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanIntegerPropertyBuilder.class */
public final class JavaBeanIntegerPropertyBuilder {
    private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

    public static JavaBeanIntegerPropertyBuilder create() {
        return new JavaBeanIntegerPropertyBuilder();
    }

    public JavaBeanIntegerProperty build() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Integer.TYPE.equals(descriptor.getType()) && !Number.class.isAssignableFrom(descriptor.getType())) {
            throw new IllegalArgumentException("Not an int property");
        }
        return new JavaBeanIntegerProperty(descriptor, this.helper.getBean());
    }

    public JavaBeanIntegerPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public JavaBeanIntegerPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public JavaBeanIntegerPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public JavaBeanIntegerPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public JavaBeanIntegerPropertyBuilder setter(String setter) {
        this.helper.setterName(setter);
        return this;
    }

    public JavaBeanIntegerPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }

    public JavaBeanIntegerPropertyBuilder setter(Method setter) {
        this.helper.setter(setter);
        return this;
    }
}
