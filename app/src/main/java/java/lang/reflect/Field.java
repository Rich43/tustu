package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import sun.misc.SharedSecrets;
import sun.reflect.CallerSensitive;
import sun.reflect.FieldAccessor;
import sun.reflect.Reflection;
import sun.reflect.annotation.AnnotationParser;
import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.TypeAnnotation;
import sun.reflect.annotation.TypeAnnotationParser;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.repository.FieldRepository;
import sun.reflect.generics.scope.ClassScope;

/* loaded from: rt.jar:java/lang/reflect/Field.class */
public final class Field extends AccessibleObject implements Member {
    private Class<?> clazz;
    private int slot;
    private String name;
    private Class<?> type;
    private int modifiers;
    private transient String signature;
    private transient FieldRepository genericInfo;
    private byte[] annotations;
    private FieldAccessor fieldAccessor;
    private FieldAccessor overrideFieldAccessor;
    private Field root;
    private volatile transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;

    private native byte[] getTypeAnnotationBytes0();

    private String getGenericSignature() {
        return this.signature;
    }

    private GenericsFactory getFactory() {
        Class<?> declaringClass = getDeclaringClass();
        return CoreReflectionFactory.make(declaringClass, ClassScope.make(declaringClass));
    }

    private FieldRepository getGenericInfo() {
        if (this.genericInfo == null) {
            this.genericInfo = FieldRepository.make(getGenericSignature(), getFactory());
        }
        return this.genericInfo;
    }

    Field(Class<?> cls, String str, Class<?> cls2, int i2, int i3, String str2, byte[] bArr) {
        this.clazz = cls;
        this.name = str;
        this.type = cls2;
        this.modifiers = i2;
        this.slot = i3;
        this.signature = str2;
        this.annotations = bArr;
    }

    Field copy() {
        if (this.root != null) {
            throw new IllegalArgumentException("Can not copy a non-root Field");
        }
        Field field = new Field(this.clazz, this.name, this.type, this.modifiers, this.slot, this.signature, this.annotations);
        field.root = this;
        field.fieldAccessor = this.fieldAccessor;
        field.overrideFieldAccessor = this.overrideFieldAccessor;
        return field;
    }

    @Override // java.lang.reflect.Member
    public Class<?> getDeclaringClass() {
        return this.clazz;
    }

    @Override // java.lang.reflect.Member
    public String getName() {
        return this.name;
    }

    @Override // java.lang.reflect.Member
    public int getModifiers() {
        return this.modifiers;
    }

    public boolean isEnumConstant() {
        return (getModifiers() & 16384) != 0;
    }

    @Override // java.lang.reflect.Member
    public boolean isSynthetic() {
        return Modifier.isSynthetic(getModifiers());
    }

    public Class<?> getType() {
        return this.type;
    }

