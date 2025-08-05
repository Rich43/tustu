package javax.lang.model.type;

import java.util.List;

/* loaded from: rt.jar:javax/lang/model/type/IntersectionType.class */
public interface IntersectionType extends TypeMirror {
    List<? extends TypeMirror> getBounds();
}
