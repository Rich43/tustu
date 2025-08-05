package javax.lang.model.util;

import java.io.Writer;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/* loaded from: rt.jar:javax/lang/model/util/Elements.class */
public interface Elements {
    PackageElement getPackageElement(CharSequence charSequence);

    TypeElement getTypeElement(CharSequence charSequence);

    Map<? extends ExecutableElement, ? extends AnnotationValue> getElementValuesWithDefaults(AnnotationMirror annotationMirror);

    String getDocComment(Element element);

    boolean isDeprecated(Element element);

    Name getBinaryName(TypeElement typeElement);

    PackageElement getPackageOf(Element element);

    List<? extends Element> getAllMembers(TypeElement typeElement);

    List<? extends AnnotationMirror> getAllAnnotationMirrors(Element element);

    boolean hides(Element element, Element element2);

    boolean overrides(ExecutableElement executableElement, ExecutableElement executableElement2, TypeElement typeElement);

    String getConstantExpression(Object obj);

    void printElements(Writer writer, Element... elementArr);

    Name getName(CharSequence charSequence);

    boolean isFunctionalInterface(TypeElement typeElement);
}
