package javax.lang.model.element;

import java.util.List;
import javax.lang.model.type.TypeMirror;

/* loaded from: rt.jar:javax/lang/model/element/ExecutableElement.class */
public interface ExecutableElement extends Element, Parameterizable {
    @Override // javax.lang.model.element.Parameterizable
    List<? extends TypeParameterElement> getTypeParameters();

    TypeMirror getReturnType();

    List<? extends VariableElement> getParameters();

    TypeMirror getReceiverType();

    boolean isVarArgs();

    boolean isDefault();

    List<? extends TypeMirror> getThrownTypes();

    AnnotationValue getDefaultValue();

    @Override // javax.lang.model.element.Element
    Name getSimpleName();
}
