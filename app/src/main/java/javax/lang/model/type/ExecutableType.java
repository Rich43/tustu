package javax.lang.model.type;

import java.util.List;

/* loaded from: rt.jar:javax/lang/model/type/ExecutableType.class */
public interface ExecutableType extends TypeMirror {
    List<? extends TypeVariable> getTypeVariables();

    TypeMirror getReturnType();

    List<? extends TypeMirror> getParameterTypes();

    TypeMirror getReceiverType();

    List<? extends TypeMirror> getThrownTypes();
}
