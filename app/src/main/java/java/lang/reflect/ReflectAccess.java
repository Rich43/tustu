package java.lang.reflect;

import sun.reflect.ConstructorAccessor;
import sun.reflect.LangReflectAccess;
import sun.reflect.MethodAccessor;

/* loaded from: rt.jar:java/lang/reflect/ReflectAccess.class */
class ReflectAccess implements LangReflectAccess {
    ReflectAccess() {
    }

    @Override // sun.reflect.LangReflectAccess
    public Field newField(Class<?> cls, String str, Class<?> cls2, int i2, int i3, String str2, byte[] bArr) {
        return new Field(cls, str, cls2, i2, i3, str2, bArr);
    }

    @Override // sun.reflect.LangReflectAccess
    public Method newMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2, Class<?>[] clsArr2, int i2, int i3, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return new Method(cls, str, clsArr, cls2, clsArr2, i2, i3, str2, bArr, bArr2, bArr3);
    }

    @Override // sun.reflect.LangReflectAccess
    public <T> Constructor<T> newConstructor(Class<T> cls, Class<?>[] clsArr, Class<?>[] clsArr2, int i2, int i3, String str, byte[] bArr, byte[] bArr2) {
        return new Constructor<>(cls, clsArr, clsArr2, i2, i3, str, bArr, bArr2);
    }

    @Override // sun.reflect.LangReflectAccess
    public MethodAccessor getMethodAccessor(Method method) {
        return method.getMethodAccessor();
    }

    @Override // sun.reflect.LangReflectAccess
    public void setMethodAccessor(Method method, MethodAccessor methodAccessor) {
        method.setMethodAccessor(methodAccessor);
    }

    @Override // sun.reflect.LangReflectAccess
    public ConstructorAccessor getConstructorAccessor(Constructor<?> constructor) {
        return constructor.getConstructorAccessor();
    }

    @Override // sun.reflect.LangReflectAccess
    public void setConstructorAccessor(Constructor<?> constructor, ConstructorAccessor constructorAccessor) {
        constructor.setConstructorAccessor(constructorAccessor);
    }

    @Override // sun.reflect.LangReflectAccess
    public int getConstructorSlot(Constructor<?> constructor) {
        return constructor.getSlot();
    }

    @Override // sun.reflect.LangReflectAccess
    public String getConstructorSignature(Constructor<?> constructor) {
        return constructor.getSignature();
    }

    @Override // sun.reflect.LangReflectAccess
    public byte[] getConstructorAnnotations(Constructor<?> constructor) {
        return constructor.getRawAnnotations();
    }

    @Override // sun.reflect.LangReflectAccess
    public byte[] getConstructorParameterAnnotations(Constructor<?> constructor) {
        return constructor.getRawParameterAnnotations();
    }

    @Override // sun.reflect.LangReflectAccess
    public byte[] getExecutableTypeAnnotationBytes(Executable executable) {
        return executable.getTypeAnnotationBytes();
    }

    @Override // sun.reflect.LangReflectAccess
    public Method copyMethod(Method method) {
        return method.copy();
    }

    @Override // sun.reflect.LangReflectAccess
    public Field copyField(Field field) {
        return field.copy();
    }

    @Override // sun.reflect.LangReflectAccess
    public <T> Constructor<T> copyConstructor(Constructor<T> constructor) {
        return constructor.copy();
    }
}
