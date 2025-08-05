package javax.lang.model.element;

/* loaded from: rt.jar:javax/lang/model/element/VariableElement.class */
public interface VariableElement extends Element {
    Object getConstantValue();

    @Override // javax.lang.model.element.Element
    Name getSimpleName();

    @Override // javax.lang.model.element.Element
    Element getEnclosingElement();
}
