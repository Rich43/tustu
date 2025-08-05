package javax.print.attribute.standard;

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttribute;

/* loaded from: rt.jar:javax/print/attribute/standard/PrinterStateReasons.class */
public final class PrinterStateReasons extends HashMap<PrinterStateReason, Severity> implements PrintServiceAttribute {
    private static final long serialVersionUID = -3731791085163619457L;

    public PrinterStateReasons() {
    }

    public PrinterStateReasons(int i2) {
        super(i2);
    }

    public PrinterStateReasons(int i2, float f2) {
        super(i2, f2);
    }

    public PrinterStateReasons(Map<PrinterStateReason, Severity> map) {
        this();
        for (Map.Entry<PrinterStateReason, Severity> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public Severity put(PrinterStateReason printerStateReason, Severity severity) {
        if (printerStateReason == null) {
            throw new NullPointerException("reason is null");
        }
        if (severity == null) {
            throw new NullPointerException("severity is null");
        }
        return (Severity) super.put((PrinterStateReasons) printerStateReason, (PrinterStateReason) severity);
    }

    @Override // javax.print.attribute.Attribute
    public final Class<? extends Attribute> getCategory() {
        return PrinterStateReasons.class;
    }

    @Override // javax.print.attribute.Attribute
    public final String getName() {
        return "printer-state-reasons";
    }

    public Set<PrinterStateReason> printerStateReasonSet(Severity severity) {
        if (severity == null) {
            throw new NullPointerException("severity is null");
        }
        return new PrinterStateReasonSet(severity, entrySet());
    }

    /* loaded from: rt.jar:javax/print/attribute/standard/PrinterStateReasons$PrinterStateReasonSet.class */
    private class PrinterStateReasonSet extends AbstractSet<PrinterStateReason> {
        private Severity mySeverity;
        private Set myEntrySet;

        public PrinterStateReasonSet(Severity severity, Set set) {
            this.mySeverity = severity;
            this.myEntrySet = set;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            int i2 = 0;
            Iterator it = iterator();
            while (it.hasNext()) {
                it.next();
                i2++;
            }
            return i2;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator iterator() {
            return PrinterStateReasons.this.new PrinterStateReasonSetIterator(this.mySeverity, this.myEntrySet.iterator());
        }
    }

    /* loaded from: rt.jar:javax/print/attribute/standard/PrinterStateReasons$PrinterStateReasonSetIterator.class */
    private class PrinterStateReasonSetIterator implements Iterator {
        private Severity mySeverity;
        private Iterator myIterator;
        private Map.Entry myEntry;

        public PrinterStateReasonSetIterator(Severity severity, Iterator it) {
            this.mySeverity = severity;
            this.myIterator = it;
            goToNext();
        }

        private void goToNext() {
            this.myEntry = null;
            while (this.myEntry == null && this.myIterator.hasNext()) {
                this.myEntry = (Map.Entry) this.myIterator.next();
                if (((Severity) this.myEntry.getValue()) != this.mySeverity) {
                    this.myEntry = null;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.myEntry != null;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.myEntry == null) {
                throw new NoSuchElementException();
            }
            Object key = this.myEntry.getKey();
            goToNext();
            return key;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
