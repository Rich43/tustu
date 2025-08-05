package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.type.IntersectionType;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
/* loaded from: rt.jar:javax/lang/model/util/SimpleTypeVisitor8.class */
public class SimpleTypeVisitor8<R, P> extends SimpleTypeVisitor7<R, P> {
    protected SimpleTypeVisitor8() {
        super(null);
    }

    protected SimpleTypeVisitor8(R r2) {
        super(r2);
    }

    @Override // javax.lang.model.util.AbstractTypeVisitor6, javax.lang.model.type.TypeVisitor
    public R visitIntersection(IntersectionType intersectionType, P p2) {
        return defaultAction(intersectionType, p2);
    }
}
