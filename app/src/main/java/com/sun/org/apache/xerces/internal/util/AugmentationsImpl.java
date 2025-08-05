package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.Augmentations;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/AugmentationsImpl.class */
public class AugmentationsImpl implements Augmentations {
    private AugmentationsItemsContainer fAugmentationsContainer = new SmallContainer();

    @Override // com.sun.org.apache.xerces.internal.xni.Augmentations
    public Object putItem(String key, Object item) {
        Object oldValue = this.fAugmentationsContainer.putItem(key, item);
        if (oldValue == null && this.fAugmentationsContainer.isFull()) {
            this.fAugmentationsContainer = this.fAugmentationsContainer.expand();
        }
        return oldValue;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.Augmentations
    public Object getItem(String key) {
        return this.fAugmentationsContainer.getItem(key);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.Augmentations
    public Object removeItem(String key) {
        return this.fAugmentationsContainer.removeItem(key);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.Augmentations
    public Enumeration keys() {
        return this.fAugmentationsContainer.keys();
    }

    @Override // com.sun.org.apache.xerces.internal.xni.Augmentations
    public void removeAllItems() {
        this.fAugmentationsContainer.clear();
    }

    public String toString() {
        return this.fAugmentationsContainer.toString();
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/AugmentationsImpl$AugmentationsItemsContainer.class */
    abstract class AugmentationsItemsContainer {
        public abstract Object putItem(Object obj, Object obj2);

        public abstract Object getItem(Object obj);

        public abstract Object removeItem(Object obj);

        public abstract Enumeration keys();

        public abstract void clear();

        public abstract boolean isFull();

        public abstract AugmentationsItemsContainer expand();

        AugmentationsItemsContainer() {
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/AugmentationsImpl$SmallContainer.class */
    class SmallContainer extends AugmentationsItemsContainer {
        static final int SIZE_LIMIT = 10;
        final Object[] fAugmentations;
        int fNumEntries;

        SmallContainer() {
            super();
            this.fAugmentations = new Object[20];
            this.fNumEntries = 0;
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Enumeration keys() {
            return new SmallContainerKeyEnumeration();
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Object getItem(Object key) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.fNumEntries * 2) {
                    if (!this.fAugmentations[i3].equals(key)) {
                        i2 = i3 + 2;
                    } else {
                        return this.fAugmentations[i3 + 1];
                    }
                } else {
                    return null;
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Object putItem(Object key, Object item) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.fNumEntries * 2) {
                    if (!this.fAugmentations[i3].equals(key)) {
                        i2 = i3 + 2;
                    } else {
                        Object oldValue = this.fAugmentations[i3 + 1];
                        this.fAugmentations[i3 + 1] = item;
                        return oldValue;
                    }
                } else {
                    this.fAugmentations[this.fNumEntries * 2] = key;
                    this.fAugmentations[(this.fNumEntries * 2) + 1] = item;
                    this.fNumEntries++;
                    return null;
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Object removeItem(Object key) {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.fNumEntries * 2) {
                    if (!this.fAugmentations[i3].equals(key)) {
                        i2 = i3 + 2;
                    } else {
                        Object oldValue = this.fAugmentations[i3 + 1];
                        int i4 = i3;
                        while (true) {
                            int j2 = i4;
                            if (j2 < (this.fNumEntries * 2) - 2) {
                                this.fAugmentations[j2] = this.fAugmentations[j2 + 2];
                                this.fAugmentations[j2 + 1] = this.fAugmentations[j2 + 3];
                                i4 = j2 + 2;
                            } else {
                                this.fAugmentations[(this.fNumEntries * 2) - 2] = null;
                                this.fAugmentations[(this.fNumEntries * 2) - 1] = null;
                                this.fNumEntries--;
                                return oldValue;
                            }
                        }
                    }
                } else {
                    return null;
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public void clear() {
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.fNumEntries * 2) {
                    this.fAugmentations[i3] = null;
                    this.fAugmentations[i3 + 1] = null;
                    i2 = i3 + 2;
                } else {
                    this.fNumEntries = 0;
                    return;
                }
            }
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public boolean isFull() {
            return this.fNumEntries == 10;
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public AugmentationsItemsContainer expand() {
            LargeContainer expandedContainer = AugmentationsImpl.this.new LargeContainer();
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < this.fNumEntries * 2) {
                    expandedContainer.putItem(this.fAugmentations[i3], this.fAugmentations[i3 + 1]);
                    i2 = i3 + 2;
                } else {
                    return expandedContainer;
                }
            }
        }

        public String toString() {
            StringBuilder buff = new StringBuilder();
            buff.append("SmallContainer - fNumEntries == ").append(this.fNumEntries);
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < 20) {
                    buff.append("\nfAugmentations[").append(i3).append("] == ").append(this.fAugmentations[i3]).append("; fAugmentations[").append(i3 + 1).append("] == ").append(this.fAugmentations[i3 + 1]);
                    i2 = i3 + 2;
                } else {
                    return buff.toString();
                }
            }
        }

        /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/AugmentationsImpl$SmallContainer$SmallContainerKeyEnumeration.class */
        class SmallContainerKeyEnumeration implements Enumeration {
            Object[] enumArray;
            int next = 0;

            SmallContainerKeyEnumeration() {
                this.enumArray = new Object[SmallContainer.this.fNumEntries];
                for (int i2 = 0; i2 < SmallContainer.this.fNumEntries; i2++) {
                    this.enumArray[i2] = SmallContainer.this.fAugmentations[i2 * 2];
                }
            }

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return this.next < this.enumArray.length;
            }

            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public Object nextElement2() {
                if (this.next >= this.enumArray.length) {
                    throw new NoSuchElementException();
                }
                Object nextVal = this.enumArray[this.next];
                this.enumArray[this.next] = null;
                this.next++;
                return nextVal;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/AugmentationsImpl$LargeContainer.class */
    class LargeContainer extends AugmentationsItemsContainer {
        final Map<Object, Object> fAugmentations;

        LargeContainer() {
            super();
            this.fAugmentations = new HashMap();
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Object getItem(Object key) {
            return this.fAugmentations.get(key);
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Object putItem(Object key, Object item) {
            return this.fAugmentations.put(key, item);
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Object removeItem(Object key) {
            return this.fAugmentations.remove(key);
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public Enumeration keys() {
            return Collections.enumeration(this.fAugmentations.keySet());
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public void clear() {
            this.fAugmentations.clear();
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public boolean isFull() {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.util.AugmentationsImpl.AugmentationsItemsContainer
        public AugmentationsItemsContainer expand() {
            return this;
        }

        public String toString() {
            StringBuilder buff = new StringBuilder();
            buff.append("LargeContainer");
            for (Object key : this.fAugmentations.keySet()) {
                buff.append("\nkey == ");
                buff.append(key);
                buff.append("; value == ");
                buff.append(this.fAugmentations.get(key));
            }
            return buff.toString();
        }
    }
}
