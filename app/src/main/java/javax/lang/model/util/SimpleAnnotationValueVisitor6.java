package javax.lang.model.util;

import java.util.List;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
/* loaded from: rt.jar:javax/lang/model/util/SimpleAnnotationValueVisitor6.class */
public class SimpleAnnotationValueVisitor6<R, P> extends AbstractAnnotationValueVisitor6<R, P> {
    protected final R DEFAULT_VALUE;

    protected SimpleAnnotationValueVisitor6() {
        this.DEFAULT_VALUE = null;
    }

    protected SimpleAnnotationValueVisitor6(R r2) {
        this.DEFAULT_VALUE = r2;
    }

    protected R defaultAction(Object obj, P p2) {
        return this.DEFAULT_VALUE;
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitBoolean(boolean z2, P p2) {
        return defaultAction(Boolean.valueOf(z2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitByte(byte b2, P p2) {
        return defaultAction(Byte.valueOf(b2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitChar(char c2, P p2) {
        return defaultAction(Character.valueOf(c2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitDouble(double d2, P p2) {
        return defaultAction(Double.valueOf(d2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitFloat(float f2, P p2) {
        return defaultAction(Float.valueOf(f2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitInt(int i2, P p2) {
        return defaultAction(Integer.valueOf(i2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitLong(long j2, P p2) {
        return defaultAction(Long.valueOf(j2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitShort(short s2, P p2) {
        return defaultAction(Short.valueOf(s2), p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitString(String str, P p2) {
        return defaultAction(str, p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitType(TypeMirror typeMirror, P p2) {
        return defaultAction(typeMirror, p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitEnumConstant(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitAnnotation(AnnotationMirror annotationMirror, P p2) {
        return defaultAction(annotationMirror, p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitArray(List<? extends AnnotationValue> list, P p2) {
        return defaultAction(list, p2);
    }
}
