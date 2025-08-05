package java.lang.invoke;

import java.lang.invoke.MethodHandles;
import sun.invoke.util.Wrapper;

/* loaded from: rt.jar:java/lang/invoke/AbstractValidatingLambdaMetafactory.class */
abstract class AbstractValidatingLambdaMetafactory {
    final Class<?> targetClass;
    final MethodType invokedType;
    final Class<?> samBase;
    final String samMethodName;
    final MethodType samMethodType;
    final MethodHandle implMethod;
    final MethodHandleInfo implInfo;
    final int implKind;
    final boolean implIsInstanceMethod;
    final Class<?> implDefiningClass;
    final MethodType implMethodType;
    final MethodType instantiatedMethodType;
    final boolean isSerializable;
    final Class<?>[] markerInterfaces;
    final MethodType[] additionalBridges;

    abstract CallSite buildCallSite() throws LambdaConversionException;

    AbstractValidatingLambdaMetafactory(MethodHandles.Lookup lookup, MethodType methodType, String str, MethodType methodType2, MethodHandle methodHandle, MethodType methodType3, boolean z2, Class<?>[] clsArr, MethodType[] methodTypeArr) throws LambdaConversionException {
        if ((lookup.lookupModes() & 2) == 0) {
            throw new LambdaConversionException(String.format("Invalid caller: %s", lookup.lookupClass().getName()));
        }
        this.targetClass = lookup.lookupClass();
        this.invokedType = methodType;
        this.samBase = methodType.returnType();
        this.samMethodName = str;
        this.samMethodType = methodType2;
        this.implMethod = methodHandle;
        this.implInfo = lookup.revealDirect(methodHandle);
        this.implKind = this.implInfo.getReferenceKind();
        this.implIsInstanceMethod = this.implKind == 5 || this.implKind == 7 || this.implKind == 9;
        this.implDefiningClass = this.implInfo.getDeclaringClass();
        this.implMethodType = this.implInfo.getMethodType();
        this.instantiatedMethodType = methodType3;
        this.isSerializable = z2;
        this.markerInterfaces = clsArr;
        this.additionalBridges = methodTypeArr;
        if (!this.samBase.isInterface()) {
            throw new LambdaConversionException(String.format("Functional interface %s is not an interface", this.samBase.getName()));
        }
        for (Class<?> cls : clsArr) {
            if (!cls.isInterface()) {
                throw new LambdaConversionException(String.format("Marker interface %s is not an interface", cls.getName()));
            }
        }
    }

