package java.lang.reflect;

/* loaded from: rt.jar:java/lang/reflect/AnnotatedWildcardType.class */
public interface AnnotatedWildcardType extends AnnotatedType {
    AnnotatedType[] getAnnotatedLowerBounds();

    AnnotatedType[] getAnnotatedUpperBounds();
}
