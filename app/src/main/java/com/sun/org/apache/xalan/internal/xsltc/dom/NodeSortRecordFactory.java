package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xalan.internal.utils.ConfigurationError;
import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.Translet;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.utils.LocaleUtility;
import java.text.Collator;
import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/NodeSortRecordFactory.class */
public class NodeSortRecordFactory {
    private static int DESCENDING = Constants.ATTRVAL_ORDER_DESCENDING.length();
    private static int NUMBER = "number".length();
    private final DOM _dom;
    private final String _className;
    private Class _class;
    private SortSettings _sortSettings;
    protected Collator _collator;

    public NodeSortRecordFactory(DOM dom, String className, Translet translet, String[] order, String[] type) throws TransletException {
        this(dom, className, translet, order, type, null, null);
    }

    public NodeSortRecordFactory(DOM dom, String className, Translet translet, String[] order, String[] type, String[] lang, String[] caseOrder) throws TransletException {
        try {
            this._dom = dom;
            this._className = className;
            this._class = translet.getAuxiliaryClass(className);
            if (this._class == null) {
                this._class = ObjectFactory.findProviderClass(className, true);
            }
            int levels = order.length;
            int[] iOrder = new int[levels];
            int[] iType = new int[levels];
            for (int i2 = 0; i2 < levels; i2++) {
                if (order[i2].length() == DESCENDING) {
                    iOrder[i2] = 1;
                }
                if (type[i2].length() == NUMBER) {
                    iType[i2] = 1;
                }
            }
            String[] emptyStringArray = null;
            if (lang == null || caseOrder == null) {
                int numSortKeys = order.length;
                emptyStringArray = new String[numSortKeys];
                for (int i3 = 0; i3 < numSortKeys; i3++) {
                    emptyStringArray[i3] = "";
                }
            }
            lang = lang == null ? emptyStringArray : lang;
            caseOrder = caseOrder == null ? emptyStringArray : caseOrder;
            int length = lang.length;
            Locale[] locales = new Locale[length];
            Collator[] collators = new Collator[length];
            for (int i4 = 0; i4 < length; i4++) {
                locales[i4] = LocaleUtility.langToLocale(lang[i4]);
                collators[i4] = Collator.getInstance(locales[i4]);
            }
            this._sortSettings = new SortSettings((AbstractTranslet) translet, iOrder, iType, locales, collators, caseOrder);
        } catch (ClassNotFoundException e2) {
            throw new TransletException(e2);
        }
    }

    public NodeSortRecord makeNodeSortRecord(int node, int last) throws TransletException, IllegalAccessException, LinkageError, InstantiationException, SecurityException, ConfigurationError {
        NodeSortRecord sortRecord = (NodeSortRecord) this._class.newInstance();
        sortRecord.initialize(node, last, this._dom, this._sortSettings);
        return sortRecord;
    }

    public String getClassName() {
        return this._className;
    }

    private final void setLang(String[] lang) {
    }
}
