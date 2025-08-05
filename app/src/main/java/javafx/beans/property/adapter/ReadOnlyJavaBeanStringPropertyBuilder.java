package javafx.beans.property.adapter;

import com.sun.javafx.property.adapter.ReadOnlyJavaBeanPropertyBuilderHelper;
import com.sun.javafx.property.adapter.ReadOnlyPropertyDescriptor;
import java.lang.reflect.Method;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanStringPropertyBuilder.class */
public final class ReadOnlyJavaBeanStringPropertyBuilder {
    private final ReadOnlyJavaBeanPropertyBuilderHelper helper = new ReadOnlyJavaBeanPropertyBuilderHelper();

    public static ReadOnlyJavaBeanStringPropertyBuilder create() {
        return new ReadOnlyJavaBeanStringPropertyBuilder();
    }

    public ReadOnlyJavaBeanStringProperty build() throws NoSuchMethodException {
        ReadOnlyPropertyDescriptor descriptor = this.helper.getDescriptor();
        if (!String.class.equals(descriptor.getType())) {
            throw new IllegalArgumentException("Not a String property");
        }
        return new ReadOnlyJavaBeanStringProperty(descriptor, this.helper.getBean());
    }

    public ReadOnlyJavaBeanStringPropertyBuilder name(String name) {
        this.helper.name(name);
        return this;
    }

    public ReadOnlyJavaBeanStringPropertyBuilder bean(Object bean) {
        this.helper.bean(bean);
        return this;
    }

    public ReadOnlyJavaBeanStringPropertyBuilder beanClass(Class<?> beanClass) {
        this.helper.beanClass(beanClass);
        return this;
    }

    public ReadOnlyJavaBeanStringPropertyBuilder getter(String getter) {
        this.helper.getterName(getter);
        return this;
    }

    public ReadOnlyJavaBeanStringPropertyBuilder getter(Method getter) {
        this.helper.getter(getter);
        return this;
    }
}
