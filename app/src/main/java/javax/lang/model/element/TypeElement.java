package javax.lang.model.element;

import java.util.List;
import javax.lang.model.type.TypeMirror;

/* loaded from: rt.jar:javax/lang/model/element/TypeElement.class */
public interface TypeElement extends Element, Parameterizable, QualifiedNameable {
    @Override // javax.lang.model.element.Element
    List<? extends Element> getEnclosedElements();

    NestingKind getNestingKind();

    @Override // javax.lang.model.element.QualifiedNameable
    Name getQualifiedName();

    @Override // javax.lang.model.element.Element
    Name getSimpleName();

    TypeMirror getSuperclass();

    List<? extends TypeMirror> getInterfaces();

    @Override // javax.lang.model.element.Parameterizable
    List<? extends TypeParameterElement> getTypeParameters();

    @Override // javax.lang.model.element.Element
    Element getEnclosingElement();
}
