package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanObjectPropertyBuilder.class */
public final class JavaBeanObjectPropertyBuilder<T> {
    private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

    public static JavaBeanObjectPropertyBuilder create() {
        return new JavaBeanObjectPropertyBuilder();
    }

    public JavaBeanObjectProperty<T> build() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor descriptor = this.helper.getDescriptor();
        return new JavaBeanObjectProperty<>(descriptor, this.helper.getBean());
    }

    public JavaBeanObjectPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public JavaBeanObjectPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public JavaBeanObjectPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public JavaBeanObjectPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public JavaBeanObjectPropertyBuilder setter(String setter) {
        this.helper.setterName(setter);
        return this;
    }

    public JavaBeanObjectPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }

    public JavaBeanObjectPropertyBuilder setter(Method setter) {
        this.helper.setter(setter);
        return this;
    }
}
