package java.io;

import java.lang.reflect.Field;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/io/ObjectStreamField.class */
public class ObjectStreamField implements Comparable<Object> {
    private final String name;
    private final String signature;
    private final Class<?> type;
    private final boolean unshared;
    private final Field field;
    private int offset;

    public ObjectStreamField(String str, Class<?> cls) {
        this(str, cls, false);
    }

    public ObjectStreamField(String str, Class<?> cls, boolean z2) {
        this.offset = 0;
        if (str == null) {
            throw new NullPointerException();
        }
        this.name = str;
        this.type = cls;
        this.unshared = z2;
        this.signature = getClassSignature(cls).intern();
        this.field = null;
    }

    ObjectStreamField(String str, String str2, boolean z2) {
        this.offset = 0;
        if (str == null) {
            throw new NullPointerException();
        }
        this.name = str;
        this.signature = str2.intern();
        this.unshared = z2;
        this.field = null;
        switch (str2.charAt(0)) {
            case 'B':
                this.type = Byte.TYPE;
                return;
            case 'C':
                this.type = Character.TYPE;
                return;
            case 'D':
                this.type = Double.TYPE;
                return;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new IllegalArgumentException("illegal signature");
            case 'F':
                this.type = Float.TYPE;
                return;
            case 'I':
                this.type = Integer.TYPE;
                return;
            case 'J':
                this.type = Long.TYPE;
                return;
            case 'L':
            case '[':
                this.type = Object.class;
                return;
            case 'S':
                this.type = Short.TYPE;
                return;
            case 'Z':
                this.type = Boolean.TYPE;
                return;
        }
    }

    ObjectStreamField(Field field, boolean z2, boolean z3) {
        this.offset = 0;
        this.field = field;
        this.unshared = z2;
        this.name = field.getName();
        Class<?> type = field.getType();
        this.type = (z3 || type.isPrimitive()) ? type : Object.class;
        this.signature = getClassSignature(type).intern();
    }

    public String getName() {
        return this.name;
    }

    @CallerSensitive
    public Class<?> getType() {
        if (System.getSecurityManager() != null && ReflectUtil.needsPackageAccessCheck(Reflection.getCallerClass().getClassLoader(), this.type.getClassLoader())) {
            ReflectUtil.checkPackageAccess(this.type);
        }
        return this.type;
    }

    public char getTypeCode() {
        return this.signature.charAt(0);
    }

    public String getTypeString() {
        if (isPrimitive()) {
            return null;
        }
        return this.signature;
    }

    public int getOffset() {
        return this.offset;
    }

    protected void setOffset(int i2) {
        this.offset = i2;
    }

    public boolean isPrimitive() {
        char cCharAt = this.signature.charAt(0);
        return (cCharAt == 'L' || cCharAt == '[') ? false : true;
    }

    public boolean isUnshared() {
        return this.unshared;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        ObjectStreamField objectStreamField = (ObjectStreamField) obj;
        boolean zIsPrimitive = isPrimitive();
        if (zIsPrimitive != objectStreamField.isPrimitive()) {
            return zIsPrimitive ? -1 : 1;
        }
        return this.name.compareTo(objectStreamField.name);
    }

    public String toString() {
        return this.signature + ' ' + this.name;
    }

    Field getField() {
        return this.field;
    }

    String getSignature() {
        return this.signature;
    }

    private static String getClassSignature(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        while (cls.isArray()) {
            sb.append('[');
            cls = cls.getComponentType();
        }
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                sb.append('I');
            } else if (cls == Byte.TYPE) {
                sb.append('B');
            } else if (cls == Long.TYPE) {
                sb.append('J');
            } else if (cls == Float.TYPE) {
                sb.append('F');
            } else if (cls == Double.TYPE) {
                sb.append('D');
            } else if (cls == Short.TYPE) {
                sb.append('S');
            } else if (cls == Character.TYPE) {
                sb.append('C');
            } else if (cls == Boolean.TYPE) {
                sb.append('Z');
            } else if (cls == Void.TYPE) {
                sb.append('V');
            } else {
                throw new InternalError();
            }
        } else {
            sb.append('L' + cls.getName().replace('.', '/') + ';');
        }
        return sb.toString();
    }
}
