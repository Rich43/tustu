package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanLongPropertyBuilder.class */
public final class JavaBeanLongPropertyBuilder {
    private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

    public static JavaBeanLongPropertyBuilder create() {
        return new JavaBeanLongPropertyBuilder();
    }

    public JavaBeanLongProperty build() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Long.TYPE.equals(descriptor.getType()) && !Number.class.isAssignableFrom(descriptor.getType())) {
            throw new IllegalArgumentException("Not a long property");
        }
        return new JavaBeanLongProperty(descriptor, this.helper.getBean());
    }

    public JavaBeanLongPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public JavaBeanLongPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public JavaBeanLongPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public JavaBeanLongPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public JavaBeanLongPropertyBuilder setter(String setter) {
        this.helper.setterName(setter);
        return this;
    }

    public JavaBeanLongPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }

    public JavaBeanLongPropertyBuilder setter(Method setter) {
        this.helper.setter(setter);
        return this;
    }
}
