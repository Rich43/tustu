package javax.lang.model.type;

import javax.lang.model.element.Element;

/* loaded from: rt.jar:javax/lang/model/type/TypeVariable.class */
public interface TypeVariable extends ReferenceType {
    Element asElement();

    TypeMirror getUpperBound();

    TypeMirror getLowerBound();
}
