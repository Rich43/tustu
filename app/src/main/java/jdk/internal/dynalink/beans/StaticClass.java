package jdk.internal.dynalink.beans;

import java.io.Serializable;
import java.util.Objects;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/StaticClass.class */
public class StaticClass implements Serializable {
    private static final ClassValue<StaticClass> staticClasses = new ClassValue<StaticClass>() { // from class: jdk.internal.dynalink.beans.StaticClass.1
        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ StaticClass computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected StaticClass computeValue(Class<?> type) {
            return new StaticClass(type);
        }
    };
    private static final long serialVersionUID = 1;
    private final Class<?> clazz;

    StaticClass(Class<?> clazz) {
        this.clazz = (Class) Objects.requireNonNull(clazz);
    }

    public static StaticClass forClass(Class<?> clazz) {
        return staticClasses.get(clazz);
    }

    public Class<?> getRepresentedClass() {
        return this.clazz;
    }

    public String toString() {
        return "JavaClassStatics[" + this.clazz.getName() + "]";
    }

    private Object readResolve() {
        return forClass(this.clazz);
    }
}
