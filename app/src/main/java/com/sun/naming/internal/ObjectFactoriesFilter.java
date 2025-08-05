package com.sun.naming.internal;

import sun.misc.ObjectInputFilter;
import sun.security.util.SecurityProperties;

/* loaded from: rt.jar:com/sun/naming/internal/ObjectFactoriesFilter.class */
public final class ObjectFactoriesFilter {
    private static final String FACTORIES_FILTER_PROPNAME = "jdk.jndi.object.factoriesFilter";
    private static final String DEFAULT_SP_VALUE = "*";
    private static final ObjectInputFilter GLOBAL = ObjectInputFilter.Config.createFilter(getFilterPropertyValue());

    public static boolean canInstantiateObjectsFactory(Class<?> cls) {
        return checkInput(() -> {
            return cls;
        });
    }

    private static boolean checkInput(FactoryInfo factoryInfo) {
        return GLOBAL.checkInput(factoryInfo) != ObjectInputFilter.Status.REJECTED;
    }

    @FunctionalInterface
    /* loaded from: rt.jar:com/sun/naming/internal/ObjectFactoriesFilter$FactoryInfo.class */
    private interface FactoryInfo extends ObjectInputFilter.FilterInfo {
        @Override // sun.misc.ObjectInputFilter.FilterInfo
        default long arrayLength() {
            return -1L;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        default long depth() {
            return 1L;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        default long references() {
            return 0L;
        }

        @Override // sun.misc.ObjectInputFilter.FilterInfo
        default long streamBytes() {
            return 0L;
        }
    }

    private ObjectFactoriesFilter() {
        throw new InternalError("Not instantiable");
    }

    private static String getFilterPropertyValue() {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable(FACTORIES_FILTER_PROPNAME);
        return strPrivilegedGetOverridable != null ? strPrivilegedGetOverridable : "*";
    }
}
