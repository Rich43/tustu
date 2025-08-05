package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xalan.internal.xsltc.CollatorFactory;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.utils.StringComparable;
import java.text.Collator;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecord.class */
public abstract class NodeSortRecord {
    public static final int COMPARE_STRING = 0;
    public static final int COMPARE_NUMERIC = 1;
    public static final int COMPARE_ASCENDING = 0;
    public static final int COMPARE_DESCENDING = 1;
    private static final Collator DEFAULT_COLLATOR = Collator.getInstance();
    protected Collator _collator;
    protected Collator[] _collators;
    protected Locale _locale;
    protected CollatorFactory _collatorFactory;
    protected SortSettings _settings;
    private DOM _dom;
    private int _node;
    private int _last;
    private int _scanned;
    private Object[] _values;

    public abstract String extractValueFromDOM(DOM dom, int i2, int i3, AbstractTranslet abstractTranslet, int i4);

    public NodeSortRecord(int node) {
        this._collator = DEFAULT_COLLATOR;
        this._dom = null;
        this._last = 0;
        this._scanned = 0;
        this._node = node;
    }

    public NodeSortRecord() {
        this(0);
    }

    public final void initialize(int node, int last, DOM dom, SortSettings settings) throws TransletException, ConfigurationError {
        this._dom = dom;
        this._node = node;
        this._last = last;
        this._settings = settings;
        int levels = settings.getSortOrders().length;
        this._values = new Object[levels];
        String colFactClassname = null;
        try {
            colFactClassname = SecuritySupport.getSystemProperty("com.sun.org.apache.xalan.internal.xsltc.COLLATOR_FACTORY");
        } catch (SecurityException e2) {
        }
        if (colFactClassname != null) {
            try {
                Object candObj = ObjectFactory.findProviderClass(colFactClassname, true);
                this._collatorFactory = (CollatorFactory) candObj;
                Locale[] locales = settings.getLocales();
                this._collators = new Collator[levels];
                for (int i2 = 0; i2 < levels; i2++) {
                    this._collators[i2] = this._collatorFactory.getCollator(locales[i2]);
                }
                this._collator = this._collators[0];
                return;
            } catch (ClassNotFoundException e3) {
                throw new TransletException(e3);
            }
        }
        this._collators = settings.getCollators();
        this._collator = this._collators[0];
    }

    public final int getNode() {
        return this._node;
    }

    public final int compareDocOrder(NodeSortRecord other) {
        return this._node - other._node;
    }

    private final Comparable stringValue(int level) {
        if (this._scanned <= level) {
            AbstractTranslet translet = this._settings.getTranslet();
            Locale[] locales = this._settings.getLocales();
            String[] caseOrder = this._settings.getCaseOrders();
            String str = extractValueFromDOM(this._dom, this._node, level, translet, this._last);
            Comparable key = StringComparable.getComparator(str, locales[level], this._collators[level], caseOrder[level]);
            Object[] objArr = this._values;
            int i2 = this._scanned;
            this._scanned = i2 + 1;
            objArr[i2] = key;
            return key;
        }
        return (Comparable) this._values[level];
    }

    private final Double numericValue(int level) {
        Double num;
        if (this._scanned <= level) {
            AbstractTranslet translet = this._settings.getTranslet();
            String str = extractValueFromDOM(this._dom, this._node, level, translet, this._last);
            try {
                num = new Double(str);
            } catch (NumberFormatException e2) {
                num = new Double(Double.NEGATIVE_INFINITY);
            }
            Object[] objArr = this._values;
            int i2 = this._scanned;
            this._scanned = i2 + 1;
            objArr[i2] = num;
            return num;
        }
        return (Double) this._values[level];
    }

    public int compareTo(NodeSortRecord other) {
        int cmp;
        int[] sortOrder = this._settings.getSortOrders();
        int levels = this._settings.getSortOrders().length;
        int[] compareTypes = this._settings.getTypes();
        for (int level = 0; level < levels; level++) {
            if (compareTypes[level] == 1) {
                Double our = numericValue(level);
                Double their = other.numericValue(level);
                cmp = our.compareTo(their);
            } else {
                Comparable our2 = stringValue(level);
                Comparable their2 = other.stringValue(level);
                cmp = our2.compareTo(their2);
            }
            if (cmp != 0) {
                return sortOrder[level] == 1 ? 0 - cmp : cmp;
            }
        }
        return this._node - other._node;
    }

    public Collator[] getCollator() {
        return this._collators;
    }
}
