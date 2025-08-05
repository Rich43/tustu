package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.Util;
import com.sun.xml.internal.bind.v2.bytecode.ClassTailor;
import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/OptimizedAccessorFactory.class */
public abstract class OptimizedAccessorFactory {
    private static final Logger logger = Util.getClassLogger();
    private static final String fieldTemplateName;
    private static final String methodTemplateName;

    private OptimizedAccessorFactory() {
    }

    static {
        String s2 = FieldAccessor_Byte.class.getName();
        fieldTemplateName = s2.substring(0, s2.length() - "Byte".length()).replace('.', '/');
        String s3 = MethodAccessor_Byte.class.getName();
        methodTemplateName = s3.substring(0, s3.length() - "Byte".length()).replace('.', '/');
    }

    public static final <B, V> Accessor<B, V> get(Method getter, Method setter) {
        Class opt;
        if (getter.getParameterTypes().length != 0) {
            return null;
        }
        Class[] sparams = setter.getParameterTypes();
        if (sparams.length != 1 || sparams[0] != getter.getReturnType() || setter.getReturnType() != Void.TYPE || getter.getDeclaringClass() != setter.getDeclaringClass() || Modifier.isPrivate(getter.getModifiers()) || Modifier.isPrivate(setter.getModifiers())) {
            return null;
        }
        Class t2 = sparams[0];
        String typeName = t2.getName().replace('.', '_');
        if (t2.isArray()) {
            String typeName2 = "AOf_";
            String compName = t2.getComponentType().getName().replace('.', '_');
            while (compName.startsWith("[L")) {
                compName = compName.substring(2);
                typeName2 = typeName2 + "AOf_";
            }
            typeName = typeName2 + compName;
        }
        String newClassName = ClassTailor.toVMClassName(getter.getDeclaringClass()) + "$JaxbAccessorM_" + getter.getName() + '_' + setter.getName() + '_' + typeName;
        if (t2.isPrimitive()) {
            opt = AccessorInjector.prepare(getter.getDeclaringClass(), methodTemplateName + RuntimeUtil.primitiveToBox.get(t2).getSimpleName(), newClassName, ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(getter.getDeclaringClass()), "get_" + t2.getName(), getter.getName(), "set_" + t2.getName(), setter.getName());
        } else {
            opt = AccessorInjector.prepare(getter.getDeclaringClass(), methodTemplateName + "Ref", newClassName, ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(getter.getDeclaringClass()), ClassTailor.toVMClassName(Ref.class), ClassTailor.toVMClassName(t2), "()" + ClassTailor.toVMTypeName(Ref.class), "()" + ClassTailor.toVMTypeName(t2), '(' + ClassTailor.toVMTypeName(Ref.class) + ")V", '(' + ClassTailor.toVMTypeName(t2) + ")V", "get_ref", getter.getName(), "set_ref", setter.getName());
        }
        if (opt == null) {
            return null;
        }
        Accessor<B, V> acc = instanciate(opt);
        if (acc != null && logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Using optimized Accessor for {0} and {1}", new Object[]{getter, setter});
        }
        return acc;
    }

    public static final <B, V> Accessor<B, V> get(Field field) {
        Class opt;
        int mods = field.getModifiers();
        if (Modifier.isPrivate(mods) || Modifier.isFinal(mods)) {
            return null;
        }
        String newClassName = ClassTailor.toVMClassName(field.getDeclaringClass()) + "$JaxbAccessorF_" + field.getName();
        if (field.getType().isPrimitive()) {
            opt = AccessorInjector.prepare(field.getDeclaringClass(), fieldTemplateName + RuntimeUtil.primitiveToBox.get(field.getType()).getSimpleName(), newClassName, ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(field.getDeclaringClass()), "f_" + field.getType().getName(), field.getName());
        } else {
            opt = AccessorInjector.prepare(field.getDeclaringClass(), fieldTemplateName + "Ref", newClassName, ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(field.getDeclaringClass()), ClassTailor.toVMClassName(Ref.class), ClassTailor.toVMClassName(field.getType()), ClassTailor.toVMTypeName(Ref.class), ClassTailor.toVMTypeName(field.getType()), "f_ref", field.getName());
        }
        if (opt == null) {
            return null;
        }
        Accessor<B, V> acc = instanciate(opt);
        if (acc != null && logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "Using optimized Accessor for {0}", field);
        }
        return acc;
    }

    private static <B, V> Accessor<B, V> instanciate(Class opt) {
        try {
            return (Accessor) opt.newInstance();
        } catch (IllegalAccessException e2) {
            logger.log(Level.INFO, "failed to load an optimized Accessor", (Throwable) e2);
            return null;
        } catch (InstantiationException e3) {
            logger.log(Level.INFO, "failed to load an optimized Accessor", (Throwable) e3);
            return null;
        } catch (SecurityException e4) {
            logger.log(Level.INFO, "failed to load an optimized Accessor", (Throwable) e4);
            return null;
        }
    }
}
