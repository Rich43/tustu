package jdk.internal.dynalink.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/AccessibleMembersLookup.class */
class AccessibleMembersLookup {
    private final Map<MethodSignature, Method> methods = new HashMap();
    private final Set<Class<?>> innerClasses = new LinkedHashSet();
    private final boolean instance;

    AccessibleMembersLookup(Class<?> clazz, boolean instance) throws SecurityException {
        this.instance = instance;
        lookupAccessibleMembers(clazz);
    }

    Method getAccessibleMethod(Method m2) {
        if (m2 == null) {
            return null;
        }
        return this.methods.get(new MethodSignature(m2));
    }

    Collection<Method> getMethods() {
        return this.methods.values();
    }

    Class<?>[] getInnerClasses() {
        return (Class[]) this.innerClasses.toArray(new Class[this.innerClasses.size()]);
    }

    /* loaded from: nashorn.jar:jdk/internal/dynalink/beans/AccessibleMembersLookup$MethodSignature.class */
    static final class MethodSignature {
        private final String name;
        private final Class<?>[] args;

        MethodSignature(String name, Class<?>[] args) {
            this.name = name;
            this.args = args;
        }

        MethodSignature(Method method) {
            this(method.getName(), method.getParameterTypes());
        }

        public boolean equals(Object o2) {
            if (o2 instanceof MethodSignature) {
                MethodSignature ms = (MethodSignature) o2;
                return ms.name.equals(this.name) && Arrays.equals(this.args, ms.args);
            }
            return false;
        }

        public int hashCode() {
            return this.name.hashCode() ^ Arrays.hashCode(this.args);
        }

        public String toString() {
            StringBuilder b2 = new StringBuilder();
            b2.append("[MethodSignature ").append(this.name).append('(');
            if (this.args.length > 0) {
                b2.append(this.args[0].getCanonicalName());
                for (int i2 = 1; i2 < this.args.length; i2++) {
                    b2.append(", ").append(this.args[i2].getCanonicalName());
                }
            }
            return b2.append(")]").toString();
        }
    }

    private void lookupAccessibleMembers(Class<?> clazz) throws SecurityException {
        boolean searchSuperTypes;
        if (!CheckRestrictedPackage.isRestrictedClass(clazz)) {
            searchSuperTypes = false;
            for (Method method : clazz.getMethods()) {
                boolean isStatic = Modifier.isStatic(method.getModifiers());
                if (this.instance != isStatic) {
                    MethodSignature sig = new MethodSignature(method);
                    if (!this.methods.containsKey(sig)) {
                        Class<?> declaringClass = method.getDeclaringClass();
                        if (declaringClass != clazz && CheckRestrictedPackage.isRestrictedClass(declaringClass)) {
                            searchSuperTypes = true;
                        } else if (!isStatic || clazz == declaringClass) {
                            this.methods.put(sig, method);
                        }
                    }
                }
            }
            for (Class<?> innerClass : clazz.getClasses()) {
                this.innerClasses.add(innerClass);
            }
        } else {
            searchSuperTypes = true;
        }
        if (this.instance && searchSuperTypes) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> cls : interfaces) {
                lookupAccessibleMembers(cls);
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                lookupAccessibleMembers(superclass);
            }
        }
    }
}
