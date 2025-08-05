package javax.management.openmbean;

import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Util;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:javax/management/openmbean/TabularDataSupport.class */
public class TabularDataSupport implements TabularData, Map<Object, Object>, Cloneable, Serializable {
    static final long serialVersionUID = 5720150593236309827L;
    private Map<Object, CompositeData> dataMap;
    private final TabularType tabularType;
    private transient String[] indexNamesArray;

    public TabularDataSupport(TabularType tabularType) {
        this(tabularType, 16, 0.75f);
    }

    public TabularDataSupport(TabularType tabularType, int i2, float f2) {
        if (tabularType == null) {
            throw new IllegalArgumentException("Argument tabularType cannot be null.");
        }
        this.tabularType = tabularType;
        List<String> indexNames = tabularType.getIndexNames();
        this.indexNamesArray = (String[]) indexNames.toArray(new String[indexNames.size()]);
        this.dataMap = "true".equalsIgnoreCase((String) AccessController.doPrivileged(new GetPropertyAction("jmx.tabular.data.hash.map"))) ? new HashMap<>(i2, f2) : new LinkedHashMap<>(i2, f2);
    }

    @Override // javax.management.openmbean.TabularData
    public TabularType getTabularType() {
        return this.tabularType;
    }

    @Override // javax.management.openmbean.TabularData
    public Object[] calculateIndex(CompositeData compositeData) {
        checkValueType(compositeData);
        return internalCalculateIndex(compositeData).toArray();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        try {
            return containsKey((Object[]) obj);
        } catch (ClassCastException e2) {
            return false;
        }
    }

    @Override // javax.management.openmbean.TabularData
    public boolean containsKey(Object[] objArr) {
        if (objArr == null) {
            return false;
        }
        return this.dataMap.containsKey(Arrays.asList(objArr));
    }

    @Override // javax.management.openmbean.TabularData
    public boolean containsValue(CompositeData compositeData) {
        return this.dataMap.containsValue(compositeData);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.dataMap.containsValue(obj);
    }

    @Override // java.util.Map
    public Object get(Object obj) {
        return get((Object[]) obj);
    }

    @Override // javax.management.openmbean.TabularData
    public CompositeData get(Object[] objArr) {
        checkKeyType(objArr);
        return this.dataMap.get(Arrays.asList(objArr));
    }

    @Override // java.util.Map
    public Object put(Object obj, Object obj2) {
        internalPut((CompositeData) obj2);
        return obj2;
    }

    @Override // javax.management.openmbean.TabularData
    public void put(CompositeData compositeData) {
        internalPut(compositeData);
    }

    private CompositeData internalPut(CompositeData compositeData) {
        return this.dataMap.put(checkValueAndIndex(compositeData), compositeData);
    }

    @Override // java.util.Map
    public Object remove(Object obj) {
        return remove((Object[]) obj);
    }

    @Override // javax.management.openmbean.TabularData
    public CompositeData remove(Object[] objArr) {
        checkKeyType(objArr);
        return this.dataMap.remove(Arrays.asList(objArr));
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Object, ? extends Object> map) {
        if (map == null || map.size() == 0) {
            return;
        }
        try {
            putAll((CompositeData[]) map.values().toArray(new CompositeData[map.size()]));
        } catch (ArrayStoreException e2) {
            throw new ClassCastException("Map argument t contains values which are not instances of <tt>CompositeData</tt>");
        }
    }

