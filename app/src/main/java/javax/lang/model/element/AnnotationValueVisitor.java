package javax.lang.model.element;

import java.util.List;
import javax.lang.model.type.TypeMirror;

/* loaded from: rt.jar:javax/lang/model/element/AnnotationValueVisitor.class */
public interface AnnotationValueVisitor<R, P> {
    R visit(AnnotationValue annotationValue, P p2);

    R visit(AnnotationValue annotationValue);

    R visitBoolean(boolean z2, P p2);

    R visitByte(byte b2, P p2);

    R visitChar(char c2, P p2);

    R visitDouble(double d2, P p2);

    R visitFloat(float f2, P p2);

    R visitInt(int i2, P p2);

    R visitLong(long j2, P p2);

    R visitShort(short s2, P p2);

    R visitString(String str, P p2);

    R visitType(TypeMirror typeMirror, P p2);

    R visitEnumConstant(VariableElement variableElement, P p2);

    R visitAnnotation(AnnotationMirror annotationMirror, P p2);

    R visitArray(List<? extends AnnotationValue> list, P p2);

    R visitUnknown(AnnotationValue annotationValue, P p2);
}