    public Type getGenericType() {
        if (getGenericSignature() != null) {
            return getGenericInfo().getGenericType();
        }
        return getType();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Field)) {
            Field field = (Field) obj;
            return getDeclaringClass() == field.getDeclaringClass() && getName() == field.getName() && getType() == field.getType();
        }
        return false;
    }

    public int hashCode() {
        return getDeclaringClass().getName().hashCode() ^ getName().hashCode();
    }

    public String toString() {
        int modifiers = getModifiers();
        return (modifiers == 0 ? "" : Modifier.toString(modifiers) + " ") + getType().getTypeName() + " " + getDeclaringClass().getTypeName() + "." + getName();
    }

    public String toGenericString() {
        int modifiers = getModifiers();
        return (modifiers == 0 ? "" : Modifier.toString(modifiers) + " ") + getGenericType().getTypeName() + " " + getDeclaringClass().getTypeName() + "." + getName();
    }

    @CallerSensitive
    public Object get(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).get(obj);
    }

    @CallerSensitive
    public boolean getBoolean(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getBoolean(obj);
    }

    @CallerSensitive
    public byte getByte(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getByte(obj);
    }

    @CallerSensitive
    public char getChar(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getChar(obj);
    }

    @CallerSensitive
    public short getShort(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getShort(obj);
    }

    @CallerSensitive
    public int getInt(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getInt(obj);
    }

    @CallerSensitive
    public long getLong(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getLong(obj);
    }

    @CallerSensitive
    public float getFloat(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getFloat(obj);
    }

    @CallerSensitive
    public double getDouble(Object obj) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        return getFieldAccessor(obj).getDouble(obj);
    }

    @CallerSensitive
    public void set(Object obj, Object obj2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).set(obj, obj2);
    }

    @CallerSensitive
    public void setBoolean(Object obj, boolean z2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setBoolean(obj, z2);
    }

    @CallerSensitive
    public void setByte(Object obj, byte b2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setByte(obj, b2);
    }

    @CallerSensitive
    public void setChar(Object obj, char c2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setChar(obj, c2);
    }

    @CallerSensitive
    public void setShort(Object obj, short s2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setShort(obj, s2);
    }

    @CallerSensitive
    public void setInt(Object obj, int i2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setInt(obj, i2);
    }

    @CallerSensitive
    public void setLong(Object obj, long j2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setLong(obj, j2);
    }

    @CallerSensitive
    public void setFloat(Object obj, float f2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setFloat(obj, f2);
    }

    @CallerSensitive
    public void setDouble(Object obj, double d2) throws IllegalAccessException, IllegalArgumentException {
        if (!this.override && !Reflection.quickCheckMemberAccess(this.clazz, this.modifiers)) {
            checkAccess(Reflection.getCallerClass(), this.clazz, obj, this.modifiers);
        }
        getFieldAccessor(obj).setDouble(obj, d2);
    }

    private FieldAccessor getFieldAccessor(Object obj) throws IllegalAccessException {
        boolean z2 = this.override;
        FieldAccessor fieldAccessor = z2 ? this.overrideFieldAccessor : this.fieldAccessor;
        return fieldAccessor != null ? fieldAccessor : acquireFieldAccessor(z2);
    }

    private FieldAccessor acquireFieldAccessor(boolean z2) {
        FieldAccessor fieldAccessorNewFieldAccessor = null;
        if (this.root != null) {
            fieldAccessorNewFieldAccessor = this.root.getFieldAccessor(z2);
        }
        if (fieldAccessorNewFieldAccessor != null) {
            if (z2) {
                this.overrideFieldAccessor = fieldAccessorNewFieldAccessor;
            } else {
                this.fieldAccessor = fieldAccessorNewFieldAccessor;
            }
        } else {
            fieldAccessorNewFieldAccessor = reflectionFactory.newFieldAccessor(this, z2);
            setFieldAccessor(fieldAccessorNewFieldAccessor, z2);
        }
        return fieldAccessorNewFieldAccessor;
    }

    private FieldAccessor getFieldAccessor(boolean z2) {
        return z2 ? this.overrideFieldAccessor : this.fieldAccessor;
    }

    private void setFieldAccessor(FieldAccessor fieldAccessor, boolean z2) {
        if (z2) {
            this.overrideFieldAccessor = fieldAccessor;
        } else {
            this.fieldAccessor = fieldAccessor;
        }
        if (this.root != null) {
            this.root.setFieldAccessor(fieldAccessor, z2);
        }
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return cls.cast(declaredAnnotations().get(cls));
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T[]) AnnotationSupport.getDirectlyAndIndirectlyPresent(declaredAnnotations(), cls);
    }

    @Override // java.lang.reflect.AccessibleObject, java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return AnnotationParser.toArray(declaredAnnotations());
    }

    private Map<Class<? extends Annotation>, Annotation> declaredAnnotations() {
        Map<Class<? extends Annotation>, Annotation> map = this.declaredAnnotations;
        Map<Class<? extends Annotation>, Annotation> annotations = map;
        if (map == null) {
            synchronized (this) {
                Map<Class<? extends Annotation>, Annotation> map2 = this.declaredAnnotations;
                annotations = map2;
                if (map2 == null) {
                    Field field = this.root;
                    if (field != null) {
                        annotations = field.declaredAnnotations();
                    } else {
                        annotations = AnnotationParser.parseAnnotations(this.annotations, SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
                    }
                    this.declaredAnnotations = annotations;
                }
            }
        }
        return annotations;
    }

    public AnnotatedType getAnnotatedType() {
        return TypeAnnotationParser.buildAnnotatedType(getTypeAnnotationBytes0(), SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), this, getDeclaringClass(), getGenericType(), TypeAnnotation.TypeAnnotationTarget.FIELD);
    }
}
