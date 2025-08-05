package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.JavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.PropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/JavaBeanStringPropertyBuilder.class */
public final class JavaBeanStringPropertyBuilder {
    private JavaBeanPropertyBuilderHelper helper = new JavaBeanPropertyBuilderHelper();

    public static JavaBeanStringPropertyBuilder create() {
        return new JavaBeanStringPropertyBuilder();
    }

    public JavaBeanStringProperty build() throws NoSuchMethodException, SecurityException {
        PropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!String.class.equals(descriptor.getType())) {
            throw new IllegalArgumentException("Not a String property");
        }
        return new JavaBeanStringProperty(descriptor, this.helper.getBean());
    }

    public JavaBeanStringPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public JavaBeanStringPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public JavaBeanStringPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public JavaBeanStringPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public JavaBeanStringPropertyBuilder setter(String setter) {
        this.helper.setterName(setter);
        return this;
    }

    public JavaBeanStringPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }

    public JavaBeanStringPropertyBuilder setter(Method setter) {
        this.helper.setter(setter);
        return this;
    }
}
