package javax.lang.model.type;

/* loaded from: rt.jar:javax/lang/model/type/WildcardType.class */
public interface WildcardType extends TypeMirror {
    TypeMirror getExtendsBound();

    TypeMirror getSuperBound();
}