    void validateMetafactoryArgs() throws LambdaConversionException {
        int i2;
        int i3;
        Class<?> clsParameterType;
        switch (this.implKind) {
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                int iParameterCount = this.implMethodType.parameterCount();
                int i4 = this.implIsInstanceMethod ? 1 : 0;
                int iParameterCount2 = this.invokedType.parameterCount();
                int iParameterCount3 = this.samMethodType.parameterCount();
                int iParameterCount4 = this.instantiatedMethodType.parameterCount();
                if (iParameterCount + i4 != iParameterCount2 + iParameterCount3) {
                    Object[] objArr = new Object[5];
                    objArr[0] = this.implIsInstanceMethod ? "instance" : "static";
                    objArr[1] = this.implInfo;
                    objArr[2] = Integer.valueOf(iParameterCount2);
                    objArr[3] = Integer.valueOf(iParameterCount3);
                    objArr[4] = Integer.valueOf(iParameterCount);
                    throw new LambdaConversionException(String.format("Incorrect number of parameters for %s method %s; %d captured parameters, %d functional interface method parameters, %d implementation parameters", objArr));
                }
                if (iParameterCount4 != iParameterCount3) {
                    Object[] objArr2 = new Object[4];
                    objArr2[0] = this.implIsInstanceMethod ? "instance" : "static";
                    objArr2[1] = this.implInfo;
                    objArr2[2] = Integer.valueOf(iParameterCount4);
                    objArr2[3] = Integer.valueOf(iParameterCount3);
                    throw new LambdaConversionException(String.format("Incorrect number of parameters for %s method %s; %d instantiated parameters, %d functional interface method parameters", objArr2));
                }
                for (MethodType methodType : this.additionalBridges) {
                    if (methodType.parameterCount() != iParameterCount3) {
                        throw new LambdaConversionException(String.format("Incorrect number of parameters for bridge signature %s; incompatible with %s", methodType, this.samMethodType));
                    }
                }
                if (this.implIsInstanceMethod) {
                    if (iParameterCount2 == 0) {
                        i2 = 0;
                        i3 = 1;
                        clsParameterType = this.instantiatedMethodType.parameterType(0);
                    } else {
                        i2 = 1;
                        i3 = 0;
                        clsParameterType = this.invokedType.parameterType(0);
                    }
                    if (!this.implDefiningClass.isAssignableFrom(clsParameterType)) {
                        throw new LambdaConversionException(String.format("Invalid receiver type %s; not a subtype of implementation type %s", clsParameterType, this.implDefiningClass));
                    }
                    Class<?> clsParameterType2 = this.implMethod.type().parameterType(0);
                    if (clsParameterType2 != this.implDefiningClass && !clsParameterType2.isAssignableFrom(clsParameterType)) {
                        throw new LambdaConversionException(String.format("Invalid receiver type %s; not a subtype of implementation receiver type %s", clsParameterType, clsParameterType2));
                    }
                } else {
                    i2 = 0;
                    i3 = 0;
                }
                int i5 = iParameterCount2 - i2;
                for (int i6 = 0; i6 < i5; i6++) {
                    Class<?> clsParameterType3 = this.implMethodType.parameterType(i6);
                    Class<?> clsParameterType4 = this.invokedType.parameterType(i6 + i2);
                    if (!clsParameterType4.equals(clsParameterType3)) {
                        throw new LambdaConversionException(String.format("Type mismatch in captured lambda parameter %d: expecting %s, found %s", Integer.valueOf(i6), clsParameterType4, clsParameterType3));
                    }
                }
                int i7 = i3 - i5;
                for (int i8 = i5; i8 < iParameterCount; i8++) {
                    Class<?> clsParameterType5 = this.implMethodType.parameterType(i8);
                    Class<?> clsParameterType6 = this.instantiatedMethodType.parameterType(i8 + i7);
                    if (!isAdaptableTo(clsParameterType6, clsParameterType5, true)) {
                        throw new LambdaConversionException(String.format("Type mismatch for lambda argument %d: %s is not convertible to %s", Integer.valueOf(i8), clsParameterType6, clsParameterType5));
                    }
                }
                Class<?> clsReturnType = this.instantiatedMethodType.returnType();
                Class<?> clsReturnType2 = this.implKind == 8 ? this.implDefiningClass : this.implMethodType.returnType();
                Class<?> clsReturnType3 = this.samMethodType.returnType();
                if (!isAdaptableToAsReturn(clsReturnType2, clsReturnType)) {
                    throw new LambdaConversionException(String.format("Type mismatch for lambda return: %s is not convertible to %s", clsReturnType2, clsReturnType));
                }
                if (!isAdaptableToAsReturnStrict(clsReturnType, clsReturnType3)) {
                    throw new LambdaConversionException(String.format("Type mismatch for lambda expected return: %s is not convertible to %s", clsReturnType, clsReturnType3));
                }
                for (MethodType methodType2 : this.additionalBridges) {
                    if (!isAdaptableToAsReturnStrict(clsReturnType, methodType2.returnType())) {
                        throw new LambdaConversionException(String.format("Type mismatch for lambda expected return: %s is not convertible to %s", clsReturnType, methodType2.returnType()));
                    }
                }
                return;
            default:
                throw new LambdaConversionException(String.format("Unsupported MethodHandle kind: %s", this.implInfo));
        }
    }

    private boolean isAdaptableTo(Class<?> cls, Class<?> cls2, boolean z2) {
        if (cls.equals(cls2)) {
            return true;
        }
        if (cls.isPrimitive()) {
            Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
            if (cls2.isPrimitive()) {
                return Wrapper.forPrimitiveType(cls2).isConvertibleFrom(wrapperForPrimitiveType);
            }
            return cls2.isAssignableFrom(wrapperForPrimitiveType.wrapperType());
        }
        if (!cls2.isPrimitive()) {
            return !z2 || cls2.isAssignableFrom(cls);
        }
        if (Wrapper.isWrapperType(cls)) {
            Wrapper wrapperForWrapperType = Wrapper.forWrapperType(cls);
            if (wrapperForWrapperType.primitiveType().isPrimitive()) {
                return Wrapper.forPrimitiveType(cls2).isConvertibleFrom(wrapperForWrapperType);
            }
        }
        return !z2;
    }

    private boolean isAdaptableToAsReturn(Class<?> cls, Class<?> cls2) {
        return cls2.equals(Void.TYPE) || (!cls.equals(Void.TYPE) && isAdaptableTo(cls, cls2, false));
    }

    private boolean isAdaptableToAsReturnStrict(Class<?> cls, Class<?> cls2) {
        return cls.equals(Void.TYPE) ? cls2.equals(Void.TYPE) : isAdaptableTo(cls, cls2, true);
    }
}
