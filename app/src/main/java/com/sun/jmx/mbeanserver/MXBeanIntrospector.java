package com.sun.jmx.mbeanserver;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.jmx.mbeanserver.MBeanIntrospector;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.JMX;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.RuntimeOperationsException;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfo;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanIntrospector.class */
class MXBeanIntrospector extends MBeanIntrospector<ConvertingMethod> {
    private final MBeanIntrospector.PerInterfaceMap<ConvertingMethod> perInterfaceMap = new MBeanIntrospector.PerInterfaceMap<>();
    private static final MXBeanIntrospector instance = new MXBeanIntrospector();
    private static final MBeanIntrospector.MBeanInfoMap mbeanInfoMap = new MBeanIntrospector.MBeanInfoMap();

    MXBeanIntrospector() {
    }

    static MXBeanIntrospector getInstance() {
        return instance;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    MBeanIntrospector.PerInterfaceMap<ConvertingMethod> getPerInterfaceMap() {
        return this.perInterfaceMap;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    MBeanIntrospector.MBeanInfoMap getMBeanInfoMap() {
        return mbeanInfoMap;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    MBeanAnalyzer<ConvertingMethod> getAnalyzer(Class<?> cls) throws NotCompliantMBeanException {
        return MBeanAnalyzer.analyzer(cls, this);
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    boolean isMXBean() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public ConvertingMethod mFrom(Method method) {
        return ConvertingMethod.from(method);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public String getName(ConvertingMethod convertingMethod) {
        return convertingMethod.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public Type getGenericReturnType(ConvertingMethod convertingMethod) {
        return convertingMethod.getGenericReturnType();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public Type[] getGenericParameterTypes(ConvertingMethod convertingMethod) {
        return convertingMethod.getGenericParameterTypes();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public String[] getSignature(ConvertingMethod convertingMethod) {
        return convertingMethod.getOpenSignature();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public void checkMethod(ConvertingMethod convertingMethod) {
        convertingMethod.checkCallFromOpen();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public Object invokeM2(ConvertingMethod convertingMethod, Object obj, Object[] objArr, Object obj2) throws MBeanException, IllegalAccessException, InvocationTargetException {
        return convertingMethod.invokeWithOpenReturn((MXBeanLookup) obj2, obj, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public boolean validParameter(ConvertingMethod convertingMethod, Object obj, int i2, Object obj2) {
        if (obj == null) {
            Type type = convertingMethod.getGenericParameterTypes()[i2];
            return ((type instanceof Class) && ((Class) type).isPrimitive()) ? false : true;
        }
        try {
            return isValidParameter(convertingMethod.getMethod(), convertingMethod.fromOpenParameter((MXBeanLookup) obj2, obj, i2), i2);
        } catch (Exception e2) {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public MBeanAttributeInfo getMBeanAttributeInfo(String str, ConvertingMethod convertingMethod, ConvertingMethod convertingMethod2) throws RuntimeOperationsException {
        OpenType<?> openReturnType;
        Type genericReturnType;
        boolean z2 = convertingMethod != null;
        boolean z3 = convertingMethod2 != null;
        boolean z4 = z2 && getName(convertingMethod).startsWith(BeanAdapter.IS_PREFIX);
        if (z2) {
            openReturnType = convertingMethod.getOpenReturnType();
            genericReturnType = convertingMethod.getGenericReturnType();
        } else {
            openReturnType = convertingMethod2.getOpenParameterTypes()[0];
            genericReturnType = convertingMethod2.getGenericParameterTypes()[0];
        }
        Descriptor descriptorTypeDescriptor = typeDescriptor(openReturnType, genericReturnType);
        if (z2) {
            descriptorTypeDescriptor = ImmutableDescriptor.union(descriptorTypeDescriptor, convertingMethod.getDescriptor());
        }
        if (z3) {
            descriptorTypeDescriptor = ImmutableDescriptor.union(descriptorTypeDescriptor, convertingMethod2.getDescriptor());
        }
        return canUseOpenInfo(genericReturnType) ? new OpenMBeanAttributeInfoSupport(str, str, openReturnType, z2, z3, z4, descriptorTypeDescriptor) : new MBeanAttributeInfo(str, originalTypeString(genericReturnType), str, z2, z3, z4, descriptorTypeDescriptor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public MBeanOperationInfo getMBeanOperationInfo(String str, ConvertingMethod convertingMethod) throws RuntimeOperationsException {
        String strOriginalTypeString;
        MBeanOperationInfo mBeanOperationInfo;
        MBeanParameterInfo mBeanParameterInfo;
        Method method = convertingMethod.getMethod();
        OpenType<?> openReturnType = convertingMethod.getOpenReturnType();
        Type genericReturnType = convertingMethod.getGenericReturnType();
        OpenType<?>[] openParameterTypes = convertingMethod.getOpenParameterTypes();
        Type[] genericParameterTypes = convertingMethod.getGenericParameterTypes();
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[openParameterTypes.length];
        boolean zCanUseOpenInfo = canUseOpenInfo(genericReturnType);
        boolean z2 = true;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i2 = 0; i2 < openParameterTypes.length; i2++) {
            String str2 = "p" + i2;
            OpenType<?> openType = openParameterTypes[i2];
            Type type = genericParameterTypes[i2];
            ImmutableDescriptor immutableDescriptorUnion = ImmutableDescriptor.union(typeDescriptor(openType, type), Introspector.descriptorForAnnotations(parameterAnnotations[i2]));
            if (canUseOpenInfo(type)) {
                mBeanParameterInfo = new OpenMBeanParameterInfoSupport(str2, str2, openType, (Descriptor) immutableDescriptorUnion);
            } else {
                z2 = false;
                mBeanParameterInfo = new MBeanParameterInfo(str2, originalTypeString(type), str2, immutableDescriptorUnion);
            }
            mBeanParameterInfoArr[i2] = mBeanParameterInfo;
        }
        ImmutableDescriptor immutableDescriptorUnion2 = ImmutableDescriptor.union(typeDescriptor(openReturnType, genericReturnType), Introspector.descriptorForElement(method));
        if (!zCanUseOpenInfo || !z2) {
            if (zCanUseOpenInfo) {
                strOriginalTypeString = openReturnType.getClassName();
            } else {
                strOriginalTypeString = originalTypeString(genericReturnType);
            }
            mBeanOperationInfo = new MBeanOperationInfo(str, str, mBeanParameterInfoArr, strOriginalTypeString, 3, immutableDescriptorUnion2);
        } else {
            OpenMBeanParameterInfo[] openMBeanParameterInfoArr = new OpenMBeanParameterInfo[mBeanParameterInfoArr.length];
            System.arraycopy(mBeanParameterInfoArr, 0, openMBeanParameterInfoArr, 0, mBeanParameterInfoArr.length);
            mBeanOperationInfo = new OpenMBeanOperationInfoSupport(str, str, openMBeanParameterInfoArr, openReturnType, 3, immutableDescriptorUnion2);
        }
        return mBeanOperationInfo;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    Descriptor getBasicMBeanDescriptor() {
        return new ImmutableDescriptor("mxbean=true", "immutableInfo=true");
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    Descriptor getMBeanDescriptor(Class<?> cls) {
        return ImmutableDescriptor.EMPTY_DESCRIPTOR;
    }

    private static Descriptor typeDescriptor(OpenType<?> openType, Type type) {
        return new ImmutableDescriptor(new String[]{JMX.OPEN_TYPE_FIELD, JMX.ORIGINAL_TYPE_FIELD}, new Object[]{openType, originalTypeString(type)});
    }

    private static boolean canUseOpenInfo(Type type) {
        if (type instanceof GenericArrayType) {
            return canUseOpenInfo(((GenericArrayType) type).getGenericComponentType());
        }
        if ((type instanceof Class) && ((Class) type).isArray()) {
            return canUseOpenInfo(((Class) type).getComponentType());
        }
        return ((type instanceof Class) && ((Class) type).isPrimitive()) ? false : true;
    }

    private static String originalTypeString(Type type) {
        if (type instanceof Class) {
            return ((Class) type).getName();
        }
        return typeName(type);
    }

    static String typeName(Type type) {
        if (type instanceof Class) {
            Class cls = (Class) type;
            if (cls.isArray()) {
                return typeName(cls.getComponentType()) + "[]";
            }
            return cls.getName();
        }
        if (type instanceof GenericArrayType) {
            return typeName(((GenericArrayType) type).getGenericComponentType()) + "[]";
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            StringBuilder sb = new StringBuilder();
            sb.append(typeName(parameterizedType.getRawType())).append("<");
            String str = "";
            for (Type type2 : parameterizedType.getActualTypeArguments()) {
                sb.append(str).append(typeName(type2));
                str = ", ";
            }
            return sb.append(">").toString();
        }
        return "???";
    }
}
