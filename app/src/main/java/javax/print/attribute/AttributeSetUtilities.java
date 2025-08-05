package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities.class */
public final class AttributeSetUtilities {
    private AttributeSetUtilities() {
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$UnmodifiableAttributeSet.class */
    private static class UnmodifiableAttributeSet implements AttributeSet, Serializable {
        private AttributeSet attrset;

        public UnmodifiableAttributeSet(AttributeSet attributeSet) {
            this.attrset = attributeSet;
        }

        @Override // javax.print.attribute.AttributeSet
        public Attribute get(Class<?> cls) {
            return this.attrset.get(cls);
        }

        @Override // javax.print.attribute.AttributeSet
        public boolean add(Attribute attribute) {
            throw new UnmodifiableSetException();
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean remove(Class<?> cls) {
            throw new UnmodifiableSetException();
        }

        @Override // javax.print.attribute.AttributeSet
        public boolean remove(Attribute attribute) {
            throw new UnmodifiableSetException();
        }

        @Override // javax.print.attribute.AttributeSet
        public boolean containsKey(Class<?> cls) {
            return this.attrset.containsKey(cls);
        }

        @Override // javax.print.attribute.AttributeSet
        public boolean containsValue(Attribute attribute) {
            return this.attrset.containsValue(attribute);
        }

        @Override // javax.print.attribute.AttributeSet
        public boolean addAll(AttributeSet attributeSet) {
            throw new UnmodifiableSetException();
        }

        @Override // javax.print.attribute.AttributeSet
        public int size() {
            return this.attrset.size();
        }

        @Override // javax.print.attribute.AttributeSet
        public Attribute[] toArray() {
            return this.attrset.toArray();
        }

        @Override // javax.print.attribute.AttributeSet
        public void clear() {
            throw new UnmodifiableSetException();
        }

        @Override // javax.print.attribute.AttributeSet
        public boolean isEmpty() {
            return this.attrset.isEmpty();
        }

        @Override // javax.print.attribute.AttributeSet
        public boolean equals(Object obj) {
            return this.attrset.equals(obj);
        }

        @Override // javax.print.attribute.AttributeSet
        public int hashCode() {
            return this.attrset.hashCode();
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$UnmodifiableDocAttributeSet.class */
    private static class UnmodifiableDocAttributeSet extends UnmodifiableAttributeSet implements DocAttributeSet, Serializable {
        public UnmodifiableDocAttributeSet(DocAttributeSet docAttributeSet) {
            super(docAttributeSet);
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$UnmodifiablePrintRequestAttributeSet.class */
    private static class UnmodifiablePrintRequestAttributeSet extends UnmodifiableAttributeSet implements PrintRequestAttributeSet, Serializable {
        public UnmodifiablePrintRequestAttributeSet(PrintRequestAttributeSet printRequestAttributeSet) {
            super(printRequestAttributeSet);
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$UnmodifiablePrintJobAttributeSet.class */
    private static class UnmodifiablePrintJobAttributeSet extends UnmodifiableAttributeSet implements PrintJobAttributeSet, Serializable {
        public UnmodifiablePrintJobAttributeSet(PrintJobAttributeSet printJobAttributeSet) {
            super(printJobAttributeSet);
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$UnmodifiablePrintServiceAttributeSet.class */
    private static class UnmodifiablePrintServiceAttributeSet extends UnmodifiableAttributeSet implements PrintServiceAttributeSet, Serializable {
        public UnmodifiablePrintServiceAttributeSet(PrintServiceAttributeSet printServiceAttributeSet) {
            super(printServiceAttributeSet);
        }
    }

    public static AttributeSet unmodifiableView(AttributeSet attributeSet) {
        if (attributeSet == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableAttributeSet(attributeSet);
    }

    public static DocAttributeSet unmodifiableView(DocAttributeSet docAttributeSet) {
        if (docAttributeSet == null) {
            throw new NullPointerException();
        }
        return new UnmodifiableDocAttributeSet(docAttributeSet);
    }

    public static PrintRequestAttributeSet unmodifiableView(PrintRequestAttributeSet printRequestAttributeSet) {
        if (printRequestAttributeSet == null) {
            throw new NullPointerException();
        }
        return new UnmodifiablePrintRequestAttributeSet(printRequestAttributeSet);
    }

    public static PrintJobAttributeSet unmodifiableView(PrintJobAttributeSet printJobAttributeSet) {
        if (printJobAttributeSet == null) {
            throw new NullPointerException();
        }
        return new UnmodifiablePrintJobAttributeSet(printJobAttributeSet);
    }

    public static PrintServiceAttributeSet unmodifiableView(PrintServiceAttributeSet printServiceAttributeSet) {
        if (printServiceAttributeSet == null) {
            throw new NullPointerException();
        }
        return new UnmodifiablePrintServiceAttributeSet(printServiceAttributeSet);
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$SynchronizedAttributeSet.class */
    private static class SynchronizedAttributeSet implements AttributeSet, Serializable {
        private AttributeSet attrset;

        public SynchronizedAttributeSet(AttributeSet attributeSet) {
            this.attrset = attributeSet;
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized Attribute get(Class<?> cls) {
            return this.attrset.get(cls);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean add(Attribute attribute) {
            return this.attrset.add(attribute);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean remove(Class<?> cls) {
            return this.attrset.remove(cls);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean remove(Attribute attribute) {
            return this.attrset.remove(attribute);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean containsKey(Class<?> cls) {
            return this.attrset.containsKey(cls);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean containsValue(Attribute attribute) {
            return this.attrset.containsValue(attribute);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean addAll(AttributeSet attributeSet) {
            return this.attrset.addAll(attributeSet);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized int size() {
            return this.attrset.size();
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized Attribute[] toArray() {
            return this.attrset.toArray();
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized void clear() {
            this.attrset.clear();
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean isEmpty() {
            return this.attrset.isEmpty();
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized boolean equals(Object obj) {
            return this.attrset.equals(obj);
        }

        @Override // javax.print.attribute.AttributeSet
        public synchronized int hashCode() {
            return this.attrset.hashCode();
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$SynchronizedDocAttributeSet.class */
    private static class SynchronizedDocAttributeSet extends SynchronizedAttributeSet implements DocAttributeSet, Serializable {
        public SynchronizedDocAttributeSet(DocAttributeSet docAttributeSet) {
            super(docAttributeSet);
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$SynchronizedPrintRequestAttributeSet.class */
    private static class SynchronizedPrintRequestAttributeSet extends SynchronizedAttributeSet implements PrintRequestAttributeSet, Serializable {
        public SynchronizedPrintRequestAttributeSet(PrintRequestAttributeSet printRequestAttributeSet) {
            super(printRequestAttributeSet);
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$SynchronizedPrintJobAttributeSet.class */
    private static class SynchronizedPrintJobAttributeSet extends SynchronizedAttributeSet implements PrintJobAttributeSet, Serializable {
        public SynchronizedPrintJobAttributeSet(PrintJobAttributeSet printJobAttributeSet) {
            super(printJobAttributeSet);
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/AttributeSetUtilities$SynchronizedPrintServiceAttributeSet.class */
    private static class SynchronizedPrintServiceAttributeSet extends SynchronizedAttributeSet implements PrintServiceAttributeSet, Serializable {
        public SynchronizedPrintServiceAttributeSet(PrintServiceAttributeSet printServiceAttributeSet) {
            super(printServiceAttributeSet);
        }
    }

    public static AttributeSet synchronizedView(AttributeSet attributeSet) {
        if (attributeSet == null) {
            throw new NullPointerException();
        }
        return new SynchronizedAttributeSet(attributeSet);
    }

    public static DocAttributeSet synchronizedView(DocAttributeSet docAttributeSet) {
        if (docAttributeSet == null) {
            throw new NullPointerException();
        }
        return new SynchronizedDocAttributeSet(docAttributeSet);
    }

    public static PrintRequestAttributeSet synchronizedView(PrintRequestAttributeSet printRequestAttributeSet) {
        if (printRequestAttributeSet == null) {
            throw new NullPointerException();
        }
        return new SynchronizedPrintRequestAttributeSet(printRequestAttributeSet);
    }

    public static PrintJobAttributeSet synchronizedView(PrintJobAttributeSet printJobAttributeSet) {
        if (printJobAttributeSet == null) {
            throw new NullPointerException();
        }
        return new SynchronizedPrintJobAttributeSet(printJobAttributeSet);
    }

    public static PrintServiceAttributeSet synchronizedView(PrintServiceAttributeSet printServiceAttributeSet) {
        if (printServiceAttributeSet == null) {
            throw new NullPointerException();
        }
        return new SynchronizedPrintServiceAttributeSet(printServiceAttributeSet);
    }

    public static Class<?> verifyAttributeCategory(Object obj, Class<?> cls) {
        Class<?> cls2 = (Class) obj;
        if (cls.isAssignableFrom(cls2)) {
            return cls2;
        }
        throw new ClassCastException();
    }

    public static Attribute verifyAttributeValue(Object obj, Class<?> cls) {
        if (obj == null) {
            throw new NullPointerException();
        }
        if (cls.isInstance(obj)) {
            return (Attribute) obj;
        }
        throw new ClassCastException();
    }

    public static void verifyCategoryForValue(Class<?> cls, Attribute attribute) {
        if (!cls.equals(attribute.getCategory())) {
            throw new IllegalArgumentException();
        }
    }
}
