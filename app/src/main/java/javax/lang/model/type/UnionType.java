package javax.lang.model.type;

import java.util.List;

/* loaded from: rt.jar:javax/lang/model/type/UnionType.class */
public interface UnionType extends TypeMirror {
    List<? extends TypeMirror> getAlternatives();
}
