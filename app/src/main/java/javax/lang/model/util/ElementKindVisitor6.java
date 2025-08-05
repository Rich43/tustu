package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
/* loaded from: rt.jar:javax/lang/model/util/ElementKindVisitor6.class */
public class ElementKindVisitor6<R, P> extends SimpleElementVisitor6<R, P> {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ElementKindVisitor6.class.desiredAssertionStatus();
    }

    protected ElementKindVisitor6() {
        super(null);
    }

    protected ElementKindVisitor6(R r2) {
        super(r2);
    }

    @Override // javax.lang.model.util.SimpleElementVisitor6, javax.lang.model.element.ElementVisitor
    public R visitPackage(PackageElement packageElement, P p2) {
        if ($assertionsDisabled || packageElement.getKind() == ElementKind.PACKAGE) {
            return defaultAction(packageElement, p2);
        }
        throw new AssertionError((Object) "Bad kind on PackageElement");
    }

    @Override // javax.lang.model.util.SimpleElementVisitor6, javax.lang.model.element.ElementVisitor
    public R visitType(TypeElement typeElement, P p2) {
        ElementKind kind = typeElement.getKind();
        switch (kind) {
            case ANNOTATION_TYPE:
                return visitTypeAsAnnotationType(typeElement, p2);
            case CLASS:
                return visitTypeAsClass(typeElement, p2);
            case ENUM:
                return visitTypeAsEnum(typeElement, p2);
            case INTERFACE:
                return visitTypeAsInterface(typeElement, p2);
            default:
                throw new AssertionError((Object) ("Bad kind " + ((Object) kind) + " for TypeElement" + ((Object) typeElement)));
        }
    }

    public R visitTypeAsAnnotationType(TypeElement typeElement, P p2) {
        return defaultAction(typeElement, p2);
    }

    public R visitTypeAsClass(TypeElement typeElement, P p2) {
        return defaultAction(typeElement, p2);
    }

    public R visitTypeAsEnum(TypeElement typeElement, P p2) {
        return defaultAction(typeElement, p2);
    }

    public R visitTypeAsInterface(TypeElement typeElement, P p2) {
        return defaultAction(typeElement, p2);
    }

    @Override // javax.lang.model.util.SimpleElementVisitor6, javax.lang.model.element.ElementVisitor
    public R visitVariable(VariableElement variableElement, P p2) {
        ElementKind kind = variableElement.getKind();
        switch (kind) {
            case ENUM_CONSTANT:
                return visitVariableAsEnumConstant(variableElement, p2);
            case EXCEPTION_PARAMETER:
                return visitVariableAsExceptionParameter(variableElement, p2);
            case FIELD:
                return visitVariableAsField(variableElement, p2);
            case LOCAL_VARIABLE:
                return visitVariableAsLocalVariable(variableElement, p2);
            case PARAMETER:
                return visitVariableAsParameter(variableElement, p2);
            case RESOURCE_VARIABLE:
                return visitVariableAsResourceVariable(variableElement, p2);
            default:
                throw new AssertionError((Object) ("Bad kind " + ((Object) kind) + " for VariableElement" + ((Object) variableElement)));
        }
    }

    public R visitVariableAsEnumConstant(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }

    public R visitVariableAsExceptionParameter(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }

    public R visitVariableAsField(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }

    public R visitVariableAsLocalVariable(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }

    public R visitVariableAsParameter(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }

    public R visitVariableAsResourceVariable(VariableElement variableElement, P p2) {
        return visitUnknown(variableElement, p2);
    }

    @Override // javax.lang.model.util.SimpleElementVisitor6, javax.lang.model.element.ElementVisitor
    public R visitExecutable(ExecutableElement executableElement, P p2) {
        ElementKind kind = executableElement.getKind();
        switch (kind) {
            case CONSTRUCTOR:
                return visitExecutableAsConstructor(executableElement, p2);
            case INSTANCE_INIT:
                return visitExecutableAsInstanceInit(executableElement, p2);
            case METHOD:
                return visitExecutableAsMethod(executableElement, p2);
            case STATIC_INIT:
                return visitExecutableAsStaticInit(executableElement, p2);
            default:
                throw new AssertionError((Object) ("Bad kind " + ((Object) kind) + " for ExecutableElement" + ((Object) executableElement)));
        }
    }

    public R visitExecutableAsConstructor(ExecutableElement executableElement, P p2) {
        return defaultAction(executableElement, p2);
    }

    public R visitExecutableAsInstanceInit(ExecutableElement executableElement, P p2) {
        return defaultAction(executableElement, p2);
    }

    public R visitExecutableAsMethod(ExecutableElement executableElement, P p2) {
        return defaultAction(executableElement, p2);
    }

    public R visitExecutableAsStaticInit(ExecutableElement executableElement, P p2) {
        return defaultAction(executableElement, p2);
    }

    @Override // javax.lang.model.util.SimpleElementVisitor6, javax.lang.model.element.ElementVisitor
    public R visitTypeParameter(TypeParameterElement typeParameterElement, P p2) {
        if ($assertionsDisabled || typeParameterElement.getKind() == ElementKind.TYPE_PARAMETER) {
            return defaultAction(typeParameterElement, p2);
        }
        throw new AssertionError((Object) "Bad kind on TypeParameterElement");
    }
}
