package javax.lang.model.element;

import java.util.List;

/* loaded from: rt.jar:javax/lang/model/element/Parameterizable.class */
public interface Parameterizable extends Element {
    List<? extends TypeParameterElement> getTypeParameters();
}
