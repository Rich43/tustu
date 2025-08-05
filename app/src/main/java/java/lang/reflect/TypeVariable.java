package java.lang.reflect;

import java.lang.reflect.GenericDeclaration;

/* loaded from: rt.jar:java/lang/reflect/TypeVariable.class */
public interface TypeVariable<D extends GenericDeclaration> extends Type, AnnotatedElement {
    Type[] getBounds();

    D getGenericDeclaration();

    String getName();

    AnnotatedType[] getAnnotatedBounds();
}
