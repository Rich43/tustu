package javax.lang.model.type;

import java.util.List;
import javax.lang.model.element.Element;

/* loaded from: rt.jar:javax/lang/model/type/DeclaredType.class */
public interface DeclaredType extends ReferenceType {
    Element asElement();

    TypeMirror getEnclosingType();

    List<? extends TypeMirror> getTypeArguments();
}
