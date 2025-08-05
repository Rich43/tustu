package java.lang.reflect;

/* loaded from: rt.jar:java/lang/reflect/ParameterizedType.class */
public interface ParameterizedType extends Type {
    Type[] getActualTypeArguments();

    Type getRawType();

    Type getOwnerType();
}
