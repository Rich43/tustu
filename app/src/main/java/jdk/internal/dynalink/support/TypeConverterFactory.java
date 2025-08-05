package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.linker.ConversionComparator;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardedTypeConversion;
import jdk.internal.dynalink.linker.GuardingTypeConverterFactory;
import jdk.internal.dynalink.linker.MethodTypeConversionStrategy;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/TypeConverterFactory.class */
public class TypeConverterFactory {
    private final GuardingTypeConverterFactory[] factories;
    private final ConversionComparator[] comparators;
    private final MethodTypeConversionStrategy autoConversionStrategy;
    private final ClassValue<ClassMap<MethodHandle>> converterMap = new ClassValue<ClassMap<MethodHandle>>() { // from class: jdk.internal.dynalink.support.TypeConverterFactory.1
        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ ClassMap<MethodHandle> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected ClassMap<MethodHandle> computeValue(final Class<?> sourceType) {
            return new ClassMap<MethodHandle>(TypeConverterFactory.getClassLoader(sourceType)) { // from class: jdk.internal.dynalink.support.TypeConverterFactory.1.1
                @Override // jdk.internal.dynalink.support.ClassMap
                protected /* bridge */ /* synthetic */ MethodHandle computeValue(Class cls) {
                    return computeValue((Class<?>) cls);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // jdk.internal.dynalink.support.ClassMap
                protected MethodHandle computeValue(Class<?> targetType) {
                    try {
                        return TypeConverterFactory.this.createConverter(sourceType, targetType);
                    } catch (RuntimeException e2) {
                        throw e2;
                    } catch (Exception e3) {
                        throw new RuntimeException(e3);
                    }
                }
            };
        }
    };
    private final ClassValue<ClassMap<MethodHandle>> converterIdentityMap = new ClassValue<ClassMap<MethodHandle>>() { // from class: jdk.internal.dynalink.support.TypeConverterFactory.2
        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ ClassMap<MethodHandle> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected ClassMap<MethodHandle> computeValue(final Class<?> sourceType) {
            return new ClassMap<MethodHandle>(TypeConverterFactory.getClassLoader(sourceType)) { // from class: jdk.internal.dynalink.support.TypeConverterFactory.2.1
                @Override // jdk.internal.dynalink.support.ClassMap
                protected /* bridge */ /* synthetic */ MethodHandle computeValue(Class cls) {
                    return computeValue((Class<?>) cls);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // jdk.internal.dynalink.support.ClassMap
                protected MethodHandle computeValue(Class<?> targetType) {
                    MethodHandle converter;
                    if (!TypeConverterFactory.canAutoConvert(sourceType, targetType) && (converter = TypeConverterFactory.this.getCacheableTypeConverter(sourceType, targetType)) != TypeConverterFactory.IDENTITY_CONVERSION) {
                        return converter;
                    }
                    return TypeConverterFactory.IDENTITY_CONVERSION.asType(MethodType.methodType(targetType, (Class<?>) sourceType));
                }
            };
        }
    };
    private final ClassValue<ClassMap<Boolean>> canConvert = new ClassValue<ClassMap<Boolean>>() { // from class: jdk.internal.dynalink.support.TypeConverterFactory.3
        @Override // java.lang.ClassValue
        protected /* bridge */ /* synthetic */ ClassMap<Boolean> computeValue(Class cls) {
            return computeValue((Class<?>) cls);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ClassValue
        protected ClassMap<Boolean> computeValue(final Class<?> sourceType) {
            return new ClassMap<Boolean>(TypeConverterFactory.getClassLoader(sourceType)) { // from class: jdk.internal.dynalink.support.TypeConverterFactory.3.1
                @Override // jdk.internal.dynalink.support.ClassMap
                protected /* bridge */ /* synthetic */ Boolean computeValue(Class cls) {
                    return computeValue((Class<?>) cls);
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // jdk.internal.dynalink.support.ClassMap
                protected Boolean computeValue(Class<?> targetType) {
                    try {
                        return Boolean.valueOf(TypeConverterFactory.this.getTypeConverterNull(sourceType, targetType) != null);
                    } catch (RuntimeException e2) {
                        throw e2;
                    } catch (Exception e3) {
                        throw new RuntimeException(e3);
                    }
                }
            };
        }
    };
    static final MethodHandle IDENTITY_CONVERSION = MethodHandles.identity(Object.class);

    /* JADX INFO: Access modifiers changed from: private */
    public static final ClassLoader getClassLoader(final Class<?> clazz) {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: jdk.internal.dynalink.support.TypeConverterFactory.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return clazz.getClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
    }

    public TypeConverterFactory(Iterable<? extends GuardingTypeConverterFactory> factories, MethodTypeConversionStrategy autoConversionStrategy) {
        List<GuardingTypeConverterFactory> l2 = new LinkedList<>();
        List<ConversionComparator> c2 = new LinkedList<>();
        for (GuardingTypeConverterFactory factory : factories) {
            l2.add(factory);
            if (factory instanceof ConversionComparator) {
                c2.add((ConversionComparator) factory);
            }
        }
        this.factories = (GuardingTypeConverterFactory[]) l2.toArray(new GuardingTypeConverterFactory[l2.size()]);
        this.comparators = (ConversionComparator[]) c2.toArray(new ConversionComparator[c2.size()]);
        this.autoConversionStrategy = autoConversionStrategy;
    }

    public MethodHandle asType(MethodHandle handle, MethodType fromType) throws RuntimeException {
        MethodHandle converter;
        MethodHandle newHandle = handle;
        MethodType toType = newHandle.type();
        int l2 = toType.parameterCount();
        if (l2 != fromType.parameterCount()) {
            throw new WrongMethodTypeException("Parameter counts differ: " + ((Object) handle.type()) + " vs. " + ((Object) fromType));
        }
        int pos = 0;
        List<MethodHandle> converters = new LinkedList<>();
        for (int i2 = 0; i2 < l2; i2++) {
            Class<?> fromParamType = fromType.parameterType(i2);
            Class<?> toParamType = toType.parameterType(i2);
            if (canAutoConvert(fromParamType, toParamType)) {
                newHandle = applyConverters(newHandle, pos, converters);
            } else {
                MethodHandle converter2 = getTypeConverterNull(fromParamType, toParamType);
                if (converter2 != null) {
                    if (converters.isEmpty()) {
                        pos = i2;
                    }
                    converters.add(converter2);
                } else {
                    newHandle = applyConverters(newHandle, pos, converters);
                }
            }
        }
        MethodHandle newHandle2 = applyConverters(newHandle, pos, converters);
        Class<?> fromRetType = fromType.returnType();
        Class<?> toRetType = toType.returnType();
        if (fromRetType != Void.TYPE && toRetType != Void.TYPE && !canAutoConvert(toRetType, fromRetType) && (converter = getTypeConverterNull(toRetType, fromRetType)) != null) {
            newHandle2 = MethodHandles.filterReturnValue(newHandle2, converter);
        }
        MethodHandle autoConvertedHandle = this.autoConversionStrategy != null ? this.autoConversionStrategy.asType(newHandle2, fromType) : newHandle2;
        return autoConvertedHandle.asType(fromType);
    }

    private static MethodHandle applyConverters(MethodHandle handle, int pos, List<MethodHandle> converters) throws RuntimeException {
        if (converters.isEmpty()) {
            return handle;
        }
        MethodHandle newHandle = MethodHandles.filterArguments(handle, pos, (MethodHandle[]) converters.toArray(new MethodHandle[converters.size()]));
        converters.clear();
        return newHandle;
    }

    public boolean canConvert(Class<?> from, Class<?> to) {
        return canAutoConvert(from, to) || this.canConvert.get(from).get(to).booleanValue();
    }

    public ConversionComparator.Comparison compareConversion(Class<?> sourceType, Class<?> targetType1, Class<?> targetType2) {
        for (ConversionComparator comparator : this.comparators) {
            ConversionComparator.Comparison result = comparator.compareConversion(sourceType, targetType1, targetType2);
            if (result != ConversionComparator.Comparison.INDETERMINATE) {
                return result;
            }
        }
        if (TypeUtilities.isMethodInvocationConvertible(sourceType, targetType1)) {
            if (!TypeUtilities.isMethodInvocationConvertible(sourceType, targetType2)) {
                return ConversionComparator.Comparison.TYPE_1_BETTER;
            }
        } else if (TypeUtilities.isMethodInvocationConvertible(sourceType, targetType2)) {
            return ConversionComparator.Comparison.TYPE_2_BETTER;
        }
        return ConversionComparator.Comparison.INDETERMINATE;
    }

    static boolean canAutoConvert(Class<?> fromType, Class<?> toType) {
        return TypeUtilities.isMethodInvocationConvertible(fromType, toType);
    }

    MethodHandle getCacheableTypeConverterNull(Class<?> sourceType, Class<?> targetType) {
        MethodHandle converter = getCacheableTypeConverter(sourceType, targetType);
        if (converter == IDENTITY_CONVERSION) {
            return null;
        }
        return converter;
    }

    MethodHandle getTypeConverterNull(Class<?> sourceType, Class<?> targetType) {
        try {
            return getCacheableTypeConverterNull(sourceType, targetType);
        } catch (NotCacheableConverter e2) {
            return e2.converter;
        }
    }

    MethodHandle getCacheableTypeConverter(Class<?> sourceType, Class<?> targetType) {
        return this.converterMap.get(sourceType).get(targetType);
    }

    public MethodHandle getTypeConverter(Class<?> sourceType, Class<?> targetType) {
        try {
            return this.converterIdentityMap.get(sourceType).get(targetType);
        } catch (NotCacheableConverter e2) {
            return e2.converter;
        }
    }

    MethodHandle createConverter(Class<?> sourceType, Class<?> targetType) throws Exception {
        MethodType type = MethodType.methodType(targetType, sourceType);
        MethodHandle identity = IDENTITY_CONVERSION.asType(type);
        MethodHandle last = identity;
        boolean cacheable = true;
        int i2 = this.factories.length;
        while (true) {
            int i3 = i2;
            i2--;
            if (i3 <= 0) {
                break;
            }
            GuardedTypeConversion next = this.factories[i2].convertToType(sourceType, targetType);
            if (next != null) {
                cacheable = cacheable && next.isCacheable();
                GuardedInvocation conversionInvocation = next.getConversionInvocation();
                conversionInvocation.assertType(type);
                last = conversionInvocation.compose(last);
            }
        }
        if (last == identity) {
            return IDENTITY_CONVERSION;
        }
        if (cacheable) {
            return last;
        }
        throw new NotCacheableConverter(last);
    }

    /* loaded from: nashorn.jar:jdk/internal/dynalink/support/TypeConverterFactory$NotCacheableConverter.class */
    private static class NotCacheableConverter extends RuntimeException {
        final MethodHandle converter;

        NotCacheableConverter(MethodHandle converter) {
            super("", null, false, false);
            this.converter = converter;
        }
    }
}
