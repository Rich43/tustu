package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.signature.SignatureReader;
import jdk.internal.org.objectweb.asm.signature.SignatureVisitor;
import jdk.internal.org.objectweb.asm.signature.SignatureWriter;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/Remapper.class */
public abstract class Remapper {
    public String mapDesc(String str) {
        Type type = Type.getType(str);
        switch (type.getSort()) {
            case 9:
                String strMapDesc = mapDesc(type.getElementType().getDescriptor());
                for (int i2 = 0; i2 < type.getDimensions(); i2++) {
                    strMapDesc = '[' + strMapDesc;
                }
                return strMapDesc;
            case 10:
                String map = map(type.getInternalName());
                if (map != null) {
                    return 'L' + map + ';';
                }
                break;
        }
        return str;
    }

    private Type mapType(Type type) {
        switch (type.getSort()) {
            case 9:
                String strMapDesc = mapDesc(type.getElementType().getDescriptor());
                for (int i2 = 0; i2 < type.getDimensions(); i2++) {
                    strMapDesc = '[' + strMapDesc;
                }
                return Type.getType(strMapDesc);
            case 10:
                String map = map(type.getInternalName());
                return map != null ? Type.getObjectType(map) : type;
            case 11:
                return Type.getMethodType(mapMethodDesc(type.getDescriptor()));
            default:
                return type;
        }
    }

    public String mapType(String str) {
        if (str == null) {
            return null;
        }
        return mapType(Type.getObjectType(str)).getInternalName();
    }

    public String[] mapTypes(String[] strArr) {
        String[] strArr2 = null;
        boolean z2 = false;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            String map = map(str);
            if (map != null && strArr2 == null) {
                strArr2 = new String[strArr.length];
                if (i2 > 0) {
                    System.arraycopy(strArr, 0, strArr2, 0, i2);
                }
                z2 = true;
            }
            if (z2) {
                strArr2[i2] = map == null ? str : map;
            }
        }
        return z2 ? strArr2 : strArr;
    }

    public String mapMethodDesc(String str) {
        if ("()V".equals(str)) {
            return str;
        }
        Type[] argumentTypes = Type.getArgumentTypes(str);
        StringBuilder sb = new StringBuilder("(");
        for (Type type : argumentTypes) {
            sb.append(mapDesc(type.getDescriptor()));
        }
        Type returnType = Type.getReturnType(str);
        if (returnType == Type.VOID_TYPE) {
            sb.append(")V");
            return sb.toString();
        }
        sb.append(')').append(mapDesc(returnType.getDescriptor()));
        return sb.toString();
    }

    public Object mapValue(Object obj) {
        if (obj instanceof Type) {
            return mapType((Type) obj);
        }
        if (obj instanceof Handle) {
            Handle handle = (Handle) obj;
            return new Handle(handle.getTag(), mapType(handle.getOwner()), mapMethodName(handle.getOwner(), handle.getName(), handle.getDesc()), mapMethodDesc(handle.getDesc()));
        }
        return obj;
    }

    public String mapSignature(String str, boolean z2) {
        if (str == null) {
            return null;
        }
        SignatureReader signatureReader = new SignatureReader(str);
        SignatureWriter signatureWriter = new SignatureWriter();
        SignatureVisitor signatureVisitorCreateRemappingSignatureAdapter = createRemappingSignatureAdapter(signatureWriter);
        if (z2) {
            signatureReader.acceptType(signatureVisitorCreateRemappingSignatureAdapter);
        } else {
            signatureReader.accept(signatureVisitorCreateRemappingSignatureAdapter);
        }
        return signatureWriter.toString();
    }

    protected SignatureVisitor createRemappingSignatureAdapter(SignatureVisitor signatureVisitor) {
        return new RemappingSignatureAdapter(signatureVisitor, this);
    }

    public String mapMethodName(String str, String str2, String str3) {
        return str2;
    }

    public String mapInvokeDynamicMethodName(String str, String str2) {
        return str;
    }

    public String mapFieldName(String str, String str2, String str3) {
        return str2;
    }

    public String map(String str) {
        return str;
    }
}
