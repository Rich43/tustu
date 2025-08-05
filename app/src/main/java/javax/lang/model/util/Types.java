package javax.lang.model.util;

import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;

/* loaded from: rt.jar:javax/lang/model/util/Types.class */
public interface Types {
    Element asElement(TypeMirror typeMirror);

    boolean isSameType(TypeMirror typeMirror, TypeMirror typeMirror2);

    boolean isSubtype(TypeMirror typeMirror, TypeMirror typeMirror2);

    boolean isAssignable(TypeMirror typeMirror, TypeMirror typeMirror2);

    boolean contains(TypeMirror typeMirror, TypeMirror typeMirror2);

    boolean isSubsignature(ExecutableType executableType, ExecutableType executableType2);

    List<? extends TypeMirror> directSupertypes(TypeMirror typeMirror);

    TypeMirror erasure(TypeMirror typeMirror);

    TypeElement boxedClass(PrimitiveType primitiveType);

    PrimitiveType unboxedType(TypeMirror typeMirror);

    TypeMirror capture(TypeMirror typeMirror);

    PrimitiveType getPrimitiveType(TypeKind typeKind);

    NullType getNullType();

    NoType getNoType(TypeKind typeKind);

    ArrayType getArrayType(TypeMirror typeMirror);

    WildcardType getWildcardType(TypeMirror typeMirror, TypeMirror typeMirror2);

    DeclaredType getDeclaredType(TypeElement typeElement, TypeMirror... typeMirrorArr);

    DeclaredType getDeclaredType(DeclaredType declaredType, TypeElement typeElement, TypeMirror... typeMirrorArr);

    TypeMirror asMemberOf(DeclaredType declaredType, Element element);
}
