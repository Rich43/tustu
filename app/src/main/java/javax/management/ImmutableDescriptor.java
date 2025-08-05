package javax.management;

import com.sun.jmx.mbeanserver.Util;
import java.io.InvalidObjectException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:javax/management/ImmutableDescriptor.class */
public class ImmutableDescriptor implements Descriptor {
    private static final long serialVersionUID = 8853308591080540165L;
    private final String[] names;
    private final Object[] values;
    private transient int hashCode;
    public static final ImmutableDescriptor EMPTY_DESCRIPTOR = new ImmutableDescriptor(new String[0]);

    public ImmutableDescriptor(String[] strArr, Object[] objArr) {
        this(makeMap(strArr, objArr));
    }

    public ImmutableDescriptor(String... strArr) {
        this(makeMap(strArr));
    }

    public ImmutableDescriptor(Map<String, ?> map) {
        this.hashCode = -1;
        if (map == null) {
            throw new IllegalArgumentException("Null Map");
        }
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            if (key == null || key.equals("")) {
                throw new IllegalArgumentException("Empty or null field name");
            }
            if (treeMap.containsKey(key)) {
                throw new IllegalArgumentException("Duplicate name: " + key);
            }
            treeMap.put(key, entry.getValue());
        }
        int size = treeMap.size();
        this.names = (String[]) treeMap.keySet().toArray(new String[size]);
        this.values = treeMap.values().toArray(new Object[size]);
    }

    private Object readResolve() throws InvalidObjectException {
        boolean z2 = false;
        if (this.names == null || this.values == null || this.names.length != this.values.length) {
            z2 = true;
        }
        if (!z2) {
            if (this.names.length == 0 && getClass() == ImmutableDescriptor.class) {
                return EMPTY_DESCRIPTOR;
            }
            Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
            String str = "";
            for (int i2 = 0; i2 < this.names.length; i2++) {
                if (this.names[i2] == null || comparator.compare(str, this.names[i2]) >= 0) {
                    z2 = true;
                    break;
                }
                str = this.names[i2];
            }
        }
        if (z2) {
            throw new InvalidObjectException("Bad names or values");
        }
        return this;
    }

    private static SortedMap<String, ?> makeMap(String[] strArr, Object[] objArr) {
        if (strArr == null || objArr == null) {
            throw new IllegalArgumentException("Null array parameter");
        }
        if (strArr.length != objArr.length) {
            throw new IllegalArgumentException("Different size arrays");
        }
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str == null || str.equals("")) {
                throw new IllegalArgumentException("Empty or null field name");
            }
            if (treeMap.put(str, objArr[i2]) != 0) {
                throw new IllegalArgumentException("Duplicate field name: " + str);
            }
        }
        return treeMap;
    }

    private static SortedMap<String, ?> makeMap(String[] strArr) {
        if (strArr == null) {
            throw new IllegalArgumentException("Null fields parameter");
        }
        String[] strArr2 = new String[strArr.length];
        String[] strArr3 = new String[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            int iIndexOf = str.indexOf(61);
            if (iIndexOf < 0) {
                throw new IllegalArgumentException("Missing = character: " + str);
            }
            strArr2[i2] = str.substring(0, iIndexOf);
            strArr3[i2] = str.substring(iIndexOf + 1);
        }
        return makeMap(strArr2, strArr3);
    }

    public static ImmutableDescriptor union(Descriptor... descriptorArr) throws RuntimeOperationsException {
        String[] fieldNames;
        boolean zEquals;
        int iFindNonEmpty = findNonEmpty(descriptorArr, 0);
        if (iFindNonEmpty < 0) {
            return EMPTY_DESCRIPTOR;
        }
        if ((descriptorArr[iFindNonEmpty] instanceof ImmutableDescriptor) && findNonEmpty(descriptorArr, iFindNonEmpty + 1) < 0) {
            return (ImmutableDescriptor) descriptorArr[iFindNonEmpty];
        }
        TreeMap treeMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
        ImmutableDescriptor immutableDescriptor = EMPTY_DESCRIPTOR;
        for (Descriptor descriptor : descriptorArr) {
            if (descriptor != null) {
                if (descriptor instanceof ImmutableDescriptor) {
                    ImmutableDescriptor immutableDescriptor2 = (ImmutableDescriptor) descriptor;
                    fieldNames = immutableDescriptor2.names;
                    if (immutableDescriptor2.getClass() == ImmutableDescriptor.class && fieldNames.length > immutableDescriptor.names.length) {
                        immutableDescriptor = immutableDescriptor2;
                    }
                } else {
                    fieldNames = descriptor.getFieldNames();
                }
                for (String str : fieldNames) {
                    Object fieldValue = descriptor.getFieldValue(str);
                    Object objPut = treeMap.put(str, fieldValue);
                    if (objPut != null) {
                        if (objPut.getClass().isArray()) {
                            zEquals = Arrays.deepEquals(new Object[]{objPut}, new Object[]{fieldValue});
                        } else {
                            zEquals = objPut.equals(fieldValue);
                        }
                        if (!zEquals) {
                            throw new IllegalArgumentException("Inconsistent values for descriptor field " + str + ": " + objPut + " :: " + fieldValue);
                        }
                    }
                }
            }
        }
        if (immutableDescriptor.names.length == treeMap.size()) {
            return immutableDescriptor;
        }
        return new ImmutableDescriptor(treeMap);
    }

    private static boolean isEmpty(Descriptor descriptor) {
        if (descriptor == null) {
            return true;
        }
        return descriptor instanceof ImmutableDescriptor ? ((ImmutableDescriptor) descriptor).names.length == 0 : descriptor.getFieldNames().length == 0;
    }

    private static int findNonEmpty(Descriptor[] descriptorArr, int i2) {
        for (int i3 = i2; i3 < descriptorArr.length; i3++) {
            if (!isEmpty(descriptorArr[i3])) {
                return i3;
            }
        }
        return -1;
    }

    private int fieldIndex(String str) {
        return Arrays.binarySearch(this.names, str, String.CASE_INSENSITIVE_ORDER);
    }

    @Override // javax.management.Descriptor
    public final Object getFieldValue(String str) throws IllegalArgumentException, NegativeArraySizeException {
        checkIllegalFieldName(str);
        int iFieldIndex = fieldIndex(str);
        if (iFieldIndex < 0) {
            return null;
        }
        Object obj = this.values[iFieldIndex];
        if (obj == null || !obj.getClass().isArray()) {
            return obj;
        }
        if (obj instanceof Object[]) {
            return ((Object[]) obj).clone();
        }
        int length = Array.getLength(obj);
        Object objNewInstance = Array.newInstance(obj.getClass().getComponentType(), length);
        System.arraycopy(obj, 0, objNewInstance, 0, length);
        return objNewInstance;
    }

    @Override // javax.management.Descriptor
    public final String[] getFields() {
        String[] strArr = new String[this.names.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Object obj = this.values[i2];
            if (obj == null) {
                obj = "";
            } else if (!(obj instanceof String)) {
                obj = "(" + obj + ")";
            }
            strArr[i2] = this.names[i2] + "=" + obj;
        }
        return strArr;
    }

    @Override // javax.management.Descriptor
    public final Object[] getFieldValues(String... strArr) {
        if (strArr == null) {
            return (Object[]) this.values.clone();
        }
        Object[] objArr = new Object[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            String str = strArr[i2];
            if (str != null && !str.equals("")) {
                objArr[i2] = getFieldValue(str);
            }
        }
        return objArr;
    }

    @Override // javax.management.Descriptor
    public final String[] getFieldNames() {
        return (String[]) this.names.clone();
    }

    @Override // javax.management.Descriptor
    public boolean equals(Object obj) {
        String[] fieldNames;
        Object[] fieldValues;
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Descriptor)) {
            return false;
        }
        if (obj instanceof ImmutableDescriptor) {
            fieldNames = ((ImmutableDescriptor) obj).names;
        } else {
            fieldNames = ((Descriptor) obj).getFieldNames();
            Arrays.sort(fieldNames, String.CASE_INSENSITIVE_ORDER);
        }
        if (this.names.length != fieldNames.length) {
            return false;
        }
        for (int i2 = 0; i2 < this.names.length; i2++) {
            if (!this.names[i2].equalsIgnoreCase(fieldNames[i2])) {
                return false;
            }
        }
        if (obj instanceof ImmutableDescriptor) {
            fieldValues = ((ImmutableDescriptor) obj).values;
        } else {
            fieldValues = ((Descriptor) obj).getFieldValues(fieldNames);
        }
        return Arrays.deepEquals(this.values, fieldValues);
    }

    @Override // javax.management.Descriptor
    public int hashCode() {
        if (this.hashCode == -1) {
            this.hashCode = Util.hashCode(this.names, this.values);
        }
        return this.hashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(VectorFormat.DEFAULT_PREFIX);
        for (int i2 = 0; i2 < this.names.length; i2++) {
            if (i2 > 0) {
                sb.append(", ");
            }
            sb.append(this.names[i2]).append("=");
            Object objSubstring = this.values[i2];
            if (objSubstring != null && objSubstring.getClass().isArray()) {
                String strDeepToString = Arrays.deepToString(new Object[]{objSubstring});
                objSubstring = strDeepToString.substring(1, strDeepToString.length() - 1);
            }
            sb.append(String.valueOf(objSubstring));
        }
        return sb.append("}").toString();
    }

    @Override // javax.management.Descriptor
    public boolean isValid() {
        return true;
    }

    @Override // javax.management.Descriptor
    public Descriptor clone() {
        return this;
    }

    @Override // javax.management.Descriptor
    public final void setFields(String[] strArr, Object[] objArr) throws RuntimeOperationsException {
        if (strArr == null || objArr == null) {
            illegal("Null argument");
        }
        if (strArr.length != objArr.length) {
            illegal("Different array sizes");
        }
        for (String str : strArr) {
            checkIllegalFieldName(str);
        }
        for (int i2 = 0; i2 < strArr.length; i2++) {
            setField(strArr[i2], objArr[i2]);
        }
    }

    @Override // javax.management.Descriptor
    public final void setField(String str, Object obj) throws RuntimeOperationsException {
        checkIllegalFieldName(str);
        int iFieldIndex = fieldIndex(str);
        if (iFieldIndex < 0) {
            unsupported();
        }
        Object obj2 = this.values[iFieldIndex];
        if (obj2 == null) {
            if (obj == null) {
                return;
            }
        } else if (obj2.equals(obj)) {
            return;
        }
        unsupported();
    }

    @Override // javax.management.Descriptor
    public final void removeField(String str) {
        if (str != null && fieldIndex(str) >= 0) {
            unsupported();
        }
    }

    static Descriptor nonNullDescriptor(Descriptor descriptor) {
        if (descriptor == null) {
            return EMPTY_DESCRIPTOR;
        }
        return descriptor;
    }

    private static void checkIllegalFieldName(String str) {
        if (str == null || str.equals("")) {
            illegal("Null or empty field name");
        }
    }

    private static void unsupported() {
        throw new RuntimeOperationsException(new UnsupportedOperationException("Descriptor is read-only"));
    }

    private static void illegal(String str) {
        throw new RuntimeOperationsException(new IllegalArgumentException(str));
    }
}
