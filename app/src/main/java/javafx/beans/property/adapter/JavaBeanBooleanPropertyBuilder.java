package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanBooleanPropertyBuilder.class */
public final class JavaBeanBooleanPropertyBuilder {
    private final JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

    public static JavaBeanBooleanPropertyBuilder create() {
        return new JavaBeanBooleanPropertyBuilder();
    }

    public JavaBeanBooleanProperty build() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Boolean.TYPE.equals(descriptor.getType()) && !Boolean.class.equals(descriptor.getType())) {
            throw new IllegalArgumentException("Not a boolean property");
        }
        return new JavaBeanBooleanProperty(descriptor, this.helper.getBean());
    }

    public JavaBeanBooleanPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public JavaBeanBooleanPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public JavaBeanBooleanPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public JavaBeanBooleanPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public JavaBeanBooleanPropertyBuilder setter(String setter) {
        this.helper.setterName(setter);
        return this;
    }

    public JavaBeanBooleanPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }

    public JavaBeanBooleanPropertyBuilder setter(Method setter) {
        this.helper.setter(setter);
        return this;
    }
}
