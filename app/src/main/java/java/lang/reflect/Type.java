package java.lang.reflect;

/* loaded from: rt.jar:java/lang/reflect/Type.class */
public interface Type {
    default String getTypeName() {
        return toString();
    }
}
