package javax.management.openmbean;

import java.util.Set;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.JMX;
import javax.management.MBeanParameterInfo;

/* loaded from: rt.jar:javax/management/openmbean/OpenMBeanParameterInfoSupport.class */
public class OpenMBeanParameterInfoSupport extends MBeanParameterInfo implements OpenMBeanParameterInfo {
    static final long serialVersionUID = -7235016873758443122L;
    private OpenType<?> openType;
    private Object defaultValue;
    private Set<?> legalValues;
    private Comparable<?> minValue;
    private Comparable<?> maxValue;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanParameterInfoSupport(String str, String str2, OpenType<?> openType) {
        this(str, str2, openType, (Descriptor) null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public OpenMBeanParameterInfoSupport(String str, String str2, OpenType<?> openType, Descriptor descriptor) {
        String className = openType == null ? null : openType.getClassName();
        Descriptor[] descriptorArr = new Descriptor[2];
        descriptorArr[0] = descriptor;
        descriptorArr[1] = openType == null ? null : openType.getDescriptor();
        super(str, className, str2, ImmutableDescriptor.union(descriptorArr));
        this.defaultValue = null;
        this.legalValues = null;
        this.minValue = null;
        this.maxValue = null;
        this.myHashCode = null;
        this.myToString = null;
        this.openType = openType;
        Descriptor descriptor2 = getDescriptor();
        this.defaultValue = OpenMBeanAttributeInfoSupport.valueFrom(descriptor2, JMX.DEFAULT_VALUE_FIELD, openType);
        this.legalValues = OpenMBeanAttributeInfoSupport.valuesFrom(descriptor2, JMX.LEGAL_VALUES_FIELD, openType);
        this.minValue = OpenMBeanAttributeInfoSupport.comparableValueFrom(descriptor2, JMX.MIN_VALUE_FIELD, openType);
        this.maxValue = OpenMBeanAttributeInfoSupport.comparableValueFrom(descriptor2, JMX.MAX_VALUE_FIELD, openType);
        try {
            OpenMBeanAttributeInfoSupport.check(this);
        } catch (OpenDataException e2) {
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    public <T> OpenMBeanParameterInfoSupport(String str, String str2, OpenType<T> openType, T t2) throws OpenDataException {
        this(str, str2, openType, t2, (Object[]) null);
    }

    public <T> OpenMBeanParameterInfoSupport(String str, String str2, OpenType<T> openType, T t2, T[] tArr) throws OpenDataException {
        this(str, str2, openType, t2, tArr, null, null);
    }

    public <T> OpenMBeanParameterInfoSupport(String str, String str2, OpenType<T> openType, T t2, Comparable<T> comparable, Comparable<T> comparable2) throws OpenDataException {
        this(str, str2, openType, t2, null, comparable, comparable2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> OpenMBeanParameterInfoSupport(String str, String str2, OpenType<T> openType, T t2, T[] tArr, Comparable<T> comparable, Comparable<T> comparable2) throws OpenDataException {
        super(str, openType == 0 ? null : openType.getClassName(), str2, OpenMBeanAttributeInfoSupport.makeDescriptor(openType, t2, tArr, comparable, comparable2));
        this.defaultValue = null;
        this.legalValues = null;
        this.minValue = null;
        this.maxValue = null;
        this.myHashCode = null;
        this.myToString = null;
        this.openType = openType;
        Descriptor descriptor = getDescriptor();
        this.defaultValue = t2;
        this.minValue = comparable;
        this.maxValue = comparable2;
        this.legalValues = (Set) descriptor.getFieldValue(JMX.LEGAL_VALUES_FIELD);
        OpenMBeanAttributeInfoSupport.check(this);
    }

    private Object readResolve() {
        if (getDescriptor().getFieldNames().length == 0) {
            return new OpenMBeanParameterInfoSupport(this.name, this.description, this.openType, OpenMBeanAttributeInfoSupport.makeDescriptor((OpenType<Object>) OpenMBeanAttributeInfoSupport.cast(this.openType), this.defaultValue, (Set<Object>) OpenMBeanAttributeInfoSupport.cast(this.legalValues), (Comparable<Object>) OpenMBeanAttributeInfoSupport.cast(this.minValue), (Comparable<Object>) OpenMBeanAttributeInfoSupport.cast(this.maxValue)));
        }
        return this;
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
        return OpenMBeanAttributeInfoSupport.isValue(this, obj);
    }

    @Override // javax.management.MBeanParameterInfo, javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (!(obj instanceof OpenMBeanParameterInfo)) {
            return false;
        }
        return OpenMBeanAttributeInfoSupport.equal(this, (OpenMBeanParameterInfo) obj);
    }

    @Override // javax.management.MBeanParameterInfo, javax.management.MBeanFeatureInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = Integer.valueOf(OpenMBeanAttributeInfoSupport.hashCode(this));
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.MBeanParameterInfo, javax.management.openmbean.OpenMBeanParameterInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = OpenMBeanAttributeInfoSupport.toString(this);
        }
        return this.myToString;
    }
}
