package com.sun.jmx.mbeanserver;

import java.io.InvalidObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.management.Descriptor;
import javax.management.MBeanException;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import sun.reflect.misc.MethodUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/ConvertingMethod.class */
final class ConvertingMethod {
    private static final String[] noStrings = new String[0];
    private final Method method;
    private final MXBeanMapping returnMapping;
    private final MXBeanMapping[] paramMappings;
    private final boolean paramConversionIsIdentity;

    static ConvertingMethod from(Method method) {
        try {
            return new ConvertingMethod(method);
        } catch (OpenDataException e2) {
            throw new IllegalArgumentException("Method " + method.getDeclaringClass().getName() + "." + method.getName() + " has parameter or return type that cannot be translated into an open type", e2);
        }
    }

    Method getMethod() {
        return this.method;
    }

    Descriptor getDescriptor() {
        return Introspector.descriptorForElement(this.method);
    }

    Type getGenericReturnType() {
        return this.method.getGenericReturnType();
    }

    Type[] getGenericParameterTypes() {
        return this.method.getGenericParameterTypes();
    }

    String getName() {
        return this.method.getName();
    }

    OpenType<?> getOpenReturnType() {
        return this.returnMapping.getOpenType();
    }

    OpenType<?>[] getOpenParameterTypes() {
        OpenType<?>[] openTypeArr = new OpenType[this.paramMappings.length];
        for (int i2 = 0; i2 < this.paramMappings.length; i2++) {
            openTypeArr[i2] = this.paramMappings[i2].getOpenType();
        }
        return openTypeArr;
    }

    void checkCallFromOpen() {
        try {
            for (MXBeanMapping mXBeanMapping : this.paramMappings) {
                mXBeanMapping.checkReconstructible();
            }
        } catch (InvalidObjectException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    void checkCallToOpen() {
        try {
            this.returnMapping.checkReconstructible();
        } catch (InvalidObjectException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    String[] getOpenSignature() {
        if (this.paramMappings.length == 0) {
            return noStrings;
        }
        String[] strArr = new String[this.paramMappings.length];
        for (int i2 = 0; i2 < this.paramMappings.length; i2++) {
            strArr[i2] = this.paramMappings[i2].getOpenClass().getName();
        }
        return strArr;
    }

    final Object toOpenReturnValue(MXBeanLookup mXBeanLookup, Object obj) throws OpenDataException {
        return this.returnMapping.toOpenValue(obj);
    }

    final Object fromOpenReturnValue(MXBeanLookup mXBeanLookup, Object obj) throws InvalidObjectException {
        return this.returnMapping.fromOpenValue(obj);
    }

    final Object[] toOpenParameters(MXBeanLookup mXBeanLookup, Object[] objArr) throws OpenDataException {
        if (this.paramConversionIsIdentity || objArr == null) {
            return objArr;
        }
        Object[] objArr2 = new Object[objArr.length];
        for (int i2 = 0; i2 < objArr.length; i2++) {
            objArr2[i2] = this.paramMappings[i2].toOpenValue(objArr[i2]);
        }
        return objArr2;
    }

    final Object[] fromOpenParameters(Object[] objArr) throws InvalidObjectException {
        if (this.paramConversionIsIdentity || objArr == null) {
            return objArr;
        }
        Object[] objArr2 = new Object[objArr.length];
        for (int i2 = 0; i2 < objArr.length; i2++) {
            objArr2[i2] = this.paramMappings[i2].fromOpenValue(objArr[i2]);
        }
        return objArr2;
    }

    final Object toOpenParameter(MXBeanLookup mXBeanLookup, Object obj, int i2) throws OpenDataException {
        return this.paramMappings[i2].toOpenValue(obj);
    }

    final Object fromOpenParameter(MXBeanLookup mXBeanLookup, Object obj, int i2) throws InvalidObjectException {
        return this.paramMappings[i2].fromOpenValue(obj);
    }

    Object invokeWithOpenReturn(MXBeanLookup mXBeanLookup, Object obj, Object[] objArr) throws MBeanException, IllegalAccessException, InvocationTargetException {
        MXBeanLookup lookup = MXBeanLookup.getLookup();
        try {
            MXBeanLookup.setLookup(mXBeanLookup);
            Object objInvokeWithOpenReturn = invokeWithOpenReturn(obj, objArr);
            MXBeanLookup.setLookup(lookup);
            return objInvokeWithOpenReturn;
        } catch (Throwable th) {
            MXBeanLookup.setLookup(lookup);
            throw th;
        }
    }

    private Object invokeWithOpenReturn(Object obj, Object[] objArr) throws MBeanException, IllegalAccessException, InvocationTargetException {
        try {
            try {
                return this.returnMapping.toOpenValue(MethodUtil.invoke(this.method, obj, fromOpenParameters(objArr)));
            } catch (OpenDataException e2) {
                throw new MBeanException(e2, methodName() + ": cannot convert return value to open value: " + ((Object) e2));
            }
        } catch (InvalidObjectException e3) {
            throw new MBeanException(e3, methodName() + ": cannot convert parameters from open values: " + ((Object) e3));
        }
    }

    private String methodName() {
        return ((Object) this.method.getDeclaringClass()) + "." + this.method.getName();
    }

    private ConvertingMethod(Method method) throws OpenDataException {
        this.method = method;
        MXBeanMappingFactory mXBeanMappingFactory = MXBeanMappingFactory.DEFAULT;
        this.returnMapping = mXBeanMappingFactory.mappingForType(method.getGenericReturnType(), mXBeanMappingFactory);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        this.paramMappings = new MXBeanMapping[genericParameterTypes.length];
        boolean zIsIdentity = true;
        for (int i2 = 0; i2 < genericParameterTypes.length; i2++) {
            this.paramMappings[i2] = mXBeanMappingFactory.mappingForType(genericParameterTypes[i2], mXBeanMappingFactory);
            zIsIdentity &= DefaultMXBeanMappingFactory.isIdentity(this.paramMappings[i2]);
        }
        this.paramConversionIsIdentity = zIsIdentity;
    }
}
