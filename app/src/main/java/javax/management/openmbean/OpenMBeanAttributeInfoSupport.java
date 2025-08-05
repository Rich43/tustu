package javax.management.openmbean;

import com.sun.jmx.remote.util.EnvHelp;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.management.Descriptor;
import javax.management.DescriptorRead;
import javax.management.ImmutableDescriptor;
import javax.management.JMX;
import javax.management.MBeanAttributeInfo;
import javax.management.RuntimeOperationsException;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:javax/management/openmbean/OpenMBeanAttributeInfoSupport.class */
public class OpenMBeanAttributeInfoSupport extends MBeanAttributeInfo implements OpenMBeanAttributeInfo {
    static final long serialVersionUID = -4867215622149721849L;
    private OpenType<?> openType;
    private final Object defaultValue;
    private final Set<?> legalValues;
    private final Comparable<?> minValue;
    private final Comparable<?> maxValue;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanAttributeInfoSupport(String str, String str2, OpenType<?> openType, boolean z2, boolean z3, boolean z4) {
        this(str, str2, openType, z2, z3, z4, (Descriptor) null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public OpenMBeanAttributeInfoSupport(String str, String str2, OpenType<?> openType, boolean z2, boolean z3, boolean z4, Descriptor descriptor) {
        String className = openType == null ? null : openType.getClassName();
        Descriptor[] descriptorArr = new Descriptor[2];
        descriptorArr[0] = descriptor;
        descriptorArr[1] = openType == null ? null : openType.getDescriptor();
        super(str, className, str2, z2, z3, z4, ImmutableDescriptor.union(descriptorArr));
        this.myHashCode = null;
        this.myToString = null;
        this.openType = openType;
        Descriptor descriptor2 = getDescriptor();
        this.defaultValue = valueFrom(descriptor2, JMX.DEFAULT_VALUE_FIELD, openType);
        this.legalValues = valuesFrom(descriptor2, JMX.LEGAL_VALUES_FIELD, openType);
        this.minValue = comparableValueFrom(descriptor2, JMX.MIN_VALUE_FIELD, openType);
        this.maxValue = comparableValueFrom(descriptor2, JMX.MAX_VALUE_FIELD, openType);
        try {
            check(this);
        } catch (OpenDataException e2) {
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    public <T> OpenMBeanAttributeInfoSupport(String str, String str2, OpenType<T> openType, boolean z2, boolean z3, boolean z4, T t2) throws OpenDataException {
        this(str, str2, openType, z2, z3, z4, t2, (Object[]) null);
    }

    public <T> OpenMBeanAttributeInfoSupport(String str, String str2, OpenType<T> openType, boolean z2, boolean z3, boolean z4, T t2, T[] tArr) throws OpenDataException {
        this(str, str2, openType, z2, z3, z4, t2, tArr, null, null);
    }

    public <T> OpenMBeanAttributeInfoSupport(String str, String str2, OpenType<T> openType, boolean z2, boolean z3, boolean z4, T t2, Comparable<T> comparable, Comparable<T> comparable2) throws OpenDataException {
        this(str, str2, openType, z2, z3, z4, t2, null, comparable, comparable2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> OpenMBeanAttributeInfoSupport(String str, String str2, OpenType<T> openType, boolean z2, boolean z3, boolean z4, T t2, T[] tArr, Comparable<T> comparable, Comparable<T> comparable2) throws OpenDataException {
        super(str, openType == 0 ? null : openType.getClassName(), str2, z2, z3, z4, makeDescriptor(openType, t2, tArr, comparable, comparable2));
        this.myHashCode = null;
        this.myToString = null;
        this.openType = openType;
        Descriptor descriptor = getDescriptor();
        this.defaultValue = t2;
        this.minValue = comparable;
        this.maxValue = comparable2;
        this.legalValues = (Set) descriptor.getFieldValue(JMX.LEGAL_VALUES_FIELD);
        check(this);
    }

    private Object readResolve() {
        if (getDescriptor().getFieldNames().length == 0) {
            return new OpenMBeanAttributeInfoSupport(this.name, this.description, this.openType, isReadable(), isWritable(), isIs(), makeDescriptor((OpenType<Object>) cast(this.openType), this.defaultValue, (Set<Object>) cast(this.legalValues), (Comparable<Object>) cast(this.minValue), (Comparable<Object>) cast(this.maxValue)));
        }
        return this;
    }

    static void check(OpenMBeanParameterInfo openMBeanParameterInfo) throws OpenDataException {
        OpenType<?> openType = openMBeanParameterInfo.getOpenType();
        if (openType == null) {
            throw new IllegalArgumentException("OpenType cannot be null");
        }
        if (openMBeanParameterInfo.getName() == null || openMBeanParameterInfo.getName().trim().equals("")) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (openMBeanParameterInfo.getDescription() == null || openMBeanParameterInfo.getDescription().trim().equals("")) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (openMBeanParameterInfo.hasDefaultValue()) {
            if (openType.isArray() || (openType instanceof TabularType)) {
                throw new OpenDataException("Default value not supported for ArrayType and TabularType");
            }
            if (!openType.isValue(openMBeanParameterInfo.getDefaultValue())) {
                throw new OpenDataException("Argument defaultValue's class [\"" + openMBeanParameterInfo.getDefaultValue().getClass().getName() + "\"] does not match the one defined in openType[\"" + openType.getClassName() + "\"]");
            }
        }
        if (openMBeanParameterInfo.hasLegalValues() && (openMBeanParameterInfo.hasMinValue() || openMBeanParameterInfo.hasMaxValue())) {
            throw new OpenDataException("cannot have both legalValue and minValue or maxValue");
        }
        if (openMBeanParameterInfo.hasMinValue() && !openType.isValue(openMBeanParameterInfo.getMinValue())) {
            throw new OpenDataException("Type of minValue [" + openMBeanParameterInfo.getMinValue().getClass().getName() + "] does not match OpenType [" + openType.getClassName() + "]");
        }
        if (openMBeanParameterInfo.hasMaxValue() && !openType.isValue(openMBeanParameterInfo.getMaxValue())) {
            throw new OpenDataException("Type of maxValue [" + openMBeanParameterInfo.getMaxValue().getClass().getName() + "] does not match OpenType [" + openType.getClassName() + "]");
        }
        if (openMBeanParameterInfo.hasDefaultValue()) {
            Object defaultValue = openMBeanParameterInfo.getDefaultValue();
            if (openMBeanParameterInfo.hasLegalValues() && !openMBeanParameterInfo.getLegalValues().contains(defaultValue)) {
                throw new OpenDataException("defaultValue is not contained in legalValues");
            }
            if (openMBeanParameterInfo.hasMinValue() && compare(openMBeanParameterInfo.getMinValue(), defaultValue) > 0) {
                throw new OpenDataException("minValue cannot be greater than defaultValue");
            }
            if (openMBeanParameterInfo.hasMaxValue() && compare(openMBeanParameterInfo.getMaxValue(), defaultValue) < 0) {
                throw new OpenDataException("maxValue cannot be less than defaultValue");
            }
        }
        if (openMBeanParameterInfo.hasLegalValues()) {
            if ((openType instanceof TabularType) || openType.isArray()) {
                throw new OpenDataException("Legal values not supported for TabularType and arrays");
            }
            for (Object obj : openMBeanParameterInfo.getLegalValues()) {
                if (!openType.isValue(obj)) {
                    throw new OpenDataException("Element of legalValues [" + obj + "] is not a valid value for the specified openType [" + openType.toString() + "]");
                }
            }
        }
        if (openMBeanParameterInfo.hasMinValue() && openMBeanParameterInfo.hasMaxValue() && compare(openMBeanParameterInfo.getMinValue(), openMBeanParameterInfo.getMaxValue()) > 0) {
            throw new OpenDataException("minValue cannot be greater than maxValue");
        }
    }

    static int compare(Object obj, Object obj2) {
        return ((Comparable) obj).compareTo(obj2);
    }

    static <T> Descriptor makeDescriptor(OpenType<T> openType, T t2, T[] tArr, Comparable<T> comparable, Comparable<T> comparable2) {
        HashMap map = new HashMap();
        if (t2 != null) {
            map.put(JMX.DEFAULT_VALUE_FIELD, t2);
        }
        if (tArr != null) {
            HashSet hashSet = new HashSet();
            for (T t3 : tArr) {
                hashSet.add(t3);
            }
            map.put(JMX.LEGAL_VALUES_FIELD, Collections.unmodifiableSet(hashSet));
        }
        if (comparable != null) {
            map.put(JMX.MIN_VALUE_FIELD, comparable);
        }
        if (comparable2 != null) {
            map.put(JMX.MAX_VALUE_FIELD, comparable2);
        }
        if (map.isEmpty()) {
            return openType.getDescriptor();
        }
        map.put(JMX.OPEN_TYPE_FIELD, openType);
        return new ImmutableDescriptor(map);
    }

    static <T> Descriptor makeDescriptor(OpenType<T> openType, T t2, Set<T> set, Comparable<T> comparable, Comparable<T> comparable2) {
        Object[] objArr;
        if (set == null) {
            objArr = null;
        } else {
            objArr = (Object[]) cast(new Object[set.size()]);
            set.toArray(objArr);
        }
        return makeDescriptor(openType, t2, objArr, comparable, comparable2);
    }

    static <T> T valueFrom(Descriptor descriptor, String str, OpenType<T> openType) throws RuntimeOperationsException {
        Object fieldValue = descriptor.getFieldValue(str);
        if (fieldValue == null) {
            return null;
        }
        try {
            return (T) convertFrom(fieldValue, openType);
        } catch (Exception e2) {
            throw ((IllegalArgumentException) EnvHelp.initCause(new IllegalArgumentException("Cannot convert descriptor field " + str + "  to " + openType.getTypeName()), e2));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T> Set<T> valuesFrom(Descriptor descriptor, String str, OpenType<T> openType) throws RuntimeOperationsException {
        List listAsList;
        Object fieldValue = descriptor.getFieldValue(str);
        if (fieldValue == null) {
            return null;
        }
        if (fieldValue instanceof Set) {
            Set set = (Set) fieldValue;
            boolean z2 = true;
            Iterator it = set.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                if (!openType.isValue(it.next())) {
                    z2 = false;
                    break;
                }
            }
            if (z2) {
                return (Set) cast(set);
            }
            listAsList = set;
        } else if (fieldValue instanceof Object[]) {
            listAsList = Arrays.asList((Object[]) fieldValue);
        } else {
            throw new IllegalArgumentException("Descriptor value for " + str + " must be a Set or an array: " + fieldValue.getClass().getName());
        }
        HashSet hashSet = new HashSet();
        Iterator<E> it2 = listAsList.iterator();
        while (it2.hasNext()) {
            hashSet.add(convertFrom(it2.next(), openType));
        }
        return hashSet;
    }

    static <T> Comparable<?> comparableValueFrom(Descriptor descriptor, String str, OpenType<T> openType) throws RuntimeOperationsException {
        Object objValueFrom = valueFrom(descriptor, str, openType);
        if (objValueFrom == null || (objValueFrom instanceof Comparable)) {
            return (Comparable) objValueFrom;
        }
        throw new IllegalArgumentException("Descriptor field " + str + " with value " + objValueFrom + " is not Comparable");
    }

    private static <T> T convertFrom(Object obj, OpenType<T> openType) {
        if (openType.isValue(obj)) {
            return (T) cast(obj);
        }
        return (T) convertFromStrings(obj, openType);
    }

    private static <T> T convertFromStrings(Object obj, OpenType<T> openType) {
        if (openType instanceof ArrayType) {
            return (T) convertFromStringArray(obj, openType);
        }
        if (obj instanceof String) {
            return (T) convertFromString((String) obj, openType);
        }
        throw new IllegalArgumentException("Cannot convert value " + obj + " of type " + obj.getClass().getName() + " to type " + openType.getTypeName());
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static <T> T convertFromString(java.lang.String r8, javax.management.openmbean.OpenType<T> r9) throws java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 262
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.management.openmbean.OpenMBeanAttributeInfoSupport.convertFromString(java.lang.String, javax.management.openmbean.OpenType):java.lang.Object");
    }

    private static <T> T convertFromStringArray(Object obj, OpenType<T> openType) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        OpenType<?> arrayType;
        ArrayType arrayType2 = (ArrayType) openType;
        OpenType<?> elementOpenType = arrayType2.getElementOpenType();
        int dimension = arrayType2.getDimension();
        String str = "[";
        for (int i2 = 1; i2 < dimension; i2++) {
            str = str + "[";
        }
        try {
            String strSafeGetClassName = elementOpenType.safeGetClassName();
            ReflectUtil.checkPackageAccess(strSafeGetClassName);
            Class<?> cls = Class.forName(str + "Ljava.lang.String;");
            Class<?> cls2 = Class.forName(str + "L" + strSafeGetClassName + ";");
            if (!cls.isInstance(obj)) {
                throw new IllegalArgumentException("Value for " + dimension + "-dimensional array of " + elementOpenType.getTypeName() + " must be same type or a String array with same dimensions");
            }
            if (dimension == 1) {
                arrayType = elementOpenType;
            } else {
                try {
                    arrayType = new ArrayType(dimension - 1, elementOpenType);
                } catch (OpenDataException e2) {
                    throw new IllegalArgumentException(e2.getMessage(), e2);
                }
            }
            int length = Array.getLength(obj);
            Object[] objArr = (Object[]) Array.newInstance(cls2.getComponentType(), length);
            for (int i3 = 0; i3 < length; i3++) {
                Array.set(objArr, i3, convertFromStrings(Array.get(obj, i3), arrayType));
            }
            return (T) cast(objArr);
        } catch (ClassNotFoundException e3) {
            throw new NoClassDefFoundError(e3.toString());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T> T cast(Object obj) {
        return obj;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public OpenType<?> getOpenType() {
        return this.openType;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Set<?> getLegalValues() {
        return this.legalValues;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Comparable<?> getMinValue() {
        return this.minValue;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public Comparable<?> getMaxValue() {
        return this.maxValue;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasLegalValues() {
        return this.legalValues != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasMinValue() {
        return this.minValue != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean hasMaxValue() {
        return this.maxValue != null;
    }

    @Override // javax.management.openmbean.OpenMBeanParameterInfo
    public boolean isValue(Object obj) {
        return isValue(this, obj);
    }

    static boolean isValue(OpenMBeanParameterInfo openMBeanParameterInfo, Object obj) {
        if (openMBeanParameterInfo.hasDefaultValue() && obj == null) {
            return true;
        }
        return openMBeanParameterInfo.getOpenType().isValue(obj) && (!openMBeanParameterInfo.hasLegalValues() || openMBeanParameterInfo.getLegalValues().contains(obj)) && ((!openMBeanParameterInfo.hasMinValue() || openMBeanParameterInfo.getMinValue().compareTo(obj) <= 0) && (!openMBeanParameterInfo.hasMaxValue() || openMBeanParameterInfo.getMaxValue().compareTo(obj) >= 0));
    }

    @Override // javax.management.MBeanAttributeInfo, javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (!(obj instanceof OpenMBeanAttributeInfo)) {
            return false;
        }
        OpenMBeanAttributeInfo openMBeanAttributeInfo = (OpenMBeanAttributeInfo) obj;
        return isReadable() == openMBeanAttributeInfo.isReadable() && isWritable() == openMBeanAttributeInfo.isWritable() && isIs() == openMBeanAttributeInfo.isIs() && equal(this, openMBeanAttributeInfo);
    }

    static boolean equal(OpenMBeanParameterInfo openMBeanParameterInfo, OpenMBeanParameterInfo openMBeanParameterInfo2) {
        if (openMBeanParameterInfo instanceof DescriptorRead) {
            if (!(openMBeanParameterInfo2 instanceof DescriptorRead) || !((DescriptorRead) openMBeanParameterInfo).getDescriptor().equals(((DescriptorRead) openMBeanParameterInfo2).getDescriptor())) {
                return false;
            }
        } else if (openMBeanParameterInfo2 instanceof DescriptorRead) {
            return false;
        }
        return openMBeanParameterInfo.getName().equals(openMBeanParameterInfo2.getName()) && openMBeanParameterInfo.getOpenType().equals(openMBeanParameterInfo2.getOpenType()) && (!openMBeanParameterInfo.hasDefaultValue() ? openMBeanParameterInfo2.hasDefaultValue() : !openMBeanParameterInfo.getDefaultValue().equals(openMBeanParameterInfo2.getDefaultValue())) && (!openMBeanParameterInfo.hasMinValue() ? openMBeanParameterInfo2.hasMinValue() : !openMBeanParameterInfo.getMinValue().equals(openMBeanParameterInfo2.getMinValue())) && (!openMBeanParameterInfo.hasMaxValue() ? openMBeanParameterInfo2.hasMaxValue() : !openMBeanParameterInfo.getMaxValue().equals(openMBeanParameterInfo2.getMaxValue())) && (!openMBeanParameterInfo.hasLegalValues() ? openMBeanParameterInfo2.hasLegalValues() : !openMBeanParameterInfo.getLegalValues().equals(openMBeanParameterInfo2.getLegalValues()));
    }

    @Override // javax.management.MBeanAttributeInfo, javax.management.MBeanFeatureInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = Integer.valueOf(hashCode(this));
        }
        return this.myHashCode.intValue();
    }

    static int hashCode(OpenMBeanParameterInfo openMBeanParameterInfo) {
        int iHashCode = 0 + openMBeanParameterInfo.getName().hashCode() + openMBeanParameterInfo.getOpenType().hashCode();
        if (openMBeanParameterInfo.hasDefaultValue()) {
            iHashCode += openMBeanParameterInfo.getDefaultValue().hashCode();
        }
        if (openMBeanParameterInfo.hasMinValue()) {
            iHashCode += openMBeanParameterInfo.getMinValue().hashCode();
        }
        if (openMBeanParameterInfo.hasMaxValue()) {
            iHashCode += openMBeanParameterInfo.getMaxValue().hashCode();
        }
        if (openMBeanParameterInfo.hasLegalValues()) {
            iHashCode += openMBeanParameterInfo.getLegalValues().hashCode();
        }
        if (openMBeanParameterInfo instanceof DescriptorRead) {
            iHashCode += ((DescriptorRead) openMBeanParameterInfo).getDescriptor().hashCode();
        }
        return iHashCode;
    }

    @Override // javax.management.MBeanAttributeInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = toString(this);
        }
        return this.myToString;
    }

    static String toString(OpenMBeanParameterInfo openMBeanParameterInfo) {
        Descriptor descriptor = openMBeanParameterInfo instanceof DescriptorRead ? ((DescriptorRead) openMBeanParameterInfo).getDescriptor() : null;
        return openMBeanParameterInfo.getClass().getName() + "(name=" + openMBeanParameterInfo.getName() + ",openType=" + ((Object) openMBeanParameterInfo.getOpenType()) + ",default=" + openMBeanParameterInfo.getDefaultValue() + ",minValue=" + ((Object) openMBeanParameterInfo.getMinValue()) + ",maxValue=" + ((Object) openMBeanParameterInfo.getMaxValue()) + ",legalValues=" + ((Object) openMBeanParameterInfo.getLegalValues()) + (descriptor == null ? "" : ",descriptor=" + ((Object) descriptor)) + ")";
    }
}
