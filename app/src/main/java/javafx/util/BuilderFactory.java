package javafx.util;

@FunctionalInterface
/* loaded from: jfxrt.jar:javafx/util/BuilderFactory.class */
public interface BuilderFactory {
    Builder<?> getBuilder(Class<?> cls);
}
