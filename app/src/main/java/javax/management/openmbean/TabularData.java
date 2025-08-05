package javax.management.openmbean;

import java.util.Collection;
import java.util.Set;

/* loaded from: rt.jar:javax/management/openmbean/TabularData.class */
public interface TabularData {
    TabularType getTabularType();

    Object[] calculateIndex(CompositeData compositeData);

    int size();

    boolean isEmpty();

    boolean containsKey(Object[] objArr);

    boolean containsValue(CompositeData compositeData);

    CompositeData get(Object[] objArr);

    void put(CompositeData compositeData);

    CompositeData remove(Object[] objArr);

    void putAll(CompositeData[] compositeDataArr);

    void clear();

    Set<?> keySet();

    Collection<?> values();

    boolean equals(Object obj);

    int hashCode();

    String toString();
}
