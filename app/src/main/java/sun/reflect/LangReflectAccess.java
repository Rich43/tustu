package sun.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* loaded from: rt.jar:sun/reflect/LangReflectAccess.class */
public interface LangReflectAccess {
    Field newField(Class<?> cls, String str, Class<?> cls2, int i2, int i3, String str2, byte[] bArr);

    Method newMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2, Class<?>[] clsArr2, int i2, int i3, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3);

    <T> Constructor<T> newConstructor(Class<T> cls, Class<?>[] clsArr, Class<?>[] clsArr2, int i2, int i3, String str, byte[] bArr, byte[] bArr2);

    MethodAccessor getMethodAccessor(Method method);

    void setMethodAccessor(Method method, MethodAccessor methodAccessor);

    ConstructorAccessor getConstructorAccessor(Constructor<?> constructor);

    void setConstructorAccessor(Constructor<?> constructor, ConstructorAccessor constructorAccessor);

    byte[] getExecutableTypeAnnotationBytes(Executable executable);

    int getConstructorSlot(Constructor<?> constructor);

    String getConstructorSignature(Constructor<?> constructor);

    byte[] getConstructorAnnotations(Constructor<?> constructor);

    byte[] getConstructorParameterAnnotations(Constructor<?> constructor);

    Method copyMethod(Method method);

    Field copyField(Field field);

    <T> Constructor<T> copyConstructor(Constructor<T> constructor);
}
