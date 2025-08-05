package com.sun.javafx.css;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StyleClassSet.class */
public final class StyleClassSet extends BitSet<StyleClass> {
    static final Map<String, Integer> styleClassMap;
    static final List<StyleClass> styleClasses;
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
        $assertionsDisabled = !StyleClassSet.class.desiredAssertionStatus();
        styleClassMap = new HashMap(64);
        styleClasses = new ArrayList();
    }

    public StyleClassSet() {
    }

    StyleClassSet(List<String> styleClassNames) {
        int nMax = styleClassNames != null ? styleClassNames.size() : 0;
        for (int n2 = 0; n2 < nMax; n2++) {
            String styleClass = styleClassNames.get(n2);
            if (styleClass != null && !styleClass.isEmpty()) {
                StyleClass sc = getStyleClass(styleClass);
                add(sc);
            }
        }
    }

    @Override // java.util.Set, java.util.Collection, java.util.List
    public Object[] toArray() {
        return toArray(new StyleClass[size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v20 */
    /* JADX WARN: Type inference failed for: r0v24, types: [java.lang.Object[]] */
    @Override // java.util.Set, java.util.Collection
    public <T> T[] toArray(T[] a2) {
        if (a2.length < size()) {
            a2 = new StyleClass[size()];
        }
        int index = 0;
        while (index < getBits().length) {
            long state = getBits()[index];
            for (int bit = 0; bit < 64; bit++) {
                long mask = 1 << bit;
                if ((state & mask) == mask) {
                    int n2 = (index * 64) + bit;
                    StyleClass impl = getStyleClass(n2);
                    int i2 = index;
                    index++;
                    a2[i2] = impl;
                }
            }
        }
        return a2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("style-classes: [");
        Iterator<StyleClass> iter = iterator();
        while (iter.hasNext()) {
            builder.append(iter.next().getStyleClassName());
            if (iter.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append(']');
        return builder.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.javafx.css.BitSet
    public StyleClass cast(Object o2) {
        if (o2 == null) {
            throw new NullPointerException("null arg");
        }
        StyleClass styleClass = (StyleClass) o2;
        return styleClass;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.javafx.css.BitSet
    public StyleClass getT(int index) {
        return getStyleClass(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.javafx.css.BitSet
    public int getIndex(StyleClass t2) {
        return t2.getIndex();
    }

    static StyleClass getStyleClass(String styleClass) {
        if (styleClass == null || styleClass.trim().isEmpty()) {
            throw new IllegalArgumentException("styleClass cannot be null or empty String");
        }
        StyleClass instance = null;
        Integer value = styleClassMap.get(styleClass);
        int index = value != null ? value.intValue() : -1;
        int size = styleClasses.size();
        if (!$assertionsDisabled && index >= size) {
            throw new AssertionError();
        }
        if (index != -1 && index < size) {
            instance = styleClasses.get(index);
        }
        if (instance == null) {
            instance = new StyleClass(styleClass, size);
            styleClasses.add(instance);
            styleClassMap.put(styleClass, Integer.valueOf(size));
        }
        return instance;
    }

    static StyleClass getStyleClass(int index) {
        if (0 <= index && index < styleClasses.size()) {
            return styleClasses.get(index);
        }
        return null;
    }
}
