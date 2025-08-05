package javax.management.openmbean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:javax/management/openmbean/CompositeDataSupport.class */
public class CompositeDataSupport implements CompositeData, Serializable {
    static final long serialVersionUID = 8003518976613702244L;
    private final SortedMap<String, Object> contents;
    private final CompositeType compositeType;

    public CompositeDataSupport(CompositeType compositeType, String[] strArr, Object[] objArr) throws OpenDataException {
        this(makeMap(strArr, objArr), compositeType);
    }

    private static SortedMap<String, Object> makeMap(String[] strArr, Object[] objArr) throws OpenDataException {
        if (strArr == null || objArr == null) {
            throw new IllegalArgumentException("Null itemNames or itemValues");
        }
        if (strArr.length == 0 || objArr.length == 0) {
            throw new IllegalArgumentException("Empty itemNames or itemValues");
        }
        if (strArr.length != objArr.length) {
            throw new IllegalArgumentException("Different lengths: itemNames[" + strArr.length + "], itemValues[" + objArr.length + "]");
        }
        TreeMap treeMap = new TreeMap();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str == null || str.equals("")) {
                throw new IllegalArgumentException("Null or empty item name");
            }
            if (treeMap.containsKey(str)) {
                throw new OpenDataException("Duplicate item name " + str);
            }
            treeMap.put(strArr[i2], objArr[i2]);
        }
        return treeMap;
    }

    public CompositeDataSupport(CompositeType compositeType, Map<String, ?> map) throws OpenDataException {
        this(makeMap(map), compositeType);
    }

    private static SortedMap<String, Object> makeMap(Map<String, ?> map) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException("Null or empty items map");
        }
        TreeMap treeMap = new TreeMap();
        for (String str : map.keySet()) {
            if (str == null || str.equals("")) {
                throw new IllegalArgumentException("Null or empty item name");
            }
            if (!(str instanceof String)) {
                throw new ArrayStoreException("Item name is not string: " + ((Object) str));
            }
            treeMap.put(str, map.get(str));
        }
        return treeMap;
    }

    private CompositeDataSupport(SortedMap<String, Object> sortedMap, CompositeType compositeType) throws OpenDataException {
        if (compositeType == null) {
            throw new IllegalArgumentException("Argument compositeType cannot be null.");
        }
        Set<String> setKeySet = compositeType.keySet();
        Set<String> setKeySet2 = sortedMap.keySet();
        if (!setKeySet.equals(setKeySet2)) {
            TreeSet treeSet = new TreeSet(setKeySet);
            treeSet.removeAll(setKeySet2);
            TreeSet treeSet2 = new TreeSet(setKeySet2);
            treeSet2.removeAll(setKeySet);
            if (!treeSet.isEmpty() || !treeSet2.isEmpty()) {
                throw new OpenDataException("Item names do not match CompositeType: names in items but not in CompositeType: " + ((Object) treeSet2) + "; names in CompositeType but not in items: " + ((Object) treeSet));
            }
        }
        for (String str : setKeySet) {
            Object obj = sortedMap.get(str);
            if (obj != null) {
                OpenType<?> type = compositeType.getType(str);
                if (!type.isValue(obj)) {
                    throw new OpenDataException("Argument value of wrong type for item " + str + ": value " + obj + ", type " + ((Object) type));
                }
            }
        }
        this.compositeType = compositeType;
        this.contents = sortedMap;
    }

    @Override // javax.management.openmbean.CompositeData
    public CompositeType getCompositeType() {
        return this.compositeType;
    }

    @Override // javax.management.openmbean.CompositeData
    public Object get(String str) {
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument key cannot be a null or empty String.");
        }
        if (!this.contents.containsKey(str.trim())) {
            throw new InvalidKeyException("Argument key=\"" + str.trim() + "\" is not an existing item name for this CompositeData instance.");
        }
        return this.contents.get(str.trim());
    }

    @Override // javax.management.openmbean.CompositeData
    public Object[] getAll(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            return new Object[0];
        }
        Object[] objArr = new Object[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            objArr[i2] = get(strArr[i2]);
        }
        return objArr;
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean containsKey(String str) {
        if (str == null || str.trim().equals("")) {
            return false;
        }
        return this.contents.containsKey(str);
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean containsValue(Object obj) {
        return this.contents.containsValue(obj);
    }

    @Override // javax.management.openmbean.CompositeData
    public Collection<?> values() {
        return Collections.unmodifiableCollection(this.contents.values());
    }

    @Override // javax.management.openmbean.CompositeData
    public boolean equals(Object obj) {
        boolean zEquals;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CompositeData)) {
            return false;
        }
        CompositeData compositeData = (CompositeData) obj;
        if (!getCompositeType().equals(compositeData.getCompositeType()) || this.contents.size() != compositeData.values().size()) {
            return false;
        }
        for (Map.Entry<String, Object> entry : this.contents.entrySet()) {
            Object value = entry.getValue();
            Object obj2 = compositeData.get(entry.getKey());
            if (value != obj2) {
                if (value == null) {
                    return false;
                }
                if (value.getClass().isArray()) {
                    zEquals = Arrays.deepEquals(new Object[]{value}, new Object[]{obj2});
                } else {
                    zEquals = value.equals(obj2);
                }
                if (!zEquals) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // javax.management.openmbean.CompositeData
    public int hashCode() {
        int iHashCode = this.compositeType.hashCode();
        for (Object obj : this.contents.values()) {
            if (obj instanceof Object[]) {
                iHashCode += Arrays.deepHashCode((Object[]) obj);
            } else if (obj instanceof byte[]) {
                iHashCode += Arrays.hashCode((byte[]) obj);
            } else if (obj instanceof short[]) {
                iHashCode += Arrays.hashCode((short[]) obj);
            } else if (obj instanceof int[]) {
                iHashCode += Arrays.hashCode((int[]) obj);
            } else if (obj instanceof long[]) {
                iHashCode += Arrays.hashCode((long[]) obj);
            } else if (obj instanceof char[]) {
                iHashCode += Arrays.hashCode((char[]) obj);
            } else if (obj instanceof float[]) {
                iHashCode += Arrays.hashCode((float[]) obj);
            } else if (obj instanceof double[]) {
                iHashCode += Arrays.hashCode((double[]) obj);
            } else if (obj instanceof boolean[]) {
                iHashCode += Arrays.hashCode((boolean[]) obj);
            } else if (obj != null) {
                iHashCode += obj.hashCode();
            }
        }
        return iHashCode;
    }

    @Override // javax.management.openmbean.CompositeData
    public String toString() {
        return getClass().getName() + "(compositeType=" + this.compositeType.toString() + ",contents=" + contentString() + ")";
    }

    private String contentString() {
        StringBuilder sb = new StringBuilder(VectorFormat.DEFAULT_PREFIX);
        String str = "";
        for (Map.Entry<String, Object> entry : this.contents.entrySet()) {
            sb.append(str).append(entry.getKey()).append("=");
            String strDeepToString = Arrays.deepToString(new Object[]{entry.getValue()});
            sb.append(strDeepToString.substring(1, strDeepToString.length() - 1));
            str = ", ";
        }
        sb.append("}");
        return sb.toString();
    }
}
