package com.intel.bluetooth;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.WeakHashMap;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/WeakVectorFactory.class */
class WeakVectorFactory {

    /* renamed from: com.intel.bluetooth.WeakVectorFactory$1, reason: invalid class name */
    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/WeakVectorFactory$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/WeakVectorFactory$WeakVector.class */
    public interface WeakVector {
        void addElement(Object obj);

        int size();

        boolean removeElement(Object obj);

        boolean contains(Object obj);

        Object firstElement();

        Enumeration elements();

        void removeAllElements();
    }

    WeakVectorFactory() {
    }

    public static WeakVector createWeakVector() {
        try {
            return new WeakVectorOnWeakHashMapImpl(null);
        } catch (Throwable th) {
            return new WeakVectorOnVectorImpl(null);
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/WeakVectorFactory$WeakVectorOnVectorImpl.class */
    private static class WeakVectorOnVectorImpl implements WeakVector {
        private Vector vectorImpl;

        WeakVectorOnVectorImpl(AnonymousClass1 x0) {
            this();
        }

        private WeakVectorOnVectorImpl() {
            this.vectorImpl = new Vector();
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public void addElement(Object obj) {
            this.vectorImpl.addElement(obj);
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public boolean contains(Object elem) {
            return this.vectorImpl.contains(elem);
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public Object firstElement() {
            return this.vectorImpl.firstElement();
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public Enumeration elements() {
            return this.vectorImpl.elements();
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public boolean removeElement(Object obj) {
            return this.vectorImpl.removeElement(obj);
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public int size() {
            return this.vectorImpl.size();
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public void removeAllElements() {
            this.vectorImpl.removeAllElements();
        }
    }

    /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/WeakVectorFactory$WeakVectorOnWeakHashMapImpl.class */
    private static class WeakVectorOnWeakHashMapImpl implements WeakVector {
        private WeakHashMap mapImpl;

        WeakVectorOnWeakHashMapImpl(AnonymousClass1 x0) {
            this();
        }

        /* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/WeakVectorFactory$WeakVectorOnWeakHashMapImpl$EnumerationAdapter.class */
        private static class EnumerationAdapter implements Enumeration {
            Iterator iterator;

            public EnumerationAdapter(Iterator iterator) {
                this.iterator = iterator;
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.iterator.hasNext();
            }

            @Override // java.util.Enumeration
            public Object nextElement() {
                return this.iterator.next();
            }
        }

        private WeakVectorOnWeakHashMapImpl() {
            this.mapImpl = new WeakHashMap();
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public void addElement(Object obj) {
            this.mapImpl.put(obj, new Object());
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public boolean contains(Object elem) {
            return this.mapImpl.containsKey(elem);
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public Object firstElement() {
            return this.mapImpl.keySet().iterator().next();
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public Enumeration elements() {
            return new EnumerationAdapter(this.mapImpl.keySet().iterator());
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public boolean removeElement(Object obj) {
            return this.mapImpl.remove(obj) != null;
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public int size() {
            return this.mapImpl.size();
        }

        @Override // com.intel.bluetooth.WeakVectorFactory.WeakVector
        public void removeAllElements() {
            this.mapImpl.clear();
        }
    }
}