    @Override // javax.management.openmbean.TabularData
    public void putAll(CompositeData[] compositeDataArr) {
        if (compositeDataArr == null || compositeDataArr.length == 0) {
            return;
        }
        ArrayList arrayList = new ArrayList(compositeDataArr.length + 1);
        for (int i2 = 0; i2 < compositeDataArr.length; i2++) {
            List<?> listCheckValueAndIndex = checkValueAndIndex(compositeDataArr[i2]);
            if (arrayList.contains(listCheckValueAndIndex)) {
                throw new KeyAlreadyExistsException("Argument elements values[" + i2 + "] and values[" + arrayList.indexOf(listCheckValueAndIndex) + "] have the same indexes, calculated according to this TabularData instance's tabularType.");
            }
            arrayList.add(listCheckValueAndIndex);
        }
        for (int i3 = 0; i3 < compositeDataArr.length; i3++) {
            this.dataMap.put(arrayList.get(i3), compositeDataArr[i3]);
        }
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public void clear() {
        this.dataMap.clear();
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public int size() {
        return this.dataMap.size();
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public Set<Object> keySet() {
        return this.dataMap.keySet();
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public Collection<Object> values() {
        return (Collection) Util.cast(this.dataMap.values());
    }

    @Override // java.util.Map
    public Set<Map.Entry<Object, Object>> entrySet() {
        return (Set) Util.cast(this.dataMap.entrySet());
    }

    public Object clone() {
        try {
            TabularDataSupport tabularDataSupport = (TabularDataSupport) super.clone();
            tabularDataSupport.dataMap = new HashMap(tabularDataSupport.dataMap);
            return tabularDataSupport;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2.toString(), e2);
        }
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            TabularData tabularData = (TabularData) obj;
            if (!getTabularType().equals(tabularData.getTabularType()) || size() != tabularData.size()) {
                return false;
            }
            Iterator<CompositeData> it = this.dataMap.values().iterator();
            while (it.hasNext()) {
                if (!tabularData.containsValue(it.next())) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    @Override // javax.management.openmbean.TabularData, java.util.Map
    public int hashCode() {
        int iHashCode = 0 + this.tabularType.hashCode();
        Iterator<Object> it = values().iterator();
        while (it.hasNext()) {
            iHashCode += it.next().hashCode();
        }
        return iHashCode;
    }

    @Override // javax.management.openmbean.TabularData
    public String toString() {
        return getClass().getName() + "(tabularType=" + this.tabularType.toString() + ",contents=" + this.dataMap.toString() + ")";
    }

    private List<?> internalCalculateIndex(CompositeData compositeData) {
        return Collections.unmodifiableList(Arrays.asList(compositeData.getAll(this.indexNamesArray)));
    }

    private void checkKeyType(Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            throw new NullPointerException("Argument key cannot be null or empty.");
        }
        if (objArr.length != this.indexNamesArray.length) {
            throw new InvalidKeyException("Argument key's length=" + objArr.length + " is different from the number of item values, which is " + this.indexNamesArray.length + ", specified for the indexing rows in this TabularData instance.");
        }
        for (int i2 = 0; i2 < objArr.length; i2++) {
            OpenType<?> type = this.tabularType.getRowType().getType(this.indexNamesArray[i2]);
            if (objArr[i2] != null && !type.isValue(objArr[i2])) {
                throw new InvalidKeyException("Argument element key[" + i2 + "] is not a value for the open type expected for this element of the index, whose name is \"" + this.indexNamesArray[i2] + "\" and whose open type is " + ((Object) type));
            }
        }
    }

    private void checkValueType(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Argument value cannot be null.");
        }
        if (!this.tabularType.getRowType().isValue(compositeData)) {
            throw new InvalidOpenTypeException("Argument value's composite type [" + ((Object) compositeData.getCompositeType()) + "] is not assignable to this TabularData instance's row type [" + ((Object) this.tabularType.getRowType()) + "].");
        }
    }

    private List<?> checkValueAndIndex(CompositeData compositeData) {
        checkValueType(compositeData);
        List<?> listInternalCalculateIndex = internalCalculateIndex(compositeData);
        if (this.dataMap.containsKey(listInternalCalculateIndex)) {
            throw new KeyAlreadyExistsException("Argument value's index, calculated according to this TabularData instance's tabularType, already refers to a value in this table.");
        }
        return listInternalCalculateIndex;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        List<String> indexNames = this.tabularType.getIndexNames();
        int size = indexNames.size();
        SharedSecrets.getJavaOISAccess().checkArray(objectInputStream, String[].class, size);
        this.indexNamesArray = (String[]) indexNames.toArray(new String[size]);
    }
}
