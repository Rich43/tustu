package javax.lang.model.element;

import java.util.List;

/* loaded from: rt.jar:javax/lang/model/element/PackageElement.class */
public interface PackageElement extends Element, QualifiedNameable {
    @Override // javax.lang.model.element.QualifiedNameable
    Name getQualifiedName();

    @Override // javax.lang.model.element.Element
    Name getSimpleName();

    @Override // javax.lang.model.element.Element
    List<? extends Element> getEnclosedElements();

    boolean isUnnamed();

    @Override // javax.lang.model.element.Element
    Element getEnclosingElement();
}
