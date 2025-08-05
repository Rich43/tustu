package javax.swing;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:javax/swing/MultiUIDefaults.class */
class MultiUIDefaults extends UIDefaults {
    private UIDefaults[] tables;

    public MultiUIDefaults(UIDefaults[] uIDefaultsArr) {
        this.tables = uIDefaultsArr;
    }

    public MultiUIDefaults() {
        this.tables = new UIDefaults[0];
    }

    @Override // javax.swing.UIDefaults, java.util.Hashtable, java.util.Dictionary
    public Object get(Object obj) {
        Object obj2 = super.get(obj);
        if (obj2 != null) {
            return obj2;
        }
        UIDefaults[] uIDefaultsArr = this.tables;
        int length = uIDefaultsArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            UIDefaults uIDefaults = uIDefaultsArr[i2];
            Object obj3 = uIDefaults != null ? uIDefaults.get(obj) : null;
            if (obj3 != null) {
                return obj3;
            }
        }
        return null;
    }

    @Override // javax.swing.UIDefaults
    public Object get(Object obj, Locale locale) {
        Object obj2 = super.get(obj, locale);
        if (obj2 != null) {
            return obj2;
        }
        UIDefaults[] uIDefaultsArr = this.tables;
        int length = uIDefaultsArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            UIDefaults uIDefaults = uIDefaultsArr[i2];
            Object obj3 = uIDefaults != null ? uIDefaults.get(obj, locale) : null;
            if (obj3 != null) {
                return obj3;
            }
        }
        return null;
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public int size() {
        return entrySet().size();
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Enumeration<Object> keys() {
        return new MultiUIDefaultsEnumerator(MultiUIDefaultsEnumerator.Type.KEYS, entrySet());
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Enumeration<Object> elements() {
        return new MultiUIDefaultsEnumerator(MultiUIDefaultsEnumerator.Type.ELEMENTS, entrySet());
    }

    @Override // java.util.Hashtable, java.util.Map
    public Set<Map.Entry<Object, Object>> entrySet() {
        HashSet hashSet = new HashSet();
        for (int length = this.tables.length - 1; length >= 0; length--) {
            if (this.tables[length] != null) {
                hashSet.addAll(this.tables[length].entrySet());
            }
        }
        hashSet.addAll(super.entrySet());
        return hashSet;
    }

    @Override // javax.swing.UIDefaults
    protected void getUIError(String str) {
        if (this.tables.length > 0) {
            this.tables[0].getUIError(str);
        } else {
            super.getUIError(str);
        }
    }

    /* loaded from: rt.jar:javax/swing/MultiUIDefaults$MultiUIDefaultsEnumerator.class */
    private static class MultiUIDefaultsEnumerator implements Enumeration<Object> {
        private Iterator<Map.Entry<Object, Object>> iterator;
        private Type type;

        /* loaded from: rt.jar:javax/swing/MultiUIDefaults$MultiUIDefaultsEnumerator$Type.class */
        public enum Type {
            KEYS,
            ELEMENTS
        }

        MultiUIDefaultsEnumerator(Type type, Set<Map.Entry<Object, Object>> set) {
            this.type = type;
            this.iterator = set.iterator();
        }

        @Override // java.util.Enumeration
        public boolean hasMoreElements() {
            return this.iterator.hasNext();
        }

        @Override // java.util.Enumeration
        /* renamed from: nextElement */
        public Object nextElement2() {
            switch (this.type) {
                case KEYS:
                    return this.iterator.next().getKey();
                case ELEMENTS:
                    return this.iterator.next().getValue();
                default:
                    return null;
            }
        }
    }

    @Override // java.util.Hashtable, java.util.Dictionary
    public Object remove(Object obj) {
        Object objRemove;
        Object obj2 = null;
        for (int length = this.tables.length - 1; length >= 0; length--) {
            if (this.tables[length] != null && (objRemove = this.tables[length].remove(obj)) != null) {
                obj2 = objRemove;
            }
        }
        Object objRemove2 = super.remove(obj);
        if (objRemove2 != null) {
            obj2 = objRemove2;
        }
        return obj2;
    }

    @Override // java.util.Hashtable, java.util.Map
    public void clear() {
        super.clear();
        for (UIDefaults uIDefaults : this.tables) {
            if (uIDefaults != null) {
                uIDefaults.clear();
            }
        }
    }

    @Override // java.util.Hashtable
    public synchronized String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(VectorFormat.DEFAULT_PREFIX);
        Enumeration<Object> enumerationKeys = keys();
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement2 = enumerationKeys.nextElement2();
            stringBuffer.append(objNextElement2 + "=" + get(objNextElement2) + ", ");
        }
        int length = stringBuffer.length();
        if (length > 1) {
            stringBuffer.delete(length - 2, length);
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}
