package javax.lang.model.element;

import java.util.List;
import javax.lang.model.type.TypeMirror;

/* loaded from: rt.jar:javax/lang/model/element/TypeParameterElement.class */
public interface TypeParameterElement extends Element {
    Element getGenericElement();

    List<? extends TypeMirror> getBounds();

    @Override // javax.lang.model.element.Element
    Element getEnclosingElement();
}
