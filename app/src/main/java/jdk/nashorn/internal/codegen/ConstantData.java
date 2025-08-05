package jdk.nashorn.internal.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.nashorn.internal.runtime.PropertyMap;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/ConstantData.class */
final class ConstantData {
    final List<Object> constants = new ArrayList();
    final Map<String, Integer> stringMap = new HashMap();
    final Map<Object, Integer> objectMap = new HashMap();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ConstantData.class.desiredAssertionStatus();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/ConstantData$ArrayWrapper.class */
    private static class ArrayWrapper {
        private final Object array;
        private final int hashCode = calcHashCode();

        public ArrayWrapper(Object array) {
            this.array = array;
        }

        private int calcHashCode() {
            Class<?> cls = this.array.getClass();
            if (!cls.getComponentType().isPrimitive()) {
                return Arrays.hashCode((Object[]) this.array);
            }
            if (cls == double[].class) {
                return Arrays.hashCode((double[]) this.array);
            }
            if (cls == long[].class) {
                return Arrays.hashCode((long[]) this.array);
            }
            if (cls == int[].class) {
                return Arrays.hashCode((int[]) this.array);
            }
            throw new AssertionError((Object) ("ConstantData doesn't support " + ((Object) cls)));
        }

        public boolean equals(Object other) {
            if (!(other instanceof ArrayWrapper)) {
                return false;
            }
            Object otherArray = ((ArrayWrapper) other).array;
            if (this.array == otherArray) {
                return true;
            }
            Class<?> cls = this.array.getClass();
            if (cls == otherArray.getClass()) {
                if (!cls.getComponentType().isPrimitive()) {
                    return Arrays.equals((Object[]) this.array, (Object[]) otherArray);
                }
                if (cls == double[].class) {
                    return Arrays.equals((double[]) this.array, (double[]) otherArray);
                }
                if (cls == long[].class) {
                    return Arrays.equals((long[]) this.array, (long[]) otherArray);
                }
                if (cls == int[].class) {
                    return Arrays.equals((int[]) this.array, (int[]) otherArray);
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return this.hashCode;
        }
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/ConstantData$PropertyMapWrapper.class */
    private static class PropertyMapWrapper {
        private final PropertyMap propertyMap;
        private final int hashCode;

        public PropertyMapWrapper(PropertyMap map) {
            this.hashCode = Arrays.hashCode(map.getProperties()) + (31 * Objects.hashCode(map.getClassName()));
            this.propertyMap = map;
        }

        public int hashCode() {
            return this.hashCode;
        }

        public boolean equals(Object other) {
            if (!(other instanceof PropertyMapWrapper)) {
                return false;
            }
            PropertyMap otherMap = ((PropertyMapWrapper) other).propertyMap;
            return this.propertyMap == otherMap || (Arrays.equals(this.propertyMap.getProperties(), otherMap.getProperties()) && Objects.equals(this.propertyMap.getClassName(), otherMap.getClassName()));
        }
    }

    ConstantData() {
    }

    public int add(String string) {
        Integer value = this.stringMap.get(string);
        if (value != null) {
            return value.intValue();
        }
        this.constants.add(string);
        int index = this.constants.size() - 1;
        this.stringMap.put(string, Integer.valueOf(index));
        return index;
    }

    public int add(Object object) {
        Object entry;
        if (!$assertionsDisabled && object == null) {
            throw new AssertionError();
        }
        if (object.getClass().isArray()) {
            entry = new ArrayWrapper(object);
        } else if (object instanceof PropertyMap) {
            entry = new PropertyMapWrapper((PropertyMap) object);
        } else {
            entry = object;
        }
        Integer value = this.objectMap.get(entry);
        if (value != null) {
            return value.intValue();
        }
        this.constants.add(object);
        int index = this.constants.size() - 1;
        this.objectMap.put(entry, Integer.valueOf(index));
        return index;
    }

    Object[] toArray() {
        return this.constants.toArray();
    }
}
