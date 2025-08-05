package java.lang.reflect;

/* loaded from: rt.jar:java/lang/reflect/WildcardType.class */
public interface WildcardType extends Type {
    Type[] getUpperBounds();

    Type[] getLowerBounds();
}
