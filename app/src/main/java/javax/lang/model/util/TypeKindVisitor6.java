package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.type.NoType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
/* loaded from: rt.jar:javax/lang/model/util/TypeKindVisitor6.class */
public class TypeKindVisitor6<R, P> extends SimpleTypeVisitor6<R, P> {
    protected TypeKindVisitor6() {
        super(null);
    }

    protected TypeKindVisitor6(R r2) {
        super(r2);
    }

    @Override // javax.lang.model.util.SimpleTypeVisitor6, javax.lang.model.type.TypeVisitor
    public R visitPrimitive(PrimitiveType primitiveType, P p2) {
        TypeKind kind = primitiveType.getKind();
        switch (kind) {
            case BOOLEAN:
                return visitPrimitiveAsBoolean(primitiveType, p2);
            case BYTE:
                return visitPrimitiveAsByte(primitiveType, p2);
            case SHORT:
                return visitPrimitiveAsShort(primitiveType, p2);
            case INT:
                return visitPrimitiveAsInt(primitiveType, p2);
            case LONG:
                return visitPrimitiveAsLong(primitiveType, p2);
            case CHAR:
                return visitPrimitiveAsChar(primitiveType, p2);
            case FLOAT:
                return visitPrimitiveAsFloat(primitiveType, p2);
            case DOUBLE:
                return visitPrimitiveAsDouble(primitiveType, p2);
            default:
                throw new AssertionError((Object) ("Bad kind " + ((Object) kind) + " for PrimitiveType" + ((Object) primitiveType)));
        }
    }

    public R visitPrimitiveAsBoolean(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    public R visitPrimitiveAsByte(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    public R visitPrimitiveAsShort(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    public R visitPrimitiveAsInt(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    public R visitPrimitiveAsLong(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    public R visitPrimitiveAsChar(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    public R visitPrimitiveAsFloat(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    public R visitPrimitiveAsDouble(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    @Override // javax.lang.model.util.SimpleTypeVisitor6, javax.lang.model.type.TypeVisitor
    public R visitNoType(NoType noType, P p2) {
        TypeKind kind = noType.getKind();
        switch (kind) {
            case VOID:
                return visitNoTypeAsVoid(noType, p2);
            case PACKAGE:
                return visitNoTypeAsPackage(noType, p2);
            case NONE:
                return visitNoTypeAsNone(noType, p2);
            default:
                throw new AssertionError((Object) ("Bad kind " + ((Object) kind) + " for NoType" + ((Object) noType)));
        }
    }

    public R visitNoTypeAsVoid(NoType noType, P p2) {
        return defaultAction(noType, p2);
    }

    public R visitNoTypeAsPackage(NoType noType, P p2) {
        return defaultAction(noType, p2);
    }

    public R visitNoTypeAsNone(NoType noType, P p2) {
        return defaultAction(noType, p2);
    }
}
