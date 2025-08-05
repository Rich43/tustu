package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanBooleanPropertyBuilder.class */
public final class ReadOnlyJavaBeanBooleanPropertyBuilder {
    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    public static ReadOnlyJavaBeanBooleanPropertyBuilder create() {
        return new ReadOnlyJavaBeanBooleanPropertyBuilder();
    }

    public ReadOnlyJavaBeanBooleanProperty build() throws NoSuchMethodException {
        ReadOnlyPropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!Boolean.TYPE.equals(descriptor.getType()) && !Boolean.class.equals(descriptor.getType())) {
            throw new IllegalArgumentException("Not a boolean property");
        }
        return new ReadOnlyJavaBeanBooleanProperty(descriptor, this.helper.getBean());
    }

    public ReadOnlyJavaBeanBooleanPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public ReadOnlyJavaBeanBooleanPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public ReadOnlyJavaBeanBooleanPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public ReadOnlyJavaBeanBooleanPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public ReadOnlyJavaBeanBooleanPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }
}
