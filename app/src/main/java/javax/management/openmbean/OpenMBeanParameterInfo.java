package javax.management.openmbean;

import java.util.Set;

/* loaded from: rt.jar:javax/management/openmbean/OpenMBeanParameterInfo.class */
public interface OpenMBeanParameterInfo {
    String getDescription();

    String getName();

    OpenType<?> getOpenType();

    Object getDefaultValue();

    Set<?> getLegalValues();

    Comparable<?> getMinValue();

    Comparable<?> getMaxValue();

    boolean hasDefaultValue();

    boolean hasLegalValues();

    boolean hasMinValue();

    boolean hasMaxValue();

    boolean isValue(Object obj);

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
