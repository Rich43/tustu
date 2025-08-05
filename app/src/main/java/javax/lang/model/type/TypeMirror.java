package javax.lang.model.type;

import javax.lang.model.AnnotatedConstruct;

/* loaded from: rt.jar:javax/lang/model/type/TypeMirror.class */
public interface TypeMirror extends AnnotatedConstruct {
    TypeKind getKind();

    boolean equals(Object obj);

    int hashCode();

    String toString();

    <R, P> R accept(TypeVisitor<R, P> typeVisitor, P p2);
}
