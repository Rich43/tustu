package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;

/* loaded from: jfxrt.jar:com/sun/javafx/css/PseudoClassState.class */
public final class PseudoClassState extends BitSet<PseudoClass> {
    static final Map<String, Integer> pseudoClassMap;
    static final List<PseudoClass> pseudoClasses;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // com.sun.javafx.css.BitSet, javafx.beans.Observable
    public /* bridge */ /* synthetic */ void removeListener(InvalidationListener invalidationListener) {
        super.removeListener(invalidationListener);
    }

    @Override // com.sun.javafx.css.BitSet, javafx.beans.Observable
    public /* bridge */ /* synthetic */ void addListener(InvalidationListener invalidationListener) {
        super.addListener(invalidationListener);
    }

    @Override // com.sun.javafx.css.BitSet, javafx.collections.ObservableSet
    public /* bridge */ /* synthetic */ void removeListener(SetChangeListener setChangeListener) {
        super.removeListener(setChangeListener);
    }

    @Override // com.sun.javafx.css.BitSet, javafx.collections.ObservableSet
    public /* bridge */ /* synthetic */ void addListener(SetChangeListener setChangeListener) {
        super.addListener(setChangeListener);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection, java.util.List
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection, java.util.List
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection, java.util.List
    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection
    public /* bridge */ /* synthetic */ boolean removeAll(Collection collection) {
        return super.removeAll(collection);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection
    public /* bridge */ /* synthetic */ boolean retainAll(Collection collection) {
        return super.retainAll(collection);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection
    public /* bridge */ /* synthetic */ boolean addAll(Collection collection) {
        return super.addAll(collection);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection
    public /* bridge */ /* synthetic */ boolean containsAll(Collection collection) {
        return super.containsAll(collection);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set
    public /* bridge */ /* synthetic */ boolean contains(Object obj) {
        return super.contains(obj);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set
    public /* bridge */ /* synthetic */ boolean remove(Object obj) {
        return super.remove(obj);
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection, java.lang.Iterable, java.util.List
    public /* bridge */ /* synthetic */ Iterator iterator() {
        return super.iterator();
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set, java.util.Collection
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.sun.javafx.css.BitSet, java.util.Set
    public /* bridge */ /* synthetic */ int size() {
        return super.size();
    }

    static {
        $assertionsDisabled = !PseudoClassState.class.desiredAssertionStatus();
        pseudoClassMap = new HashMap(64);
        pseudoClasses = new ArrayList();
    }

    public PseudoClassState() {
    }

    PseudoClassState(List<String> pseudoClassNames) {
        int nMax = pseudoClassNames != null ? pseudoClassNames.size() : 0;
        for (int n2 = 0; n2 < nMax; n2++) {
            PseudoClass sc = getPseudoClass(pseudoClassNames.get(n2));
            add(sc);
        }
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public Object[] toArray() {
        return toArray(new PseudoClass[size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v20 */
    /* JADX WARN: Type inference failed for: r0v24, types: [java.lang.Object[]] */
    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] a2) {
        if (a2.length < size()) {
            a2 = new PseudoClass[size()];
        }
        int index = 0;
        while (index < getBits().length) {
            long state = getBits()[index];
            for (int bit = 0; bit < 64; bit++) {
                long mask = 1 << bit;
                if ((state & mask) == mask) {
                    int n2 = (index * 64) + bit;
                    PseudoClass impl = getPseudoClass(n2);
                    int i2 = index;
                    index++;
                    a2[i2] = impl;
                }
            }
        }
        return a2;
    }

    public String toString() {
        List<String> strings = new ArrayList<>();
        Iterator<PseudoClass> iter = iterator();
        while (iter.hasNext()) {
            strings.add(iter.next().getPseudoClassName());
        }
        return strings.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.javafx.css.BitSet
    public PseudoClass cast(Object o2) {
        if (o2 == null) {
            throw new NullPointerException("null arg");
        }
        PseudoClass pseudoClass = (PseudoClass) o2;
        return pseudoClass;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.javafx.css.BitSet
    public PseudoClass getT(int index) {
        return getPseudoClass(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.css.BitSet
    public int getIndex(PseudoClass t2) {
        if (t2 instanceof PseudoClassImpl) {
            return ((PseudoClassImpl) t2).getIndex();
        }
        String pseudoClass = t2.getPseudoClassName();
        Integer index = pseudoClassMap.get(pseudoClass);
        if (index == null) {
            index = Integer.valueOf(pseudoClasses.size());
            pseudoClasses.add(new PseudoClassImpl(pseudoClass, index.intValue()));
            pseudoClassMap.put(pseudoClass, index);
        }
        return index.intValue();
    }

    public static PseudoClass getPseudoClass(String pseudoClass) {
        if (pseudoClass == null || pseudoClass.trim().isEmpty()) {
            throw new IllegalArgumentException("pseudoClass cannot be null or empty String");
        }
        PseudoClass instance = null;
        Integer value = pseudoClassMap.get(pseudoClass);
        int index = value != null ? value.intValue() : -1;
        int size = pseudoClasses.size();
        if (!$assertionsDisabled && index >= size) {
            throw new AssertionError();
        }
        if (index != -1 && index < size) {
            instance = pseudoClasses.get(index);
        }
        if (instance == null) {
            instance = new PseudoClassImpl(pseudoClass, size);
            pseudoClasses.add(instance);
            pseudoClassMap.put(pseudoClass, Integer.valueOf(size));
        }
        return instance;
    }

    static PseudoClass getPseudoClass(int index) {
        if (0 <= index && index < pseudoClasses.size()) {
            return pseudoClasses.get(index);
        }
        return null;
    }
}
