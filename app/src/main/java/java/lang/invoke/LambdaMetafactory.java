package java.lang.invoke;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

/* loaded from: rt.jar:java/lang/invoke/LambdaMetafactory.class */
public class LambdaMetafactory {
    public static final int FLAG_SERIALIZABLE = 1;
    public static final int FLAG_MARKERS = 2;
    public static final int FLAG_BRIDGES = 4;
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
    private static final MethodType[] EMPTY_MT_ARRAY = new MethodType[0];

    public static CallSite metafactory(MethodHandles.Lookup lookup, String str, MethodType methodType, MethodType methodType2, MethodHandle methodHandle, MethodType methodType3) throws LambdaConversionException {
        InnerClassLambdaMetafactory innerClassLambdaMetafactory = new InnerClassLambdaMetafactory(lookup, methodType, str, methodType2, methodHandle, methodType3, false, EMPTY_CLASS_ARRAY, EMPTY_MT_ARRAY);
        innerClassLambdaMetafactory.validateMetafactoryArgs();
        return innerClassLambdaMetafactory.buildCallSite();
    }

    public static CallSite altMetafactory(MethodHandles.Lookup lookup, String str, MethodType methodType, Object... objArr) throws LambdaConversionException {
        Class<?>[] clsArr;
        MethodType[] methodTypeArr;
        MethodType methodType2 = (MethodType) objArr[0];
        MethodHandle methodHandle = (MethodHandle) objArr[1];
        MethodType methodType3 = (MethodType) objArr[2];
        int iIntValue = ((Integer) objArr[3]).intValue();
        int i2 = 4;
        if ((iIntValue & 2) != 0) {
            int i3 = 4 + 1;
            int iIntValue2 = ((Integer) objArr[4]).intValue();
            clsArr = new Class[iIntValue2];
            System.arraycopy(objArr, i3, clsArr, 0, iIntValue2);
            i2 = i3 + iIntValue2;
        } else {
            clsArr = EMPTY_CLASS_ARRAY;
        }
        if ((iIntValue & 4) != 0) {
            int i4 = i2;
            int i5 = i2 + 1;
            int iIntValue3 = ((Integer) objArr[i4]).intValue();
            methodTypeArr = new MethodType[iIntValue3];
            System.arraycopy(objArr, i5, methodTypeArr, 0, iIntValue3);
            int i6 = i5 + iIntValue3;
        } else {
            methodTypeArr = EMPTY_MT_ARRAY;
        }
        boolean z2 = (iIntValue & 1) != 0;
        if (z2) {
            boolean zIsAssignableFrom = Serializable.class.isAssignableFrom(methodType.returnType());
            for (Class<?> cls : clsArr) {
                zIsAssignableFrom |= Serializable.class.isAssignableFrom(cls);
            }
            if (!zIsAssignableFrom) {
                clsArr = (Class[]) Arrays.copyOf(clsArr, clsArr.length + 1);
                clsArr[clsArr.length - 1] = Serializable.class;
            }
        }
        InnerClassLambdaMetafactory innerClassLambdaMetafactory = new InnerClassLambdaMetafactory(lookup, methodType, str, methodType2, methodHandle, methodType3, z2, clsArr, methodTypeArr);
        innerClassLambdaMetafactory.validateMetafactoryArgs();
        return innerClassLambdaMetafactory.buildCallSite();
    }
}
