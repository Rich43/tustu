package com.sun.beans.finder;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: rt.jar:com/sun/beans/finder/InstanceFinder.class */
public class InstanceFinder<T> {
    private static final String[] EMPTY = new String[0];
    private final Class<? extends T> type;
    private final boolean allow;
    private final String suffix;
    private volatile String[] packages;

    InstanceFinder(Class<? extends T> cls, boolean z2, String str, String... strArr) {
        this.type = cls;
        this.allow = z2;
        this.suffix = str;
        this.packages = (String[]) strArr.clone();
    }

    public String[] getPackages() {
        return (String[]) this.packages.clone();
    }

    public void setPackages(String... strArr) {
        this.packages = (strArr == null || strArr.length <= 0) ? EMPTY : (String[]) strArr.clone();
    }

    public T find(Class<?> cls) {
        T tInstantiate;
        if (cls == null) {
            return null;
        }
        String strSubstring = cls.getName() + this.suffix;
        T tInstantiate2 = instantiate(cls, strSubstring);
        if (tInstantiate2 != null) {
            return tInstantiate2;
        }
        if (this.allow && (tInstantiate = instantiate(cls, null)) != null) {
            return tInstantiate;
        }
        int iLastIndexOf = strSubstring.lastIndexOf(46) + 1;
        if (iLastIndexOf > 0) {
            strSubstring = strSubstring.substring(iLastIndexOf);
        }
        for (String str : this.packages) {
            T tInstantiate3 = instantiate(cls, str, strSubstring);
            if (tInstantiate3 != null) {
                return tInstantiate3;
            }
        }
        return null;
    }

    protected T instantiate(Class<?> cls, String str) {
        if (cls != null) {
            if (str != null) {
                try {
                    cls = ClassFinder.findClass(str, cls.getClassLoader());
                } catch (Exception e2) {
                    return null;
                }
            }
            if (this.type.isAssignableFrom(cls)) {
                return (T) cls.newInstance();
            }
            return null;
        }
        return null;
    }

    protected T instantiate(Class<?> cls, String str, String str2) {
        return instantiate(cls, str + '.' + str2);
    }
}
