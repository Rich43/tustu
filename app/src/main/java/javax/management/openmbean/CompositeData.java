package javax.management.openmbean;

import java.util.Collection;

/* loaded from: rt.jar:javax/management/openmbean/CompositeData.class */
public interface CompositeData {
    CompositeType getCompositeType();

    Object get(String str);

    Object[] getAll(String[] strArr);

    boolean containsKey(String str);

    boolean containsValue(Object obj);

    Collection<?> values();

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
