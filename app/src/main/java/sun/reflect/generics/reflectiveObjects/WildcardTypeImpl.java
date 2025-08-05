package sun.reflect.generics.reflectiveObjects;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.FieldTypeSignature;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/reflectiveObjects/WildcardTypeImpl.class */
public class WildcardTypeImpl extends LazyReflectiveObjectGenerator implements WildcardType {
    private Type[] upperBounds;
    private Type[] lowerBounds;
    private FieldTypeSignature[] upperBoundASTs;
    private FieldTypeSignature[] lowerBoundASTs;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WildcardTypeImpl.class.desiredAssertionStatus();
    }

    private WildcardTypeImpl(FieldTypeSignature[] fieldTypeSignatureArr, FieldTypeSignature[] fieldTypeSignatureArr2, GenericsFactory genericsFactory) {
        super(genericsFactory);
        this.upperBoundASTs = fieldTypeSignatureArr;
        this.lowerBoundASTs = fieldTypeSignatureArr2;
    }

    public static WildcardTypeImpl make(FieldTypeSignature[] fieldTypeSignatureArr, FieldTypeSignature[] fieldTypeSignatureArr2, GenericsFactory genericsFactory) {
        return new WildcardTypeImpl(fieldTypeSignatureArr, fieldTypeSignatureArr2, genericsFactory);
    }

    private FieldTypeSignature[] getUpperBoundASTs() {
        if ($assertionsDisabled || this.upperBounds == null) {
            return this.upperBoundASTs;
        }
        throw new AssertionError();
    }

    private FieldTypeSignature[] getLowerBoundASTs() {
        if ($assertionsDisabled || this.lowerBounds == null) {
            return this.lowerBoundASTs;
        }
        throw new AssertionError();
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getUpperBounds() {
        if (this.upperBounds == null) {
            FieldTypeSignature[] upperBoundASTs = getUpperBoundASTs();
            Type[] typeArr = new Type[upperBoundASTs.length];
            for (int i2 = 0; i2 < upperBoundASTs.length; i2++) {
                Reifier reifier = getReifier();
                upperBoundASTs[i2].accept(reifier);
                typeArr[i2] = reifier.getResult();
            }
            this.upperBounds = typeArr;
        }
        return (Type[]) this.upperBounds.clone();
    }

    @Override // java.lang.reflect.WildcardType
    public Type[] getLowerBounds() {
        if (this.lowerBounds == null) {
            FieldTypeSignature[] lowerBoundASTs = getLowerBoundASTs();
            Type[] typeArr = new Type[lowerBoundASTs.length];
            for (int i2 = 0; i2 < lowerBoundASTs.length; i2++) {
                Reifier reifier = getReifier();
                lowerBoundASTs[i2].accept(reifier);
                typeArr[i2] = reifier.getResult();
            }
            this.lowerBounds = typeArr;
        }
        return (Type[]) this.lowerBounds.clone();
    }

    public String toString() {
        Type[] lowerBounds = getLowerBounds();
        Type[] typeArr = lowerBounds;
        StringBuilder sb = new StringBuilder();
        if (lowerBounds.length > 0) {
            sb.append("? super ");
        } else {
            Type[] upperBounds = getUpperBounds();
            if (upperBounds.length > 0 && !upperBounds[0].equals(Object.class)) {
                typeArr = upperBounds;
                sb.append("? extends ");
            } else {
                return "?";
            }
        }
        if (!$assertionsDisabled && typeArr.length <= 0) {
            throw new AssertionError();
        }
        boolean z2 = true;
        for (Type type : typeArr) {
            if (!z2) {
                sb.append(" & ");
            }
            z2 = false;
            sb.append(type.getTypeName());
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) obj;
            return Arrays.equals(getLowerBounds(), wildcardType.getLowerBounds()) && Arrays.equals(getUpperBounds(), wildcardType.getUpperBounds());
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(getLowerBounds()) ^ Arrays.hashCode(getUpperBounds());
    }
}
