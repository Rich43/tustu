package com.sun.beans.finder;

/* loaded from: rt.jar:com/sun/beans/finder/Signature.class */
final class Signature {
    private final Class<?> type;
    private final String name;
    private final Class<?>[] args;
    private volatile int code;

    Signature(Class<?> cls, Class<?>[] clsArr) {
        this(cls, null, clsArr);
    }

    Signature(Class<?> cls, String str, Class<?>[] clsArr) {
        this.type = cls;
        this.name = str;
        this.args = clsArr;
    }

    Class<?> getType() {
        return this.type;
    }

    String getName() {
        return this.name;
    }

    Class<?>[] getArgs() {
        return this.args;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Signature) {
            Signature signature = (Signature) obj;
            return isEqual(signature.type, this.type) && isEqual(signature.name, this.name) && isEqual(signature.args, this.args);
        }
        return false;
    }

    private static boolean isEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    private static boolean isEqual(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr == null || clsArr2 == null) {
            return clsArr == clsArr2;
        }
        if (clsArr.length != clsArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            if (!isEqual(clsArr[i2], clsArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (this.code == 0) {
            int iAddHashCode = addHashCode(addHashCode(17, this.type), this.name);
            if (this.args != null) {
                for (Class<?> cls : this.args) {
                    iAddHashCode = addHashCode(iAddHashCode, cls);
                }
            }
            this.code = iAddHashCode;
        }
        return this.code;
    }

    private static int addHashCode(int i2, Object obj) {
        int i3 = i2 * 37;
        return obj != null ? i3 + obj.hashCode() : i3;
    }
}
