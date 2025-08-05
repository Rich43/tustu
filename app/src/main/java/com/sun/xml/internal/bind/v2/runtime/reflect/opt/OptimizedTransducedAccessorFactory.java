package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.Util;
import com.sun.xml.internal.bind.v2.bytecode.ClassTailor;
import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/OptimizedTransducedAccessorFactory.class */
public abstract class OptimizedTransducedAccessorFactory {
    private static final Logger logger = Util.getClassLogger();
    private static final String fieldTemplateName;
    private static final String methodTemplateName;
    private static final Map<Class, String> suffixMap;

    private OptimizedTransducedAccessorFactory() {
    }

    static {
        String s2 = TransducedAccessor_field_Byte.class.getName();
        fieldTemplateName = s2.substring(0, s2.length() - "Byte".length()).replace('.', '/');
        String s3 = TransducedAccessor_method_Byte.class.getName();
        methodTemplateName = s3.substring(0, s3.length() - "Byte".length()).replace('.', '/');
        suffixMap = new HashMap();
        suffixMap.put(Byte.TYPE, "Byte");
        suffixMap.put(Short.TYPE, "Short");
        suffixMap.put(Integer.TYPE, "Integer");
        suffixMap.put(Long.TYPE, "Long");
        suffixMap.put(Boolean.TYPE, "Boolean");
        suffixMap.put(Float.TYPE, "Float");
        suffixMap.put(Double.TYPE, "Double");
    }

    public static final TransducedAccessor get(RuntimePropertyInfo prop) {
        Accessor acc = prop.getAccessor();
        Class opt = null;
        TypeInfo<Type, Class> parent = prop.parent();
        if (!(parent instanceof RuntimeClassInfo)) {
            return null;
        }
        Class dc = ((RuntimeClassInfo) parent).getClazz();
        String newClassName = ClassTailor.toVMClassName(dc) + "_JaxbXducedAccessor_" + prop.getName();
        if (acc instanceof Accessor.FieldReflection) {
            Accessor.FieldReflection racc = (Accessor.FieldReflection) acc;
            Field field = racc.f12068f;
            int mods = field.getModifiers();
            if (Modifier.isPrivate(mods) || Modifier.isFinal(mods)) {
                return null;
            }
            Class<?> t2 = field.getType();
            if (t2.isPrimitive()) {
                opt = AccessorInjector.prepare(dc, fieldTemplateName + suffixMap.get(t2), newClassName, ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(dc), "f_" + t2.getName(), field.getName());
            }
        }
        if (acc.getClass() == Accessor.GetterSetterReflection.class) {
            Accessor.GetterSetterReflection gacc = (Accessor.GetterSetterReflection) acc;
            if (gacc.getter == null || gacc.setter == null) {
                return null;
            }
            Class<?> t3 = gacc.getter.getReturnType();
            if (Modifier.isPrivate(gacc.getter.getModifiers()) || Modifier.isPrivate(gacc.setter.getModifiers())) {
                return null;
            }
            if (t3.isPrimitive()) {
                opt = AccessorInjector.prepare(dc, methodTemplateName + suffixMap.get(t3), newClassName, ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(dc), "get_" + t3.getName(), gacc.getter.getName(), "set_" + t3.getName(), gacc.setter.getName());
            }
        }
        if (opt == null) {
            return null;
        }
        logger.log(Level.FINE, "Using optimized TransducedAccessor for " + prop.displayName());
        try {
            return (TransducedAccessor) opt.newInstance();
        } catch (IllegalAccessException e2) {
            logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", (Throwable) e2);
            return null;
        } catch (InstantiationException e3) {
            logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", (Throwable) e3);
            return null;
        } catch (SecurityException e4) {
            logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", (Throwable) e4);
            return null;
        }
    }
}
