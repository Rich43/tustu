package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/DOMWSFilter.class */
public class DOMWSFilter implements DTMWSFilter {
    private AbstractTranslet m_translet;
    private StripFilter m_filter;
    private Map<DTM, short[]> m_mappings = new HashMap();
    private DTM m_currentDTM;
    private short[] m_currentMapping;

    /* JADX WARN: Multi-variable type inference failed */
    public DOMWSFilter(AbstractTranslet abstractTranslet) {
        this.m_translet = abstractTranslet;
        if (abstractTranslet instanceof StripFilter) {
            this.m_filter = (StripFilter) abstractTranslet;
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.DTMWSFilter
    public short getShouldStripSpace(int node, DTM dtm) {
        short[] mapping;
        int type;
        if (this.m_filter != null && (dtm instanceof DOM)) {
            DOM dom = (DOM) dtm;
            if (dtm instanceof DOMEnhancedForDTM) {
                DOMEnhancedForDTM mappableDOM = (DOMEnhancedForDTM) dtm;
                if (dtm == this.m_currentDTM) {
                    mapping = this.m_currentMapping;
                } else {
                    mapping = this.m_mappings.get(dtm);
                    if (mapping == null) {
                        mapping = mappableDOM.getMapping(this.m_translet.getNamesArray(), this.m_translet.getUrisArray(), this.m_translet.getTypesArray());
                        this.m_mappings.put(dtm, mapping);
                        this.m_currentDTM = dtm;
                        this.m_currentMapping = mapping;
                    }
                }
                int expType = mappableDOM.getExpandedTypeID(node);
                if (expType >= 0 && expType < mapping.length) {
                    type = mapping[expType];
                } else {
                    type = -1;
                }
                if (this.m_filter.stripSpace(dom, node, type)) {
                    return (short) 2;
                }
                return (short) 1;
            }
            return (short) 3;
        }
        return (short) 1;
    }
}
